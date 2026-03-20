package github.artseb.redarkened.content.items

import github.artseb.artlib.content.Recipe
import github.artseb.artlib.content.RegistrableAsset
import github.artseb.artlib.registry.Register
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.system.item.HammerItem
import github.artseb.redarkened.system.item.ToolMaterials
import github.artseb.redarkened.util.ModAssets
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

@Register
class ElectrumHammer: RegistrableAsset<HammerItem>(
    Identifier(Redarkened.MOD_ID, "electrum_hammer"),
    Registries.ITEM,
    null,
    "rpg_main"
) {
    override fun createInstance(): HammerItem {
        return HammerItem(ToolMaterials.ELECTRUM, 4, -2.8f, Item.Settings())
    }

    override fun initialize() {
        super.initialize()

//        AttackEntityCallback.EVENT.register { player, world, hand, entity, hitResult ->
//            return@register ActionResult.PASS
//        }

        recipes.add(
            Recipe.Shaped(
            listOf(
                "bib",
                " s ",
                " s "
            ),
            mapOf(
                'b' to Ingredient.ofItems(ModAssets.getItem("electrum_block")),
                'i' to Ingredient.ofItems(ModAssets.getItem("electrum_ingot")),
                's' to Ingredient.ofItems(Items.STICK)
            ),
            ItemStack(instance, 1),
            "tools"
        ))
    }
}