package com.betterfpsdist.mixin;

import com.betterfpsdist.BetterfpsdistMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin
{
    @Shadow
    @Final
    private Minecraft                         minecraft;
    private SectionRenderDispatcher.RenderSection current = null;
    //private HashSet<BlockPos>                 renderedPositions = new HashSet<>();
    //private long                              nextUpdate        = 0;

    @Redirect(method = "RenderSectionLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSection;getCompiled()Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$CompiledSection;"))
    public SectionRenderDispatcher.CompiledSection on(final SectionRenderDispatcher.RenderSection instance)
    {
        current = instance;
        return instance.getCompiled();
    }

    @Redirect(method = "RenderSectionLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$CompiledSection;isEmpty(Lnet/minecraft/client/renderer/RenderType;)Z"))
    public boolean on(final SectionRenderDispatcher.CompiledSection instance, final RenderType type)
    {
        boolean returnv =
          minecraft.cameraEntity != null && distSqr(minecraft.cameraEntity.position(), new Vec3(current.getOrigin().getX(), current.getOrigin().getY(), current.getOrigin().getZ()))
                                              > (Minecraft.getInstance().options.renderDistance().get() * 16) * (Minecraft.getInstance().options.renderDistance().get() * 16)
            || instance.isEmpty(type);

/*
        if (Minecraft.getInstance().player.level.getGameTime() > nextUpdate)
        {
            nextUpdate = Minecraft.getInstance().player.level.getGameTime() + 20 * 5;
            BetterfpsdistMod.LOGGER.warn("Rendered Sections:" + renderedPositions.size());
            renderedPositions.clear();
        }

        if (!returnv)
        {
            renderedPositions.add(current.getOrigin());
        }*/

        return returnv;
    }

    private double distSqr(Vec3 from, Vec3 to)
    {
        double d0 = from.x - to.x;
        double d1 = from.y - to.y;
        double d2 = from.z - to.z;
        return d0 * d0 + BetterfpsdistMod.config.getCommonConfig().stretch * (d1 * d1) + d2 * d2;
    }
}
