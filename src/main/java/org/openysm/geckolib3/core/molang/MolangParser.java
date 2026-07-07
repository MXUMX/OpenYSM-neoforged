package org.openysm.geckolib3.core.molang;

import org.openysm.OpenYSM;
import org.openysm.client.renderer.AnimationDebugOverlay;
import org.openysm.geckolib3.core.molang.binding.PrimaryBinding;
import org.openysm.geckolib3.core.molang.value.IValue;
import org.openysm.geckolib3.core.molang.value.FloatValue;
import org.openysm.geckolib3.core.molang.value.MolangValue;
import org.openysm.molang.MolangEngine;
import org.openysm.molang.parser.ParseException;
import org.openysm.util.log.ChatLogger;
import net.minecraft.network.chat.Component;

import java.util.Map;

public class MolangParser {

    private final MolangEngine engine;

    private final PrimaryBinding primaryBinding;

    public MolangParser(Map<String, Object> map) {
        this.primaryBinding = new PrimaryBinding(map);
        this.engine = MolangEngine.fromCustomBinding(this.primaryBinding);
    }

    @SuppressWarnings("unused")
    public IValue parseExpression(String molangExpression, boolean isScript) {
        try {
            return parseExpressionUnsafe(molangExpression, isScript);
        } catch (Exception e) {
            if (AnimationDebugOverlay.isDebugActive()) {
                OpenYSM.LOGGER.error("Failed to parse molang expression: {}\n{}", e.getMessage(), molangExpression);
                ChatLogger.INSTANCE.logComponent(Component.translatable("error.openysm.parse_molang_exp").append(e.getMessage()).append("\n----------------------\n").append(molangExpression.replace("\r\n", "\n").replace("\r", "\n")).append("\n----------------------"));
            } else {
                OpenYSM.LOGGER.debug("Failed to parse molang expression: {}\n{}", e.getMessage(), molangExpression);
            }
            return FloatValue.ZERO;
        }
    }

    public IValue parseExpressionUnsafe(String molangExpression, boolean isScript) throws ParseException {
        MolangValue value = new MolangValue(this.engine.parse(preprocessExpression(molangExpression, isScript)), isScript);
        this.primaryBinding.dispose();
        return value;
    }

    private static String preprocessExpression(String input, boolean isScript) {
        String expression = stripComments(input);
        return stripLegacyLabelStatements(expression);
    }

    private static String stripLegacyLabelStatements(String input) {
        String normalized = input.replace("\r\n", "\n").replace('\r', '\n');
        String[] lines = normalized.split("\n", -1);
        StringBuilder builder = new StringBuilder(input.length());
        boolean changed = false;
        for (String line : lines) {
            if (isLegacyLabelStatement(line.trim())) {
                changed = true;
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append('\n');
            }
            builder.append(line);
        }
        return changed ? builder.toString() : input;
    }

    private static boolean isLegacyLabelStatement(String line) {
        if (line.isEmpty()) {
            return false;
        }
        if ("];".equals(line)) {
            return true;
        }
        if (line.length() > 1 && line.startsWith("'") && line.endsWith("'") && containsNonAscii(line)) {
            return true;
        }
        if ((line.startsWith("['") || line.startsWith("'")) && line.endsWith(";") && containsNonAscii(line)) {
            return true;
        }
        if (!line.endsWith(";") || !containsNonAscii(line)) {
            return false;
        }
        String statement = line.substring(0, line.length() - 1).trim();
        if (statement.isEmpty()) {
            return false;
        }
        for (int i = 0; i < statement.length(); i++) {
            int c = statement.codePointAt(i);
            if (!Character.isUnicodeIdentifierPart(c) && c != '_') {
                return false;
            }
            if (Character.charCount(c) == 2) {
                i++;
            }
        }
        return true;
    }

    private static boolean containsNonAscii(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) > 0x7f) {
                return true;
            }
        }
        return false;
    }

    private static String stripComments(String input) {
        StringBuilder resultBuilder = new StringBuilder(input.length());
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inStringLiteral = false;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (inStringLiteral) {
                if (currentChar == '\'') {
                    inStringLiteral = false;
                }
                resultBuilder.append(currentChar);

            } else if (inLineComment) {
                if (currentChar == '\r' || currentChar == '\n') {
                    inLineComment = false;
                    resultBuilder.append('\n');
                }

            } else if (inBlockComment) {
                if (currentChar == '*' && i + 1 < input.length()) {
                    char nextChar = input.charAt(i + 1);
                    if (nextChar == '/') {
                        inBlockComment = false;
                        i++;
                    }
                }

            } else if (currentChar == '\'') {
                inStringLiteral = true;
                resultBuilder.append('\'');

            } else {
                if (currentChar == '/' && i + 1 < input.length()) {
                    char nextChar = input.charAt(i + 1);

                    if (nextChar == '/') {
                        inLineComment = true;
                        i++;
                        continue;
                    }

                    if (nextChar == '*') {
                        inBlockComment = true;
                        i++;
                        continue;
                    }
                }

                resultBuilder.append(currentChar);
            }
        }

        return resultBuilder.toString();
    }

    public IValue toFloatValue(double d) {
        return new FloatValue((float) d);
    }

    public void reset() {
        this.primaryBinding.reset();
    }
}
