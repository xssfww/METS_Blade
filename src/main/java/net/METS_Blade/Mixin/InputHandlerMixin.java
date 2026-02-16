package net.METS_Blade.Mixin;

import cn.mmf.energyblade.client.InputHandler;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.METS_Blade.Register.METS_BladeSlashArt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(value = InputHandler.class,remap = false)
public class InputHandlerMixin {
    @Inject(at = {@At("HEAD")}, method = {"onPlayerPostTick"}, cancellable = true)
    private static void InjectTick(InputEvent.Key event, CallbackInfo ci){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty()) {
               stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(State -> {
                   if (InputHandler.KEY_CHARGE.isDown()) {
                       if (State.getSlashArts().equals(METS_BladeSlashArt.ConservationEnergy.get())) {
                           ci.cancel();
                       }
                   }
               });
            }
        }
    }
}
