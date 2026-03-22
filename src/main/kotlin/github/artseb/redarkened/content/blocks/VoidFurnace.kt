package github.artseb.redarkened.content.blocks

import github.artseb.artlib.content.Recipe
import github.artseb.artlib.registry.Register
import github.artseb.artlib.system.block.BlockModel
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.content.blockentities.VoidFurnaceBlockEntity
import github.artseb.redarkened.content.particles.NihiliteAshParticleType
import github.artseb.redarkened.content.sounds.VoidFurnaceSounds
import github.artseb.redarkened.system.block.BaseSmelterBlock
import github.artseb.redarkened.system.blockentity.BaseSmelterBlockEntity
import github.artseb.redarkened.util.ModAssets
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.BlockState
import net.minecraft.block.MapColor
import net.minecraft.block.enums.Instrument
import net.minecraft.data.client.TexturedModel
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

@Register(after = [VoidFurnaceSounds::class, NihiliteAshParticleType::class])
class VoidFurnace : BaseSmelterBlock(
    Identifier(Redarkened.MOD_ID, "void_furnace"),
    FabricBlockSettings.create()
        .mapColor(MapColor.STONE_GRAY)
        .instrument(Instrument.BASEDRUM)
        .requiresTool()
        .strength(3.5F),
    BlockModel.Custom { generator, block ->
        generator.registerSingleton(block, TexturedModel.ORIENTABLE)
    }
) {
    override fun getAmbientParticle() = NihiliteAshParticleType.TYPE
    override fun getAmbientSound()    = VoidFurnaceSounds.AMBIENT

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BaseSmelterBlockEntity =
        VoidFurnaceBlockEntity(blockEntityType, pos, state)

    override fun initialize() {
        super.initialize()
        // Adjust ingredient to whatever your crafting recipe for this block should be
        recipes.add(Recipe.Shaped(
            listOf("###", "# #", "###"),
            mapOf('#' to Ingredient.ofItems(ModAssets.getItem("corrupted_stone"))),
            ItemStack(instance, 1),
            "craft"
        ))
    }
}