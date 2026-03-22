package github.artseb.redarkened.content.blockentities

import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.system.blockentity.BaseSmelterBlockEntity
import github.artseb.redarkened.util.ModAssets
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class VoidFurnaceBlockEntity(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState
) : BaseSmelterBlockEntity(type, pos, state) {
    override fun getDisplayName(): Text =
        Text.translatable("container.${Redarkened.MOD_ID}.nihilite_smelter")

    override fun canAcceptInput(stack: ItemStack): Boolean =
        stack.isOf(ModAssets.getItem("raw_nihilite"))

    override fun getResult(input: ItemStack): ItemStack =
        ItemStack(ModAssets.getItem("nihilite_ingot"), 1)
}