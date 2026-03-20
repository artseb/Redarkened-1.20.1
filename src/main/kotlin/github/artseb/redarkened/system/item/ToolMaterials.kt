package github.artseb.redarkened.system.item

import github.artseb.artlib.system.item.ToolMaterial
import github.artseb.redarkened.util.ModAssets
import net.fabricmc.yarn.constants.MiningLevels
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Lazy
import java.util.function.Supplier

enum class ToolMaterials(
    private val materialName: String,
    private val miningLevel: Int,
    private val itemDurability: Int,
    private val miningSpeed: Float,
    private val attackDamage: Float,
    private val enchantability: Int,
    repairIngredient: Supplier<Ingredient>
) : ToolMaterial {
    ELECTRUM(
        "electrum",
        MiningLevels.NETHERITE,
        3020,
        10.0f,
        5.0f,
        20,
        Supplier {
            Ingredient.ofItems(ModAssets.getItem("electrum_ingot"))
        }
    ),
    LEAD(
        "lead",
        MiningLevels.NETHERITE,
        3260,
        10.0f,
        5.0f,
        18,
        Supplier {
            Ingredient.ofItems(ModAssets.getItem("lead_ingot"))
        }
    );

    private val repairIngredient: Lazy<Ingredient> = Lazy<Ingredient>(repairIngredient)

    override fun getMaterialName(): String = materialName
    override fun getDurability(): Int = itemDurability
    override fun getMiningSpeedMultiplier(): Float = miningSpeed
    override fun getAttackDamage(): Float = attackDamage
    override fun getMiningLevel(): Int = miningLevel
    override fun getEnchantability(): Int = enchantability
    override fun getRepairIngredient(): Ingredient = repairIngredient.get()
}