package github.artseb.redarkened.util;

import github.artseb.artlib.content.items.gecko.GeckoArmorItem;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class ArmorVisibilityHelper {

    public static void hideVanillaPartsIfGecko(LivingEntity entity, BipedEntityModel<?> model) {
        if (entity.isSpectator()) return;

        if (shouldHide(entity, EquipmentSlot.HEAD)) {
            model.head.visible = false;
            model.hat.visible  = false;
        }
        if (shouldHide(entity, EquipmentSlot.CHEST)) {
            model.body.visible     = false;
            model.rightArm.visible = false;
            model.leftArm.visible  = false;
            if (model instanceof net.minecraft.client.render.entity.model.PlayerEntityModel<?> playerModel) {
                playerModel.jacket.visible      = false;
                playerModel.leftSleeve.visible  = false;
                playerModel.rightSleeve.visible = false;
            }
        }
        if (shouldHideLegs(entity)) {
            model.rightLeg.visible = false;
            model.leftLeg.visible  = false;
            if (model instanceof net.minecraft.client.render.entity.model.PlayerEntityModel<?> playerModel) {
                playerModel.leftPants.visible  = false;
                playerModel.rightPants.visible = false;
            }
        }
    }

    private static boolean shouldHide(LivingEntity entity, EquipmentSlot slot) {
        return entity.getEquippedStack(slot).getItem() instanceof GeckoArmorItem;
    }

    private static boolean shouldHideLegs(LivingEntity entity) {
        return entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof GeckoArmorItem
                && entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof GeckoArmorItem;
    }
}