package net.METS_Blade.Register;

import com.google.common.base.CaseFormat;
import net.METS_Blade.METS_SlashBlade;
import net.METS_Blade.SlashBlade.Entity.EntityBloodSword;
import net.METS_Blade.SlashBlade.Entity.EntityCommonSword;
import net.METS_Blade.SlashBlade.Entity.EntityCraftSword;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class METS_BladeEntity {
    public static final ResourceLocation CommonSwordLoc = ResourceLocation.fromNamespaceAndPath(METS_SlashBlade.MODID, classToString(EntityCommonSword.class));
    public static EntityType<EntityCommonSword> CommonSwords;
    public static final ResourceLocation BloodSwordsLoc = ResourceLocation.fromNamespaceAndPath(METS_SlashBlade.MODID, classToString(EntityBloodSword.class));
    public static EntityType<EntityBloodSword> BloodSwords;
    public static final ResourceLocation CraftSwordsLoc = ResourceLocation.fromNamespaceAndPath(METS_SlashBlade.MODID, classToString(EntityCraftSword.class));
    public static EntityType<EntityCraftSword> CraftSwords;
    @SubscribeEvent
    public static void register(RegisterEvent event) {
     event.register(ForgeRegistries.Keys.ENTITY_TYPES, helper -> {
         {
             EntityType<EntityCommonSword> Common = CommonSwords = EntityType.Builder
                     .of(EntityCommonSword::new, MobCategory.MISC).sized(0.5F, 0.5F).setTrackingRange(4)
                     .setUpdateInterval(20).setCustomClientFactory((packet, worldIn) -> EntityCommonSword.createInstance(worldIn))
                     .build(CommonSwordLoc.toString());
             helper.register(CommonSwordLoc, Common);
             EntityType<EntityBloodSword> Blood = BloodSwords = EntityType.Builder
                     .of(EntityBloodSword::new, MobCategory.MISC).sized(0.5F, 0.5F).setTrackingRange(4)
                     .setUpdateInterval(20).setCustomClientFactory((packet, worldIn) -> EntityBloodSword.createInstance(worldIn))
                     .build(BloodSwordsLoc.toString());
             helper.register(BloodSwordsLoc, Blood);
             EntityType<EntityCraftSword> Craft = CraftSwords = EntityType.Builder
                     .of(EntityCraftSword::new, MobCategory.MISC).sized(0.5F, 0.5F).setTrackingRange(4)
                     .setUpdateInterval(20).setCustomClientFactory((packet, worldIn) -> EntityCraftSword.createInstance(worldIn))
                     .build(CraftSwordsLoc.toString());
             helper.register(CraftSwordsLoc, Craft);
         }
     });
    }
    private static String classToString(Class<? extends Entity> entityClass) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()).replace("entity_", "");
    }
}

