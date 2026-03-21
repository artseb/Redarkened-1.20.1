package github.artseb.redarkened.content.blocks

import github.artseb.artlib.content.Recipe
import github.artseb.artlib.registry.Register
import github.artseb.artlib.system.block.BlockModel
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.system.block.BlockCreator
import github.artseb.redarkened.util.ModAssets
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

@Register
class LeadBlock: BlockCreator(
    Identifier(Redarkened.MOD_ID, "lead_block"),
    Block(FabricBlockSettings.create()
        .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.METAL)),
    BlockModel.CubeAll
) {
    override fun initialize() {
        super.initialize()
        recipes.add(Recipe.Shaped(
            listOf("###", "###", "###"),
            mapOf('#' to Ingredient.ofItems(ModAssets.getItem("lead_ingot"))),
            ItemStack(instance, 1),
            "pack"
        ))
    }
}