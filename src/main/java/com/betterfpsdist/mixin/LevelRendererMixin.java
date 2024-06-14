package com.betterfpsdist.mixin;

import com.betterfpsdist.BetterfpsdistMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class LevelRendererMixin
{
    @Shadow
    @Final
    private MinecraftClient                         minecraft;
    private ChunkBuilder.BuiltChunk current = null;
    //private HashSet<BlockPos>                 renderedPositions = new HashSet<>();
    //private long                              nextUpdate        = 0;

    @Redirect(method = "renderChunkLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$RenderChunk;getCompiledChunk()Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$CompiledChunk;"))
    public ChunkBuilder.ChunkData on(final ChunkBuilder.BuiltChunk instance)
    {
        current = instance;
        return instance.getData();
    }

    @Redirect(method = "renderChunkLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$CompiledChunk;isEmpty(Lnet/minecraft/client/renderer/RenderType;)Z"))
    public boolean on(final ChunkBuilder.ChunkData instance, final RenderLayer type)
    {
        boolean returnv =
          minecraft.cameraEntity != null && distSqr(minecraft.cameraEntity.getPos(), new Vec3d(current.getOrigin().getX(), current.getOrigin().getY(), current.getOrigin().getZ()))
                                              > (MinecraftClient.getInstance().options.getViewDistance().getValue() * 16) * (MinecraftClient.getInstance().options.getViewDistance().getValue() * 16)
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

    private double distSqr(Vec3d from, Vec3d to)
    {
        double d0 = from.x - to.x;
        double d1 = from.y - to.y;
        double d2 = from.z - to.z;
        return d0 * d0 + BetterfpsdistMod.config.getCommonConfig().stretch * (d1 * d1) + d2 * d2;
    }
}
