package com.natia.secretmod.mixins;

import com.natia.secretmod.hooks.EndermanModelHook;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderEnderman.class)
public class MixinEndermanRender {
    /*@Inject(method = "doRender", at = @At("HEAD"))
    private void changeEndermanColor(EntityEnderman entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        EndermanModelHook.changeEndermanColor();
    }

    @Inject(method = "doRender", at = @At("RETURN"))
    private void post(EntityEnderman entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        EndermanModelHook.post();
    }*/

    @Inject(method = "getEntityTexture", at = @At("HEAD"), cancellable = true)
    private void getEntityTexture(EntityEnderman entity, CallbackInfoReturnable<ResourceLocation> cir) {
        EndermanModelHook.overrideTexture(entity, cir);
    }

}
