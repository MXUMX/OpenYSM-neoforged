/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  org.apache.commons.lang3.StringUtils
 */
package rip.ysm.compat.gun.tacz;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import rip.ysm.compat.gun.swarfare.SWarfareCompat;
import rip.ysm.compat.gun.tacz.TacCompat;

public class ConditionTAC {
    private static final String EMPTY = "";
    private final ObjectOpenHashSet<String> nameTest = new ObjectOpenHashSet();
    private final ObjectOpenHashSet<ResourceLocation> idTest = new ObjectOpenHashSet();

    public void addTest(String name) {
        if (!name.startsWith("tac:") || !name.contains("$")) {
            return;
        }
        String[] strArrSplit = StringUtils.split((String)name, (String)"$", (int)2);
        if (strArrSplit.length < 2) {
            return;
        }
        String str2 = strArrSplit[1];
        if (ResourceLocation.tryParse(str2) != null) {
            this.nameTest.add(name);
            this.idTest.add(ResourceLocation.parse(str2));
        }
    }

    public String doTest(ItemStack itemStack, String str) {
        if (itemStack.isEmpty()) {
            return EMPTY;
        }
        ResourceLocation gunId = TacCompat.getGunTexture(itemStack);
        if (gunId == null && (gunId = SWarfareCompat.getGunTexture(itemStack)) == null) {
            return EMPTY;
        }
        if (this.idTest.contains((Object)gunId)) {
            String str2 = str.substring(0, str.length() - 1) + "$" + String.valueOf(gunId);
            if (this.nameTest.contains((Object)str2)) {
                return str2;
            }
            return EMPTY;
        }
        return EMPTY;
    }
}

