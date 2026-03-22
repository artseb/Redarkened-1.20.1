package github.artseb.redarkened.system.blockentity

import github.artseb.redarkened.content.screens.SmelterHandlerType
import github.artseb.redarkened.system.screen.BaseSmelterScreenHandler
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class BaseSmelterBlockEntity(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState
) : BlockEntity(type, pos, state), Inventory, NamedScreenHandlerFactory {

    private val items: DefaultedList<ItemStack> = DefaultedList.ofSize(SLOT_COUNT, ItemStack.EMPTY)

    var burnTime: Int = 0
    var fuelTime:  Int = 0
    var cookTime:  Int = 0

    val propertyDelegate: PropertyDelegate = object : PropertyDelegate {
        override fun get(index: Int) = when (index) {
            0 -> burnTime
            1 -> fuelTime
            2 -> cookTime
            3 -> getCookTimeTotal()
            else -> 0
        }
        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> burnTime = value
                1 -> fuelTime = value
                2 -> cookTime = value
                // 3 is read-only (derived from getCookTimeTotal)
            }
        }
        override fun size() = 4
    }

    // ── Contract ─────────────────────────────────────────────────────────────

    abstract fun canAcceptInput(stack: ItemStack): Boolean
    abstract fun getResult(input: ItemStack): ItemStack
    abstract override fun getDisplayName(): Text

    open fun getCookTimeTotal(): Int = 200

    // ── NamedScreenHandlerFactory ─────────────────────────────────────────────

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler =
        BaseSmelterScreenHandler(SmelterHandlerType.TYPE, syncId, inv, this, propertyDelegate)

    // ── Inventory ────────────────────────────────────────────────────────────

    override fun size() = SLOT_COUNT
    override fun isEmpty() = items.all { it.isEmpty }
    override fun getStack(slot: Int): ItemStack = items[slot]
    override fun removeStack(slot: Int, amount: Int): ItemStack = Inventories.splitStack(items, slot, amount)
    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(items, slot)
    override fun setStack(slot: Int, stack: ItemStack) {
        items[slot] = stack
        if (stack.count > maxCountPerStack) stack.count = maxCountPerStack
        markDirty()
    }
    override fun canPlayerUse(player: PlayerEntity) =
        world?.getBlockEntity(pos) == this &&
                player.squaredDistanceTo(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5) <= 64.0
    override fun clear() = items.clear()

    // ── NBT ──────────────────────────────────────────────────────────────────

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        Inventories.readNbt(nbt, items)
        burnTime = nbt.getShort("BurnTime").toInt()
        fuelTime  = nbt.getShort("FuelTime").toInt()
        cookTime  = nbt.getShort("CookTime").toInt()
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        Inventories.writeNbt(nbt, items)
        nbt.putShort("BurnTime", burnTime.toShort())
        nbt.putShort("FuelTime",  fuelTime.toShort())
        nbt.putShort("CookTime",  cookTime.toShort())
    }

    // ── Smelting tick ─────────────────────────────────────────────────────────

    fun isBurning() = burnTime > 0

    private fun canSmelt(): Boolean {
        val input = items[INPUT_SLOT]
        if (input.isEmpty || !canAcceptInput(input)) return false
        val result = getResult(input)
        if (result.isEmpty) return false
        val output = items[OUTPUT_SLOT]
        return output.isEmpty ||
                (output.isOf(result.item) && output.count + result.count <= output.maxCount)
    }

    private fun smelt() {
        val result = getResult(items[INPUT_SLOT])
        val output = items[OUTPUT_SLOT]
        if (output.isEmpty) items[OUTPUT_SLOT] = result.copy() else output.increment(result.count)
        items[INPUT_SLOT].decrement(1)
    }

    fun tick(world: World, pos: BlockPos, state: BlockState) {
        if (world.isClient) return
        val wasBurning = isBurning()

        if (isBurning()) burnTime--

        val fuel = items[FUEL_SLOT]
        if (!isBurning() && !fuel.isEmpty && canSmelt()) {
            fuelTime = getFuelTime(fuel)
            burnTime = fuelTime
            if (isBurning()) {
                val remainder = fuel.item.recipeRemainder
                if (remainder != null) items[FUEL_SLOT] = ItemStack(remainder)
                else fuel.decrement(1)
            }
        }

        if (isBurning() && canSmelt()) {
            cookTime++
            if (cookTime >= getCookTimeTotal()) {
                cookTime = 0
                smelt()
            }
        } else if (cookTime > 0) {
            cookTime = (cookTime - 2).coerceAtLeast(0)
        }

        if (wasBurning != isBurning()) {
            val currentState = world.getBlockState(pos)
            if (currentState.contains(Properties.LIT)) {
                world.setBlockState(pos, currentState.with(Properties.LIT, isBurning()), Block.NOTIFY_ALL)
            }
            markDirty()
        }
    }

    private fun getFuelTime(stack: ItemStack): Int =
        if (stack.isEmpty) 0
        else AbstractFurnaceBlockEntity.createFuelTimeMap()[stack.item] ?: 0

    companion object {
        const val INPUT_SLOT  = 0
        const val FUEL_SLOT   = 1
        const val OUTPUT_SLOT = 2
        const val SLOT_COUNT  = 3
    }
}