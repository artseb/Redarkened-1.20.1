package github.artseb.redarkened.content.blocks

import github.artseb.artlib.registry.Register
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
import net.minecraft.util.Identifier

@Register
class VoidFurnace: BlockCreator(
    Identifier(Redarkened.MOD_ID, "void_furnace"),
    {
        object : FurnaceBlock(FabricBlockSettings.create()
            .mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).requiresTool()
            .strength(3.5F).luminance(createLightLevelFromLitBlockState(13)))
        {}
    } as Block,
    BlockModel.Custom { generator, block ->
        generator.registerSingleton(block, TexturedModel.ORIENTABLE)
    },
)