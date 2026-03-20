package github.artseb.redarkened.system.item

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterial
import net.minecraft.item.Vanishable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

open class HammerItem(
    material: ToolMaterial,
    attackDamage: Int,
    attackSpeed: Float,
    settings: Settings
) : ToolItem(material, settings), Vanishable {
    val attackDamage: Float = attackDamage + material.attackDamage

    private val attributeModifiers: Multimap<EntityAttribute, EntityAttributeModifier> by lazy {
        ImmutableMultimap.builder<EntityAttribute, EntityAttributeModifier>()
            .put(
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                EntityAttributeModifier(
                    ATTACK_DAMAGE_MODIFIER_ID,
                    "Weapon modifier",
                    this.attackDamage.toDouble(),
                    EntityAttributeModifier.Operation.ADDITION
                )
            )
            .put(
                EntityAttributes.GENERIC_ATTACK_SPEED,
                EntityAttributeModifier(
                    ATTACK_SPEED_MODIFIER_ID,
                    "Weapon modifier",
                    attackSpeed.toDouble(),
                    EntityAttributeModifier.Operation.ADDITION
                )
            )
            .build()
    }

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        stack.damage(1, attacker) { it.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND) }
        return true
    }

    override fun canMine(state: BlockState?, world: World?, pos: BlockPos?, miner: PlayerEntity): Boolean = !miner.isCreative

    override fun getAttributeModifiers(slot: EquipmentSlot): Multimap<EntityAttribute, EntityAttributeModifier> =
        if (slot == EquipmentSlot.MAINHAND) attributeModifiers else super.getAttributeModifiers(slot)
}