package net.METS_Blade.CoreMod.Helper;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
public class SealHelper {
    private static final String SEAL_TAG = "mets_sealed";
    private static final String DESTROY_TAG = "mets_destroy";
    @SuppressWarnings("unused")
    public static boolean isSealed(LivingEntity entity) {
        return entity.getPersistentData().getBoolean(SEAL_TAG);
    }
    @SuppressWarnings("unused")
    public static boolean isDestroy(LivingEntity entity) {
        if (entity == null) return false;
        if (!(entity.level() instanceof ServerLevel)) return false;
        return entity.getPersistentData().getFloat(DESTROY_TAG) >= entity.getMaxHealth();
    }
    public static void seal(LivingEntity entity,float amount) {
        if (entity == null) return;
        if (isSealed(entity)) {
            entity.getPersistentData().putFloat(DESTROY_TAG, entity.getPersistentData().getFloat(DESTROY_TAG) + (amount / 66F));
        } else {
            entity.getPersistentData().putBoolean(SEAL_TAG, true);
        }
    }
}
