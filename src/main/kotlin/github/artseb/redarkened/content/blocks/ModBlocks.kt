package github.artseb.redarkened.content.blocks

import github.artseb.artlib.content.Recipe
import github.artseb.artlib.content.RegistrableAsset
import github.artseb.artlib.content.RegistrableBlock
import github.artseb.artlib.registry.Register
import github.artseb.artlib.registry.Registrable
import github.artseb.artlib.system.block.BlockModel
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.util.ModAssets
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

@Register
class ModBlocks : Registrable {
    val blocks = arrayOf(
        object : SimpleBlock(
            Identifier(Redarkened.MOD_ID, "electrum_block"),
            Block(FabricBlockSettings.create()
                .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.METAL)),
            BlockModel.CubeAll
        ) {
            override fun initialize() {
                super.initialize()
                recipes.add(Recipe.Shaped(
                    listOf("###", "###", "###"),
                    mapOf('#' to Ingredient.ofItems(ModAssets.getItem("electrum_ingot")),),
                    ItemStack(instance, 1),
                    "pack"
                ))
            }
        },
        object : SimpleBlock(
            Identifier(Redarkened.MOD_ID, "lead_block"),
            Block(FabricBlockSettings.create()
                .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.METAL)),
            BlockModel.CubeAll
        ) {
            override fun initialize() {
                super.initialize()
                recipes.add(Recipe.Shaped(
                    listOf("###", "###", "###"),
                    mapOf('#' to Ingredient.ofItems(ModAssets.getItem("lead_ingot")),),
                    ItemStack(instance, 1),
                    "pack"
                ))
            }
        },
        SimpleBlock(
            Identifier(Redarkened.MOD_ID, "mythril_block"),
            Block(FabricBlockSettings.create()
                .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.METAL)),
            BlockModel.CubeAll
        ),
        SimpleBlock(
            Identifier(Redarkened.MOD_ID, "corrupted_stone"),
            Block(FabricBlockSettings.create()
                .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.DEEPSLATE)),
            BlockModel.CubeAll
        )
    )

    open inner class SimpleBlock(id: Identifier, val block: Block, blockModel: BlockModel, tags: Array<String>? = null, group: String? = null
    ) : RegistrableBlock(id, blockModel, tags, group) {
        private lateinit var itemAsset: ModBlockItem

        inner class ModBlockItem : RegistrableAsset<BlockItem>(id, Registries.ITEM, null, "rpg_blocks") {
            override fun createInstance(): BlockItem = BlockItem(this@SimpleBlock.instance, Item.Settings())
        }

        override fun createInstance(): Block = block

        override fun register() {
            super.register()
            itemAsset = ModBlockItem()
            itemAsset.register()
        }

        override fun initialize() {
            super.initialize()
            itemAsset.initialize()
        }

        override fun postRegistration() {
            super.postRegistration()
            itemAsset.postRegistration()
        }
    }

    override fun register() =  blocks.forEach {it.register()}
    override fun initialize() = blocks.forEach {it.initialize()}
    override fun postRegistration() = blocks.forEach {it.postRegistration()}
}