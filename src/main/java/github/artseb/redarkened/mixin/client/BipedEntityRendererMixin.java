package github.artseb.redarkened.mixin.client;

import github.artseb.redarkened.util.ArmorVisibilityHelper;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class BipedEntityRendererMixin {

    @Inject(method = "render*", at = @At("RETURN"))
    private void afterRender(LivingEntity entity, float yaw, float tickDelta,
                             MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                             int light, CallbackInfo ci) {
        if (entity instanceof PlayerEntity) return;

        Object model = ((LivingEntityRenderer<?, ?>) (Object) this).getModel();
        if (!(model instanceof BipedEntityModel<?> bipedModel)) return;

        ArmorVisibilityHelper.hideVanillaPartsIfGecko(entity, bipedModel);
    }
}