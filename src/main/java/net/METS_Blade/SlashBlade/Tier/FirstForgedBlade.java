package net.METS_Blade.SlashBlade.Tier;

import cn.mmf.energyblade.item.ItemFEBlade;
import net.METS_Blade.Capability.METS_FECapabilityProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import javax.annotation.Nullable;
public class FirstForgedBlade extends ItemFEBlade {
    public FirstForgedBlade(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        super.initCapabilities(stack, nbt);
        return new METS_FECapabilityProvider(stack, 0, (int) 3e4, (int) 1e4, (int) 1e3, (int) 1e2, (int) 1e1, true);
    }
}
