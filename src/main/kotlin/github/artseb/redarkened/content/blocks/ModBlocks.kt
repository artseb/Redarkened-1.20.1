package github.artseb.redarkened.content.blocks

import github.artseb.artlib.registry.Register
import github.artseb.artlib.registry.Registrable
import github.artseb.artlib.system.block.BlockModel
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.system.block.BlockCreator
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks.createLightLevelFromLitBlockState
import net.minecraft.block.FurnaceBlock
import net.minecraft.block.MapColor
import net.minecraft.block.enums.Instrument
import net.minecraft.data.client.TexturedModel
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

@Register
class ModBlocks : Registrable {
    val blocks = arrayOf(
        BlockCreator(
            Identifier(Redarkened.MOD_ID, "corrupted_stone"),
            Block(FabricBlockSettings.create()
                .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.DEEPSLATE)),
            BlockModel.CubeAll
        ),
        BlockCreator(
            Identifier(Redarkened.MOD_ID, "scratched_corrupted_stone"),
            Block(FabricBlockSettings.create()
                .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.DEEPSLATE)),
            BlockModel.CubeAll
        ),
        BlockCreator(
            Identifier(Redarkened.MOD_ID, "vein_corrupted_stone"),
            Block(FabricBlockSettings.create()
                .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.DEEPSLATE)),
            BlockModel.CubeAll
        ),
        BlockCreator(
            Identifier(Redarkened.MOD_ID, "corrupted_bricks"),
            Block(FabricBlockSettings.create()
                .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.DEEPSLATE)),
            BlockModel.CubeAll
        ),
        BlockCreator(
            Identifier(Redarkened.MOD_ID, "scratched_corrupted_bricks"),
            Block(FabricBlockSettings.create()
                .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.DEEPSLATE)),
            BlockModel.CubeAll
        ),
        BlockCreator(
            Identifier(Redarkened.MOD_ID, "vein_corrupted_bricks"),
            Block(FabricBlockSettings.create()
                .requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.DEEPSLATE)),
            BlockModel.CubeAll
        )
    )

    override fun register() =  blocks.forEach {it.register()}
    override fun initialize() = blocks.forEach {it.initialize()}
    override fun postRegistration() = blocks.forEach {it.postRegistration()}
}