package net.METS_Blade.SlashBlade.Util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
public class ToolManager {
    public static Holder<Enchantment> Holder(LivingEntity user, ResourceKey<Enchantment> key) {
        return user.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
    }
}
