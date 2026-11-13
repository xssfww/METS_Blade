package net.METS_Blade.SlashBlade.Util;

import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.List;
public class ToolManager {
    public static List<Entity> getEntity(Level level, EntityAbstractSummonedSword summonedSword, double radius) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return List.of();
        }
        Vec3 pos = summonedSword.position();
        AABB aabb = new AABB(
                pos.x - radius, pos.y - radius, pos.z - radius,
                pos.x + radius, pos.y + radius, pos.z + radius
        );
        List<Entity> entities = serverLevel.getEntitiesOfClass(Entity.class, aabb);
        Entity shooter = summonedSword.getShooter();
        if (shooter != null) {
            entities.remove(shooter);
        }
        return entities;
    }
}

