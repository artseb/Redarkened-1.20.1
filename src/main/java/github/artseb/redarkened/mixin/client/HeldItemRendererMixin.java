package github.artseb.redarkened.mixin.client;

import github.artseb.artlib.content.items.gecko.GeckoArmorItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.lang.reflect.Method;
import java.util.Optional;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Shadow @Final private MinecraftClient client;
    @Unique private static final Logger LOGGER = LoggerFactory.getLogger("HeldItemRendererMixin");

    /**
     * Injects at the tail of renderArmHoldingItem — this is the Yarn name for
     * what ISMAH calls renderPlayerArm (Mojang mappings). They are the same method.
     * This is the correct injection point because:
     * - The matrix stack is specifically set up for first-person arm rendering
     * - The translate(0, 1.5, 0) + Z180 transform only makes sense in THIS space
     * - ISMAH uses this exact method and transform successfully
     * Note: this fires for empty hand and map-in-one-hand only.
     * When holding a normal item the arm renders inline inside renderFirstPersonItem
     * via a direct playerEntityRenderer.renderRightArm/renderLeftArm call,
     * which would need a separate injection with a different transform.
     */
    @Inject(method = "renderArmHoldingItem", at = @At("TAIL"))
    private void afterRenderArmHoldingItem(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            float equipProgress,
            float swingProgress,
            Arm arm,
            CallbackInfo ci) {

        if (client.player == null || client.player.isInvisible()) return;

        ItemStack chestplate = client.player.getEquippedStack(EquipmentSlot.CHEST);
        if (!(chestplate.getItem() instanceof GeckoArmorItem)) return;

        renderGeoArmor(matrices, vertexConsumers, light,
                (Item & GeoItem) chestplate.getItem(), chestplate, arm, client.player);
    }

    @Unique
    private <T extends Item & GeoItem> void renderGeoArmor(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            T geoArmorItem,
            ItemStack chestplate,
            Arm arm,
            AbstractClientPlayerEntity player) {

        // --- Get GeoArmorRenderer via reflection ---
        Object renderProvider = geoArmorItem.getRenderProvider().get();
        GeoArmorRenderer<T> renderer = null;

        try {
            PlayerEntityRenderer playerRenderer = (PlayerEntityRenderer)
                    client.getEntityRenderDispatcher().getRenderer(player);

            Method getHumanoidArmorModel = renderProvider.getClass().getDeclaredMethod(
                    "getHumanoidArmorModel",
                    LivingEntity.class, ItemStack.class, EquipmentSlot.class, BipedEntityModel.class
            );
            getHumanoidArmorModel.setAccessible(true);

            Object result = getHumanoidArmorModel.invoke(
                    renderProvider, player, chestplate, EquipmentSlot.CHEST, playerRenderer.getModel()
            );
            if (result instanceof GeoArmorRenderer<?>) {
                //noinspection unchecked
                renderer = (GeoArmorRenderer<T>) result;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get GeoArmorRenderer: ", e);
        }

        if (renderer == null) return;

        // --- Prep renderer ---
        PlayerEntityRenderer playerRenderer = (PlayerEntityRenderer)
                client.getEntityRenderDispatcher().getRenderer(player);
        renderer.prepForRender(player, chestplate, EquipmentSlot.CHEST, playerRenderer.getModel());

        // --- Find parent arm bone ---
        BakedGeoModel bakedModel = renderer.getGeoModel().getBakedModel(
                renderer.getGeoModel().getModelResource(geoArmorItem, renderer));

        // Use the parent biped bone so its pivot is included when walking the tree
        String boneName = arm == Arm.LEFT ? "bipedLeftArm" : "bipedRightArm";
        Optional<GeoBone> armBoneOpt = bakedModel.getBone(boneName);
        if (armBoneOpt.isEmpty()) {
            LOGGER.error("Bone '{}' not found!", boneName);
            return;
        }
        GeoBone armBone = armBoneOpt.get();

        // Force visibility — GeckoLib's 3rd-person pass hides arm bones when
        // only chest slot is active, and they stay hidden when returning to 1st person
        forceVisible(armBone);

        // --- Render ---
        Identifier texture = renderer.getTextureLocation(geoArmorItem);
        RenderLayer renderType = renderer.getRenderType(geoArmorItem, texture, vertexConsumers, 0.0f);
        VertexConsumer buffer = vertexConsumers.getBuffer(renderType);

        matrices.push();
        // This transform is correct specifically for renderArmHoldingItem's matrix space.
        // It is the same transform ISMAH uses at this same injection point.
        matrices.translate(0.0f, 1.5f, 0.0f);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        renderBoneAndChildren(matrices, armBone, buffer, light);
        matrices.pop();
    }

    @Unique
    private void forceVisible(GeoBone bone) {
        bone.setHidden(false);
        for (GeoBone child : bone.getChildBones()) {
            child.setHidden(false);
            for (GeoBone grandchild : child.getChildBones()) {
                grandchild.setHidden(false);
            }
        }
    }

    @Unique
    private void renderBoneAndChildren(MatrixStack matrices, GeoBone bone,
                                       VertexConsumer buffer, int light) {
        if (bone.isHidden()) return;

        matrices.push();

        // Apply pivot and scale ONLY — no rotations.
        // bone.getRotX/Y/Z() contains GeckoLib's animation state (idle, equip, etc.)
        // which causes the arm to bend. The first-person matrix stack already
        // handles arm swing/bob, so we just want the rest pose geometry.
        matrices.translate(bone.getPivotX() / 16f, bone.getPivotY() / 16f, bone.getPivotZ() / 16f);
        matrices.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
        matrices.translate(-bone.getPivotX() / 16f, -bone.getPivotY() / 16f, -bone.getPivotZ() / 16f);

        for (GeoCube cube : bone.getCubes()) {
            for (GeoQuad quad : cube.quads()) {
                Vector3f normal = quad.normal().normalize();
                MatrixStack.Entry entry = matrices.peek();
                for (GeoVertex vertex : quad.vertices()) {
                    Vector4f pos = new Vector4f(
                            vertex.position().x(), vertex.position().y(),
                            vertex.position().z(), 1.0f);
                    entry.getPositionMatrix().transform(pos);
                    buffer.vertex(pos.x(), pos.y(), pos.z(),
                            1f, 1f, 1f, 1f,
                            vertex.texU(), vertex.texV(),
                            OverlayTexture.DEFAULT_UV, light,
                            normal.x(), normal.y(), normal.z());
                }
            }
        }

        for (GeoBone child : bone.getChildBones()) {
            if (!child.isHidden() && !bone.isHidingChildren()) {
                renderBoneAndChildren(matrices, child, buffer, light);
            }
        }

        matrices.pop();
    }
}