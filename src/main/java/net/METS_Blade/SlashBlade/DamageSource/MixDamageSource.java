package net.METS_Blade.SlashBlade.DamageSource;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashSet;
import java.util.Set;
public class MixDamageSource extends DamageSource {
    protected final Set<TagKey<DamageType>> RuntimeTags;
    protected final boolean canPass;
    protected static final Set<TagKey<DamageType>> PASS_BLACKLIST = Set.of(
            DamageTypeTags.IS_FIRE,
            DamageTypeTags.IS_FALL,
            DamageTypeTags.IS_EXPLOSION,
            DamageTypeTags.IS_PROJECTILE,
            DamageTypeTags.IS_DROWNING,
            DamageTypeTags.IS_LIGHTNING,
            DamageTypeTags.IS_FREEZING
    );
    public MixDamageSource(Holder<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 initialPosition) {
        super(damageType, directEntity, causingEntity, initialPosition);
        this.RuntimeTags = new HashSet<>();
        this.canPass = false;
    }
    public MixDamageSource(Holder<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 initialPosition, boolean canPass) {
        super(damageType, directEntity, causingEntity, initialPosition);
        this.RuntimeTags = new HashSet<>();
        this.canPass = canPass;
    }
    @Override
    public boolean is(@NotNull TagKey<DamageType> type) {
        if (canPass && !PASS_BLACKLIST.contains(type)) {
            return true;
        }
        return this.RuntimeTags.contains(type) || super.is(type);
    }
    @Override
    public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity killedEntity) {
        Entity killer = killedEntity.getKillCredit();
        if (canPass) {
            if (killer != null) {
                return Component.translatable("death.attack.mix_damage_killer", killedEntity.getDisplayName(), killer.getDisplayName());
            } else {
                return Component.translatable("death.attack.mix_damage", killedEntity.getDisplayName());
            }
        }
        return super.getLocalizedDeathMessage(killedEntity);
    }
    public MixDamageSource addRuntimeTag(TagKey<DamageType> type) {
        this.RuntimeTags.add(type);
        return this;
    }
}
