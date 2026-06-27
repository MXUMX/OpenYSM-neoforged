package org.openysm.client.animation.molang.functions.ysm;

import org.openysm.molang.runtime.ExecutionContext;
import org.openysm.molang.runtime.Function;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModVersion implements Function {
    @Override
    @Nullable
    public Object evaluate(@NotNull ExecutionContext<?> context, @NotNull Function.ArgumentCollection arguments) {
        String modid = arguments.getAsString(context, 0);
        if (modid == null) {
            return null;
        }
        return ModList.get().getModContainerById(modid).map(modContainer -> MavenVersionStringHelper.artifactVersionToString(modContainer.getModInfo().getVersion())).orElse(null);
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 1;
    }
}