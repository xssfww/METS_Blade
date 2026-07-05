package net.METS_Blade.Mixin;

import cn.mmf.energyblade.client.InputHandler;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import net.METS_Blade.Register.METS_BladeSlashArt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(value = InputHandler.class,remap = false)
public class InputHandlerMixin {
    @Inject(at = {@At("HEAD")}, method = {"onClientTick"}, cancellable = true)
    private static void InjectTick(ClientTickEvent.Post event, CallbackInfo ci){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty()) {
                BladeStateAccess.of(stack).ifPresent(State -> {
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
