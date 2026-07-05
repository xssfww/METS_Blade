package net.METS_Blade.SlashBlade.SA;

import mods.flammpfeil.slashblade.ability.SummonedSwordArts;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.METS_Blade.Capability.METS_FEBladeStorage;
import net.METS_Blade.Register.METS_BladeEntity;
import net.METS_Blade.SlashBlade.Entity.EntityCraftSword;
import net.METS_Blade.SlashBlade.Util.ToolManager;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import java.util.Optional;
public class CraftRenaissanceSA {
    private static final int[] color = new int[] { 16711680, 16776960, 255, 65280, 65535, 41215, 9072383 };
    public static void doCraftRenaissanceSA(LivingEntity owner, boolean simulate) {
        ItemStack stack = owner.getMainHandItem();
        BladeStateAccess.of(stack).ifPresent(State -> {
            Level worldIn = owner.level();
            if (worldIn instanceof ServerLevel Level) {
                Optional<Entity> foundTarget = Optional.empty();
                if (owner instanceof ServerPlayer player) {
                    foundTarget = SummonedSwordArts.getInstance().findTarget(player, State.getTargetEntity(Level));
                }
                Vec3 targetPos = foundTarget
                        .map(e -> new Vec3(e.getX(), e.getY() + e.getEyeHeight() * 0.5, e.getZ()))
                        .orElseGet(() -> {
                            Vec3 start = owner.getEyePosition(1.0f);
                            Vec3 end = start.add(owner.getLookAngle().scale(40));
                            HitResult result = Level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner));
                            return result.getLocation();
                        });
                int count = Math.min(7 + (State.getRefine() * 2), 32);
                for (int i = 0; i < count; i++) {
                    boolean select = (i % 2 == 0);
                    int c = color[i % color.length];
                    float Attack = State.getBaseAttackModifier();
                    int level = EnchantmentHelper.getEnchantmentLevel(ToolManager.Holder(owner, Enchantments.POWER), owner);
                    int rank = owner.getData(CapabilityConcentrationRank.RANK_POINT).getRank(Level.getGameTime()).level;
                    float Damage = foundTarget.filter(entity -> entity instanceof LivingEntity)
                            .map(entity -> {
                                LivingEntity livingEntity = (LivingEntity) entity;
                                return (livingEntity.getMaxHealth() / 2.0f) + Attack + (Attack * ((level + State.getRefine() + rank) / 2));
                            })
                            .orElseGet(() -> Attack + (Attack * ((level + State.getRefine() + rank / 2))));
                    float yawRad = owner.yBodyRotO * (float) Math.PI / 180.0f;
                    double a = Math.cos(3 * i) + 3 * Math.cos(i * Math.PI) + 0.5;
                    double offsetY = Math.tan(3 * i) + 0.75;
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
                    IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                    if (energyStorage instanceof METS_FEBladeStorage FEBlade) {
                        boolean EnergyCheck = FEBlade.extractEnergy((int) 1e5, false) > 0;
                        if (owner instanceof ServerPlayer player) {
                            hasEnergy = player.isCreative() || EnergyCheck;
                        } else {
                            hasEnergy = EnergyCheck;
                        }
                        if (!simulate && !hasEnergy) break;
                        Vec3 dir = targetPos.subtract(pos).normalize();
                        EntityCraftSword Swords = new EntityCraftSword(METS_BladeEntity.CraftSwords, Level);
                        Swords.setDamage(Damage);
                        Swords.setColor(c);
                        Swords.setPos(pos);
                        Swords.setOwner(owner);
                        Swords.setRot(dir);
                        Swords.setTargetPos(dir);
                        foundTarget.ifPresent(entity -> Swords.setTargetId(entity.getId()));
                        Swords.setRoll(owner.getRandom().nextFloat() * 360.0f);
                        Swords.setDelay(1 + i / 2);
                        Level.addFreshEntity(Swords);
                    }
                }
            }
        });
    }
}