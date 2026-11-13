package net.METS_Blade.SlashBlade.SE;

import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.specialeffects.SpecialEffect;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.METS_Blade.Capability.METS_FEBladeStorage;
import net.METS_Blade.Register.METS_BladeEntity;
import net.METS_Blade.Register.METS_BladeSlashEffect;
import net.METS_Blade.SlashBlade.Entity.EntityCraftSword;
import net.METS_Blade.SlashBlade.Util.SEBladeSwitch;
import net.METS_Blade.SlashBlade.Util.ToolManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
@EventBusSubscriber
public class CraftRenaissanceSE extends SpecialEffect {
    public CraftRenaissanceSE() {
        super(35, true, true);
    }
    @SubscribeEvent
    public static void onSlashBladeSwing(SlashBladeEvent.DoSlashEvent event) {
        ISlashBladeState state = event.getSlashBladeState();
        if (state.hasSpecialEffect(METS_BladeSlashEffect.Divine_Surge_Fire_Arrow.getId())) {
            LivingEntity user = event.getUser();
            if (user instanceof ServerPlayer player) {
                if (SEBladeSwitch.isDisabled(player)) {
                    return;
                }
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
        BladeStateAccess.of(stack).ifPresent(State -> {
            Level worldIn = owner.level();
            if (worldIn instanceof ServerLevel Level) {
                int rank = owner.getData(CapabilityConcentrationRank.RANK_POINT).getRank(Level.getGameTime()).level;
                int level = EnchantmentHelper.getEnchantmentLevel(ToolManager.Holder(owner, Enchantments.POWER), owner);
                float Attack = State.getBaseAttackModifier();
                float Damage = Attack + (Attack * ((float) (level + State.getRefine() + rank) / 2));
                if (!owner.getPersistentData().contains("blade_side")) {
                    owner.getPersistentData().putBoolean("blade_side", false);
                }
                boolean sided = owner.getPersistentData().getBoolean("blade_side");
                Vec3 start = owner.getEyePosition(1.0f);
                Vec3 end = start.add(owner.getLookAngle().scale(40));
                Vec3 TargetPos = Level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner)).getLocation();
                Vec3 pos = owner.getEyePosition(1.0F).add(VectorHelper.getVectorForRotation(0.0F, owner.getViewYRot(0.0F) + 90.0F).scale(sided ? 1.0 : -1.0));
                Vec3 dir = TargetPos.subtract(pos).normalize();
                boolean hasEnergy;
                IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                if (energyStorage instanceof METS_FEBladeStorage FEBlade) {
                    boolean EnergyCheck = FEBlade.extractEnergy((int) 1e5, false) > 0;
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
                    EntityCraftSword Swords = new EntityCraftSword(METS_BladeEntity.CraftSwords, Level);
                    Swords.setDamage(Damage);
                    Swords.setColor(16711680);
                    Swords.setPos(pos);
                    Swords.setOwner(owner);
                    Swords.setRot(dir);
                    Swords.setTargetPos(dir);
                    Swords.setRoll(owner.getRandom().nextFloat() * 360.0f);
                    Swords.setDelay(0);
                    owner.getPersistentData().putBoolean("blade_side", !sided);
                    Level.addFreshEntity(Swords);
                }
            }
        });
    }
}
