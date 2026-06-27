/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Lighting
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderDispatcher
 *  net.minecraft.network.chat.Component
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 */
package rip.ysm.gui;

import org.openysm.client.entity.LivingAnimatable;
import org.openysm.client.gui.ModelMetadataPresenter;
import org.openysm.client.gui.custom.AbstractConfig;
import org.openysm.client.gui.custom.ExtraAnimationButtons;
import org.openysm.client.gui.custom.configs.CheckboxConfig;
import org.openysm.client.gui.custom.configs.RadioConfig;
import org.openysm.client.gui.custom.configs.RangeConfig;
import org.openysm.client.model.ModelAssembly;
import org.openysm.client.renderer.ModelPreviewRenderer;
import org.openysm.client.renderer.RendererManager;
import org.openysm.geckolib3.core.AnimatableEntity;
import org.openysm.geckolib3.geo.GeoReplacedEntityRenderer;
import org.openysm.util.data.OrderedStringMap;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import rip.ysm.gui.OptionGroup;
import rip.ysm.gui.OptionRow;
import rip.ysm.gui.OptionScreen;
import rip.ysm.gui.components.BooleanOptionRow;
import rip.ysm.gui.components.RadioOptionRow;
import rip.ysm.gui.components.SliderOptionRow;
import rip.ysm.gui.components.groups.IdentifiedGroup;
import rip.ysm.gui.molang.MolangOption;

public class ModelSettingsScreen
extends OptionScreen {
    private final ModelAssembly modelAssembly;
    private final AnimatableEntity<?> animatable;
    @Nullable
    private final String initialGroupId;
    private int previewLeft;
    private int previewTop;
    private int previewRight;
    private int previewBottom;
    private float yaw = 200.0f;
    private float pitch = 0.0f;
    private float zoom = 90.0f;
    private float offsetX = 0.0f;
    private float offsetY = 0.0f;
    private boolean draggingPreview;
    private int draggingButton = -1;

    public ModelSettingsScreen(ModelAssembly modelAssembly, AnimatableEntity<?> animatable, @Nullable Screen parent, @Nullable String initialGroupId) {
        super((Component)Component.translatable((String)"gui.openysm.model_settings.title"), parent);
        this.modelAssembly = modelAssembly;
        this.animatable = animatable;
        this.initialGroupId = initialGroupId;
    }

    @Override
    protected int computePanelWidth() {
        return Math.min(this.width - 40, 640);
    }

    @Override
    protected int computePanelHeight() {
        return Math.min(this.height - 40, 360);
    }

    @Override
    protected boolean shouldUseCompactTabs() {
        return this.width < 620;
    }

    @Override
    protected int computeRowAreaRight() {
        return this.panelRight - this.previewWidth() - 4;
    }

    private int previewWidth() {
        if (this.compactTabs) {
            int panelW = this.panelRight - this.panelLeft;
            return Mth.clamp((int)(panelW / 3), (int)110, (int)180);
        }
        return 200;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(this.applyBtn);
        this.addRenderableWidget(this.undoBtn);
        this.addRenderableWidget(this.cancelBtn);
        this.applyBtn.visible = false;
        this.undoBtn.visible = false;
        this.cancelBtn.visible = false;
        this.applyBtn.active = false;
        this.undoBtn.active = false;
        this.saveBtn.setMessage((Component)Component.translatable((String)"gui.openysm.config.done"));
        this.saveBtn.setX(this.panelRight - this.saveBtn.getWidth());
        this.previewLeft = this.panelRight - this.previewWidth();
        this.previewTop = this.rowAreaTop;
        this.previewRight = this.panelRight;
        this.previewBottom = this.panelBottom - 60;
        if (this.initialGroupId != null) {
            for (OptionGroup g : this.groups) {
                if (!(g instanceof IdentifiedGroup)) continue;
                IdentifiedGroup ig = (IdentifiedGroup)g;
                if (!this.initialGroupId.equals(ig.id)) continue;
                this.selectGroup(g);
                break;
            }
        }
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parentScreen);
        }
    }

    @Override
    protected void collectBlurRegions(List<int[]> out) {
        super.collectBlurRegions(out);
        out.add(new int[]{this.previewLeft, this.previewTop, this.previewRight - this.previewLeft, this.previewBottom - this.previewTop});
    }

    @Override
    protected void registerGroups() {
        ArrayList<ExtraAnimationButtons> ordered = new ArrayList<ExtraAnimationButtons>(this.modelAssembly.getModelData().getModelProperties().getExtraAnimationButtons().values());
        ordered.sort((a, b) -> a.getId().compareTo(b.getId()));
        for (ExtraAnimationButtons cfgGroup : ordered) {
            IdentifiedGroup g = new IdentifiedGroup(cfgGroup.getId(), this.groupLabel(cfgGroup));
            int formIndex = 0;
            for (AbstractConfig form : cfgGroup.getConfigForms()) {
                OptionRow<?> row = this.buildRow(cfgGroup.getId(), formIndex, form);
                if (row != null) {
                    g.add(row);
                }
                ++formIndex;
            }
            this.groups.add(g);
        }
    }

    private String groupLabel(ExtraAnimationButtons group) {
        String fallback = group.getName() == null || group.getName().isEmpty() ? group.getId() : group.getName();
        return ModelMetadataPresenter.getLocalizedModelString(this.modelAssembly, "properties.extra_animation_buttons.%s.name".formatted(group.getId()), fallback);
    }

    @Nullable
    private OptionRow<?> buildRow(String groupId, int formIndex, AbstractConfig form) {
        String title = ModelMetadataPresenter.getLocalizedModelString(this.modelAssembly, "properties.extra_animation_buttons.%s.config_forms.%d.title".formatted(groupId, formIndex), form.getTitle());
        String desc = ModelMetadataPresenter.getLocalizedModelString(this.modelAssembly, "properties.extra_animation_buttons.%s.config_forms.%d.description".formatted(groupId, formIndex), form.getDescription());
        if (form instanceof CheckboxConfig) {
            CheckboxConfig cfg = (CheckboxConfig)form;
            return new BooleanOptionRow(0, 0, 0, 22, MolangOption.ofBoolean(title, desc, this.animatable, cfg.getValue()));
        }
        if (form instanceof RangeConfig) {
            RangeConfig cfg = (RangeConfig)form;
            return new SliderOptionRow(0, 0, 0, 22, MolangOption.ofDouble(title, desc, this.animatable, cfg.getValue()), cfg.getMin(), cfg.getMax(), cfg.getStep(), "");
        }
        if (form instanceof RadioConfig) {
            RadioConfig cfg = (RadioConfig)form;
            OrderedStringMap<String, String> labels = cfg.getLabels();
            ArrayList<String> texts = new ArrayList<String>(labels.size());
            String[] writeExprs = new String[labels.size()];
            for (int i = 0; i < labels.size(); ++i) {
                texts.add(ModelMetadataPresenter.getLocalizedModelString(this.modelAssembly, "properties.extra_animation_buttons.%s.config_forms.%d.labels.%d".formatted(groupId, formIndex, i), labels.getKeyAt(i)));
                writeExprs[i] = labels.getValueAt(i);
            }
            return new RadioOptionRow(0, 0, 0, 22, MolangOption.ofIndex(title, desc, this.animatable, cfg.getValue(), writeExprs), texts);
        }
        return null;
    }

    @Override
    protected void renderExtras(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(this.previewLeft, this.previewTop, this.previewRight, this.previewBottom, 0x66000000);
        this.renderPreview(g, partialTick);
    }

    private void renderPreview(GuiGraphics g, float partialTick) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }
        AnimatableEntity<?> animatableEntity = this.animatable;
        if (!(animatableEntity instanceof LivingAnimatable)) {
            return;
        }
        LivingAnimatable la = (LivingAnimatable)animatableEntity;
        double scale = this.minecraft.getWindow().getGuiScale();
        int sx = (int)((double)this.previewLeft * scale);
        int sy = (int)((double)this.minecraft.getWindow().getHeight() - (double)this.previewBottom * scale);
        int sw = (int)((double)(this.previewRight - this.previewLeft) * scale);
        int sh = (int)((double)(this.previewBottom - this.previewTop) * scale);
        RenderSystem.enableScissor((int)sx, (int)sy, (int)sw, (int)sh);
        float cx = (float)(this.previewLeft + this.previewRight) / 2.0f + this.offsetX;
        float cy = (float)this.previewTop + (float)(this.previewBottom - this.previewTop) * 0.65f + this.offsetY;
        ModelSettingsScreen.renderPlayerForSettings(cx, cy, this.zoom, this.pitch, this.yaw, partialTick, la, RendererManager.getPlayerRenderer());
        RenderSystem.disableScissor();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void renderPlayerForSettings(float x, float y, float scale, float pitch, float yaw, float partialTick, LivingAnimatable animatable, GeoReplacedEntityRenderer renderer) {
        ModelPreviewRenderer.setPreviewMode(true);
        LivingEntity livingEntity = (LivingEntity)animatable.getEntity();
        var modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate(x, y, 1250.0f);
        modelViewStack.scale(1.0f, 1.0f, -1.0f);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack = new PoseStack();
        poseStack.translate(0.0, 0.0, 1000.0);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.8, 0.0);
        Quaternionf rotationZ = Axis.ZP.rotationDegrees(180.0f);
        Quaternionf rotationX = Axis.XP.rotationDegrees(-10.0f + pitch);
        rotationZ.mul((Quaternionfc)rotationX);
        poseStack.mulPose(rotationZ);
        float oldBodyRot = livingEntity.yBodyRot;
        float oldBodyRotO = livingEntity.yBodyRotO;
        float oldYRot = livingEntity.getYRot();
        float oldYRotO = livingEntity.yRotO;
        float oldXRot = livingEntity.getXRot();
        float oldXRotO = livingEntity.xRotO;
        float oldHeadRot = livingEntity.yHeadRot;
        float oldHeadRotO = livingEntity.yHeadRotO;
        livingEntity.yBodyRot = -yaw;
        livingEntity.yBodyRotO = -yaw;
        livingEntity.setYRot(180.0f);
        livingEntity.yRotO = 180.0f;
        livingEntity.setXRot(0.0f);
        livingEntity.xRotO = 0.0f;
        livingEntity.yHeadRot = -yaw;
        livingEntity.yHeadRotO = -yaw;
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        rotationX.conjugate();
        dispatcher.overrideCameraOrientation(rotationX);
        dispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        try {
            RenderSystem.runAsFancy(() -> renderer.renderEntity(animatable, 0.0f, partialTick, poseStack, (MultiBufferSource)bufferSource, 0xF000F0));
            bufferSource.endBatch();
        }
        finally {
            dispatcher.setRenderShadow(true);
            livingEntity.yBodyRot = oldBodyRot;
            livingEntity.yBodyRotO = oldBodyRotO;
            livingEntity.setYRot(oldYRot);
            livingEntity.yRotO = oldYRotO;
            livingEntity.setXRot(oldXRot);
            livingEntity.xRotO = oldXRotO;
            livingEntity.yHeadRot = oldHeadRot;
            livingEntity.yHeadRotO = oldHeadRotO;
            modelViewStack.popMatrix();
            RenderSystem.applyModelViewMatrix();
            Lighting.setupFor3DItems();
            ModelPreviewRenderer.setPreviewMode(false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isInPreview(mouseX, mouseY)) {
            this.draggingPreview = true;
            this.draggingButton = button;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.draggingPreview && button == this.draggingButton) {
            this.draggingPreview = false;
            this.draggingButton = -1;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.draggingPreview && button == this.draggingButton) {
            if (button == 0) {
                this.yaw = (float)((double)this.yaw + dragX * 1.2);
                this.pitch = Mth.clamp((float)((float)((double)this.pitch - dragY * 0.8)), (float)-85.0f, (float)85.0f);
            } else if (button == 1) {
                this.offsetX = (float)((double)this.offsetX + dragX);
                this.offsetY = (float)((double)this.offsetY + dragY);
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        if (this.isInPreview(mouseX, mouseY)) {
            this.zoom = Mth.clamp((float)((float)((double)this.zoom * (1.0 + delta * 0.1))), (float)30.0f, (float)400.0f);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, 0.0, delta);
    }

    private boolean isInPreview(double mouseX, double mouseY) {
        return mouseX >= (double)this.previewLeft && mouseX < (double)this.previewRight && mouseY >= (double)this.previewTop && mouseY < (double)this.previewBottom;
    }
}
