package github.artseb.redarkened.content.blocks

import github.artseb.artlib.content.Recipe
import github.artseb.artlib.content.RegistrableAsset
import github.artseb.artlib.registry.Register
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.util.ModAssets
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

@Register
class ElectrumBlock: RegistrableAsset<Block>(Identifier(Redarkened.MOD_ID, "electrum_block"), Registries.BLOCK) {
    private lateinit var itemAsset: ElectrumBlockItem

    inner class ElectrumBlockItem : RegistrableAsset<BlockItem>(
        id,
        Registries.ITEM,
        null,
        "rpg_blocks"
    ) {
        override fun createInstance(): BlockItem {
            return BlockItem(this@ElectrumBlock.instance, Item.Settings())
        }
    }

    override fun createInstance(): Block {
        return Block(
            FabricBlockSettings.create()
                .mapColor(MapColor.ORANGE)
                .requiresTool()
                .strength(50.0F, 1200.0F)
                .sounds(BlockSoundGroup.METAL)
        )
    }

    override fun register() {
        super.register()

        itemAsset = ElectrumBlockItem()
        itemAsset.register()
    }

    override fun initialize() {
        super.initialize()

        itemAsset.initialize()

        recipes.add(
            Recipe.Shaped(
                listOf(
                    "###",
                    "###",
                    "###"
                ),
                mapOf(
                    '#' to Ingredient.ofItems(ModAssets.getItem("electrum_ingot")),
                ),
                ItemStack(instance, 1),
                "pack"
            ))
    }

    override fun postRegistration() {
        super.postRegistration()

        itemAsset.postRegistration()
    }
}