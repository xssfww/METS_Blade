package net.METS_Blade.SlashBlade.Util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
public class SEBladeSwitch {
    private static final String KEY = "mets_blade_se_disabled";
    public static boolean isDisabled(Entity entity) {
        return entity != null && entity.getPersistentData().getBoolean(KEY);
    }
    public static boolean toggle(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        boolean nowDisabled = !data.getBoolean(KEY);
        data.putBoolean(KEY, nowDisabled);
        return !nowDisabled;
    }
}
