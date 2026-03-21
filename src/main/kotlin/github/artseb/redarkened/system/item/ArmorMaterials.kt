package github.artseb.redarkened.system.item

import github.artseb.redarkened.util.ModAssets
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Lazy
import net.minecraft.util.Util
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

enum class ArmorMaterials(
    private val armorName: String,
    private val durabilityMultiplier: Int,
    private val protectionAmounts: EnumMap<ArmorItem.Type, Int>,
    private val enchantability: Int,
    private val equipSound: SoundEvent,
    private val toughness: Float,
    private val knockbackResistance: Float,
    repairIngredient: Supplier<Ingredient?>
) : ArmorMaterial {
    ELECTRUM(
        "electrum",
        37,
        Util.make(EnumMap(ArmorItem.Type::class.java), Consumer { map ->
            map[ArmorItem.Type.BOOTS] = 3
            map[ArmorItem.Type.LEGGINGS] = 6
            map[ArmorItem.Type.CHESTPLATE] = 8
            map[ArmorItem.Type.HELMET] = 3
        }),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_IRON,
        3.0f,
        0.1f,
        Supplier {
            Ingredient.ofItems(ModAssets.getItem("electrum_ingot"))
        }
    ),
    LEAD(
        "lead",
        37,
        Util.make(EnumMap(ArmorItem.Type::class.java), Consumer { map ->
            map[ArmorItem.Type.BOOTS] = 3
            map[ArmorItem.Type.LEGGINGS] = 6
            map[ArmorItem.Type.CHESTPLATE] = 8
            map[ArmorItem.Type.HELMET] = 3
        }),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
        3.0f,
        0.1f,
        Supplier {
            Ingredient.ofItems(ModAssets.getItem("lead_ingot"))
        }
    ),
    MYTHRIL(
        "mythril",
        37,
        Util.make(EnumMap(ArmorItem.Type::class.java), Consumer { map ->
            map[ArmorItem.Type.BOOTS] = 3
            map[ArmorItem.Type.LEGGINGS] = 6
            map[ArmorItem.Type.CHESTPLATE] = 8
            map[ArmorItem.Type.HELMET] = 3
        }),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
        3.0f,
        0.1f,
        Supplier {
            Ingredient.ofItems(ModAssets.getItem("mythril_ingot"))
        }
    ),
    NIHILITE(
        "nihilite",
        37,
        Util.make(EnumMap(ArmorItem.Type::class.java), Consumer { map ->
            map[ArmorItem.Type.BOOTS] = 3
            map[ArmorItem.Type.LEGGINGS] = 6
            map[ArmorItem.Type.CHESTPLATE] = 8
            map[ArmorItem.Type.HELMET] = 3
        }),
        15,
        SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
        3.0f,
        0.1f,
        Supplier {
            Ingredient.ofItems(ModAssets.getItem("nihilite_ingot"))
        }
    );

    private val repairIngredient: Lazy<Ingredient> = Lazy<Ingredient>(repairIngredient)
    private val baseDurability: EnumMap<ArmorItem.Type?, Int?> =
        Util.make(EnumMap(ArmorItem.Type::class.java)) { map ->
            map[ArmorItem.Type.BOOTS] = 13
            map[ArmorItem.Type.LEGGINGS] = 15
            map[ArmorItem.Type.CHESTPLATE] = 16
            map[ArmorItem.Type.HELMET] = 11
        }

    override fun getDurability(type: ArmorItem.Type): Int = baseDurability[type]!! * durabilityMultiplier
    override fun getProtection(type: ArmorItem.Type): Int = protectionAmounts[type]!!
    override fun getEnchantability(): Int = enchantability
    override fun getEquipSound(): SoundEvent = equipSound
    override fun getRepairIngredient(): Ingredient = repairIngredient.get()
    override fun getName(): String = armorName
    override fun getToughness(): Float = toughness
    override fun getKnockbackResistance(): Float = knockbackResistance
}