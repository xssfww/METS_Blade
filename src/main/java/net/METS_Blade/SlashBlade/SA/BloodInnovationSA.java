package net.METS_Blade.SlashBlade.SA;

import cn.mmf.energyblade.energy.FEBladeStorage;
import mods.flammpfeil.slashblade.ability.SummonedSwordArts;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.METS_Blade.Register.METS_BladeEntity;
import net.METS_Blade.SlashBlade.Entity.EntityBloodSword;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import java.util.Optional;
public class BloodInnovationSA {
    public static void doBloodInnovationSA(LivingEntity owner, boolean simulate) {
        ItemStack stack = owner.getMainHandItem();
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            Level worldIn = owner.level();
            if (worldIn instanceof ServerLevel Level) {
                Optional<Entity> foundTarget = Optional.empty();
                if (owner instanceof ServerPlayer player) {
                    foundTarget = SummonedSwordArts.getInstance().findTarget(player, state.getTargetEntity(Level));
                }
                Vec3 targetPos = foundTarget.map(e -> new Vec3(e.getX(), e.getY() + e.getEyeHeight() * 0.5, e.getZ())).orElseGet(() -> {
                    Vec3 start = owner.getEyePosition(1.0f);
                    Vec3 end = start.add(owner.getLookAngle().scale(40));
                    HitResult result = Level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner));
                    return result.getLocation();
                });
                int count = Math.min(16 + (state.getRefine() * 2), 64);
                for (int i = 0; i < count; i++) {
                    boolean select = (i % 2 == 0);
                    int color = select ? 8262676 : 10040372;
                    float Attack = state.getBaseAttackModifier();
                    int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, owner);
                    int rank = owner.getCapability(CapabilityConcentrationRank.RANK_POINT).map(r -> r.getRank(Level.getGameTime()).level).orElse(0);
                    float Damage = foundTarget.filter(entity -> entity instanceof LivingEntity)
                            .map(entity -> {
                                LivingEntity livingEntity = (LivingEntity) entity;
                                return (livingEntity.getMaxHealth() / 2.0f) + Attack + (Attack * ((level + state.getRefine() + rank) / 2));
                            })
                            .orElseGet(() -> Attack + (Attack * ((level + state.getRefine() + rank / 2))));
                    float yawRad = owner.yBodyRotO * (float) Math.PI / 180.0f;
                    double a = Math.cos(3 * i) + 3 * Math.cos(i * Math.PI) + 0.5;
                    double offsetY = Math.tan(3 * i) + 0.5;
                    Vec3 pos;
                    if (select) {
                        double rotatedX = a * Math.cos(yawRad) - a * Math.sin(yawRad);
                        double rotatedZ = a * Math.sin(yawRad) + a * Math.cos(yawRad);
                        pos = new Vec3(
                                owner.position().x() + rotatedX,
                                owner.position().y() + offsetY,
                                owner.position().z() + rotatedZ
                        );
                    } else {
                        double angle = yawRad - Math.PI / 2;
                        double rotatedX = a * Math.cos(angle) - a * Math.sin(angle);
                        double rotatedZ = a * Math.sin(angle) + a * Math.cos(angle);
                        pos = new Vec3(
                                owner.position().x() + rotatedX,
                                owner.position().y() + offsetY,
                                owner.position().z() + rotatedZ
                        );
                    }
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
                            break;
                        }
                    }
                    Vec3 dir = targetPos.subtract(pos).normalize();
                    EntityBloodSword Swords = new EntityBloodSword(METS_BladeEntity.BloodSwords, Level);
                    Swords.setDamage(Damage);
                    Swords.setPos(pos);
                    Swords.setOwner(owner);
                    Swords.setColor(color);
                    Swords.setRot(dir);
                    Swords.SetTargetPos(dir);
                    Swords.setRoll(owner.getRandom().nextFloat() * 360.0f);
                    Swords.setDelay(i / 3);
                    Level.addFreshEntity(Swords);
                }
            }
        });
    }
}
