package com.betterfpsdist.mixin;

import com.betterfpsdist.BetterfpsdistMod;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.occlusion.OcclusionCuller;
import me.jellysquid.mods.sodium.client.render.viewport.CameraTransform;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OcclusionCuller.class)
public class Sodiummixin
{
    @Inject(method = "isWithinRenderDistance", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void betterfps$renderdistance(
      final CameraTransform camera,
      final RenderSection section,
      final float maxDistance, final CallbackInfoReturnable<Boolean> cir)
    {
        if (MinecraftClient.getInstance().player != null)
        {
            if (distSqr(section.getOriginX(),
              section.getOriginY(),
              section.getOriginZ(),
              MinecraftClient.getInstance().player.getX(),
              MinecraftClient.getInstance().player.getY(),
              MinecraftClient.getInstance().player.getZ())
                  > (MinecraftClient.getInstance().options.getViewDistance().getValue() * 16) * (
              MinecraftClient.getInstance().options.getViewDistance().getValue() * 16))
            {
                cir.setReturnValue(false);
            }
        }
    }

    @Unique
    private static double distSqr(float fromX, float fromY, float fromZ, double toX, double toY, double toZ)
    {
        double d0 = fromX - toX;
        double d1 = fromY - toY;
        double d2 = fromZ - toZ;
        return d0 * d0 + BetterfpsdistMod.config.getCommonConfig().stretch * (d1 * d1) + d2 * d2;
    }
}
