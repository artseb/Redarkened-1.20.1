package github.artseb.redarkened.system.classes

import github.artseb.redarkened.content.classes.RegistrableClass
import github.artseb.redarkened.util.ModClasses
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import java.util.*

object ClassApplier {
    private val HEALTH_MOD = UUID.fromString("11111111-1111-1111-1111-111111111111")
    private val MOVEMENT_MOD = UUID.fromString("22222222-2222-2222-2222-222222222222")
    private val ATTACK_SPEED_MOD = UUID.fromString("33333333-3333-3333-3333-333333333333")
    private val KNOCKBACK_RESIST_MOD = UUID.fromString("44444444-4444-4444-4444-444444444444")
    private val ATTACK_DAMAGE_MOD = UUID.fromString("55555555-5555-5555-5555-555555555555")

    private val playerDefenseModifier = WeakHashMap<PlayerEntity, Float>()

    @JvmStatic
    fun applyClass(player: ServerPlayerEntity, classId: Identifier) {
        val coreClass = ModClasses.CLASSES[classId] ?: return
        val combined = combineWithParent(coreClass)
        clearClass(player)

        val attributes = player.attributes

        if (combined.healthModifier != 0f) {
            val modifier = EntityAttributeModifier(
                HEALTH_MOD,
                "Class health modifier",
                combined.healthModifier.toDouble(),
                EntityAttributeModifier.Operation.MULTIPLY_BASE
            )
            attributes.getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH)?.addTemporaryModifier(modifier)
        }
        if (combined.movementSpeedModifier != 0.0) {
            val modifier = EntityAttributeModifier(
                MOVEMENT_MOD,
                "Class speed modifier",
                combined.movementSpeedModifier,
                EntityAttributeModifier.Operation.MULTIPLY_BASE
            )
            attributes.getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)?.addTemporaryModifier(modifier)
        }
        if (combined.attackSpeedModifier != 0f) {
            val modifier = EntityAttributeModifier(
                ATTACK_SPEED_MOD,
                "Class attack speed modifier",
                combined.attackSpeedModifier.toDouble(),
                EntityAttributeModifier.Operation.MULTIPLY_BASE
            )
            attributes.getCustomInstance(EntityAttributes.GENERIC_ATTACK_SPEED)?.addTemporaryModifier(modifier)
        }
        if (combined.knockbackResistanceModifier != 0f) {
            val modifier = EntityAttributeModifier(
                KNOCKBACK_RESIST_MOD,
                "Class knockback resist modifier",
                combined.knockbackResistanceModifier.toDouble(),
                EntityAttributeModifier.Operation.ADDITION
            )
            attributes.getCustomInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)?.addTemporaryModifier(modifier)
        }
        if (combined.damageModifier != 0f) {
            val modifier = EntityAttributeModifier(
                ATTACK_DAMAGE_MOD,
                "Class damage modifier",
                combined.damageModifier.toDouble(),
                EntityAttributeModifier.Operation.MULTIPLY_BASE
            )
            attributes.getCustomInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)?.addTemporaryModifier(modifier)
        }
        playerDefenseModifier[player] = combined.defenseModifier

        player.health = player.maxHealth
    }

    @JvmStatic
    fun clearClass(player: ServerPlayerEntity) {
        val attributes = player.attributes
        attributes.getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH)?.removeModifier(HEALTH_MOD)
        attributes.getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)?.removeModifier(MOVEMENT_MOD)
        attributes.getCustomInstance(EntityAttributes.GENERIC_ATTACK_SPEED)?.removeModifier(ATTACK_SPEED_MOD)
        attributes.getCustomInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)?.removeModifier(KNOCKBACK_RESIST_MOD)
        attributes.getCustomInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)?.removeModifier(ATTACK_DAMAGE_MOD)
        playerDefenseModifier.remove(player)
    }

    @JvmStatic
    fun getDefenseModifier(player: PlayerEntity): Float {
        return playerDefenseModifier[player] ?: 0f
    }

    private fun combineWithParent(coreClass: RegistrableClass): CoreClass {
        val parentId = coreClass.parentId ?: return coreClass.instance
        val parent = ModClasses.CLASSES[parentId]?.instance ?: return coreClass.instance

        return CoreClass(
            parent.healthModifier + coreClass.instance.healthModifier,
            parent.defenseModifier + coreClass.instance.defenseModifier,
            parent.damageModifier + coreClass.instance.damageModifier,
            parent.movementSpeedModifier + coreClass.instance.movementSpeedModifier,
            parent.attackSpeedModifier + coreClass.instance.attackSpeedModifier,
            parent.knockbackResistanceModifier + coreClass.instance.knockbackResistanceModifier,
            coreClass.instance.weaponProficiencies.ifEmpty { parent.weaponProficiencies },
            parent.abilities + coreClass.instance.abilities
        )
    }
}