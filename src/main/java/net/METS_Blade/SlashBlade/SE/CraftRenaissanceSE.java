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
import net.METS_Blade.SlashBlade.Entity.EntityCraftSword;
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
public class CraftRenaissanceSE extends SpecialEffect {
    private static boolean sided = true;
    public CraftRenaissanceSE() {
        super(35, true, true);
    }
    @SubscribeEvent
    public static void onSlashBladeSwing(SlashBladeEvent.DoSlashEvent event) {
        ISlashBladeState state = event.getSlashBladeState();
        if (state.hasSpecialEffect(METS_BladeSlashEffect.Divine_Surge_Fire_Arrow.getId())) {
            LivingEntity user = event.getUser();
            if (user instanceof ServerPlayer player) {
                if ((player.level() instanceof ServerLevel)) {
                    int level = player.experienceLevel;
                    if (SpecialEffect.isEffective(METS_BladeSlashEffect.Divine_Surge_Fire_Arrow.get(), level)) {
                        doCraftRenaissanceSE(player,false);
                    }
                }
            }
        }
    }
    public static void doCraftRenaissanceSE(LivingEntity owner,boolean simulate) {
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
                        .map(energy -> energy.extractEnergy((int) 1e5, false) > 0)
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
                if (!simulate) {
                    if (!hasEnergy) {
                        return;
                    }
                }
                EntityCraftSword Swords = new EntityCraftSword(METS_BladeEntity.CraftSwords,Level);
                Swords.setDamage(Damage);
                Swords.setColor(16711680);
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
