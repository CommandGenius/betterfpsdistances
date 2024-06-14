package com.betterfpsdist.mixin;

import com.betterfpsdist.event.ClientEventHandler;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public class VideoSettingsScreenMixin
{
    @Shadow
    private OptionListWidget list;

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/OptionsList;addSmall([Lnet/minecraft/client/OptionInstance;)V"))
    public void on(final CallbackInfo ci)
    {
        list.addSingleOptionEntry(ClientEventHandler.chunkrenderdist);
    }
}