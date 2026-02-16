package net.METS_Blade.SlashBlade.SE;

import cn.mmf.energyblade.energy.FEBladeStorage;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.METS_Blade.Register.METS_BladeEntity;
import net.METS_Blade.Register.METS_BladeSlashEffect;
import net.METS_Blade.SlashBlade.Entity.EntityBloodSword;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber
public class ConservationEnergySE  extends SpecialEffect {
    private static boolean sided = true;
    public ConservationEnergySE() {
        super(25, true, true);
    }
    @SubscribeEvent
    public static void onSlashBladeSwing(SlashBladeEvent.DoSlashEvent event) {
        ISlashBladeState state = event.getSlashBladeState();
        if (state.hasSpecialEffect(METS_BladeSlashEffect.Inch_Surge_Arrow.getId())) {
            LivingEntity user = event.getUser();
            if (user instanceof ServerPlayer player) {
                if ((player.level() instanceof ServerLevel)) {
                    int level = player.experienceLevel;
                    if (SpecialEffect.isEffective(METS_BladeSlashEffect.Inch_Surge_Arrow.get(), level)) {
                            doConservationEnergySE(player, false);
                    }
                }
            }
        }
    }
    public static void doConservationEnergySE(LivingEntity owner, boolean Simulate) {
        ItemStack stack = owner.getMainHandItem();
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            Level worldIn = owner.level();
            if (worldIn instanceof ServerLevel Level) {
                int rank = owner.getCapability(CapabilityConcentrationRank.RANK_POINT).map(r -> r.getRank(Level.getGameTime()).level).orElse(0);
                int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, owner);
                float Attack = state.getBaseAttackModifier();
                float Damage = Attack + (Attack * ((float) (level + state.getRefine() + rank) / 2));
                Vec3 start = owner.getEyePosition(1.0f);
                Vec3 end = start.add(owner.getLookAngle().scale(40));
                Vec3 TargetPos = Level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner)).getLocation();
                Vec3 pos = owner.getEyePosition(1.0F).add(VectorHelper.getVectorForRotation(0.0F, owner.getViewYRot(0.0F) + 90.0F).scale(sided ? 1.0 : -1.0));
                Vec3 dir = TargetPos.subtract(pos).normalize();
                boolean hasEnergy;
                boolean EnergyCheck = stack.getCapability(ForgeCapabilities.ENERGY)
                        .filter(FEBladeStorage.class::isInstance)
                        .map(FEBladeStorage.class::cast)
                        .map(energy -> energy.extractEnergy((int) 1e3, false) > 0)
                        .orElse(false);
                if (owner instanceof ServerPlayer player) {
                    if (player.isCreative()) {
                        hasEnergy = true;
                    } else {
                        hasEnergy = EnergyCheck;
                    }
                } else {
                    hasEnergy = EnergyCheck;
                }
                if (!Simulate) {
                    if (!hasEnergy) {
                        return;
                    }
                }
                int color =sided ?0xFFFFB1:0xFFFF7B;
                EntityBloodSword Swords = new EntityBloodSword(METS_BladeEntity.BloodSwords,Level);
                Swords.setDamage(Damage);
                Swords.setColor(color);
                Swords.setPos(pos);
                Swords.setOwner(owner);
                Swords.setRot(dir);
                Swords.SetTargetPos(dir);
                Swords.setRoll(owner.getRandom().nextFloat() * 360.0f);
                Swords.setDelay(0);
                Level.addFreshEntity(Swords);
                sided = !sided;
            }
        });
    }
}

