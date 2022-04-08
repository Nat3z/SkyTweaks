package mixin.natia.skytweaks.mixins;

import natia.skytweaks.SecretUtils;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiEditSign.class)
public class MixinGuiEditSign {

    @Shadow private TileEntitySign tileSign;

    @Inject(method = "initGui", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        SecretUtils.INSTANCE.setCURRENT_SIGN(this.tileSign);
    }
}
