package github.artseb.redarkened.mixin.client;

import github.artseb.redarkened.util.ArmorVisibilityHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "setModelPose", at = @At("RETURN"))
    private void afterSetModelPose(AbstractClientPlayerEntity player, CallbackInfo ci) {
        PlayerEntityModel<AbstractClientPlayerEntity> model =
                ((PlayerEntityRenderer) (Object) this).getModel();
        ArmorVisibilityHelper.hideVanillaPartsIfGecko(player, model);
    }
}