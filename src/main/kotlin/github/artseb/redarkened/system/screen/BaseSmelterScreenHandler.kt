package github.artseb.redarkened.system.screen

import github.artseb.redarkened.system.blockentity.BaseSmelterBlockEntity
import net.minecraft.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot

class BaseSmelterScreenHandler(
    type: ScreenHandlerType<*>,
    syncId: Int,
    playerInventory: PlayerInventory,
    private val inventory: Inventory,
    private val propertyDelegate: PropertyDelegate
) : ScreenHandler(type, syncId) {

    // Client-side constructor — called by the registered factory
    constructor(type: ScreenHandlerType<*>, syncId: Int, playerInventory: PlayerInventory)
            : this(type, syncId, playerInventory, SimpleInventory(3), ArrayPropertyDelegate(4))

    init {
        checkSize(inventory, 3)
        checkDataCount(propertyDelegate, 4)
        inventory.onOpen(playerInventory.player)

        // Input slot
        addSlot(Slot(inventory, BaseSmelterBlockEntity.INPUT_SLOT, 56, 17))

        // Fuel slot — only accepts valid furnace fuels
        addSlot(object : Slot(inventory, BaseSmelterBlockEntity.FUEL_SLOT, 56, 53) {
            override fun canInsert(stack: ItemStack) =
                AbstractFurnaceBlockEntity.createFuelTimeMap().containsKey(stack.item)
        })

        // Output slot — no insertion
        addSlot(object : Slot(inventory, BaseSmelterBlockEntity.OUTPUT_SLOT, 116, 35) {
            override fun canInsert(stack: ItemStack) = false
        })

        // Player inventory (rows 0–2)
        for (row in 0..2)
            for (col in 0..8)
                addSlot(Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18))

        // Hotbar
        for (col in 0..8)
            addSlot(Slot(playerInventory, col, 8 + col * 18, 142))

        addProperties(propertyDelegate)
    }

    // Property accessors for the screen
    val burnTime:      Int   get() = propertyDelegate[0]
    val fuelTime:      Int   get() = propertyDelegate[1]
    val cookTime:      Int   get() = propertyDelegate[2]
    val cookTimeTotal: Int   get() = propertyDelegate[3]

    fun isBurning()        = burnTime > 0
    fun getCookProgress()  = if (cookTimeTotal <= 0) 0f else cookTime.toFloat() / cookTimeTotal
    fun getFuelProgress()  = if (fuelTime     <= 0) 0f else burnTime.toFloat() / fuelTime

    override fun canUse(player: PlayerEntity) = inventory.canPlayerUse(player)

    override fun quickMove(player: PlayerEntity, slotIndex: Int): ItemStack {
        val slot = slots.getOrNull(slotIndex) ?: return ItemStack.EMPTY
        if (!slot.hasStack()) return ItemStack.EMPTY
        val stack = slot.stack
        val copy  = stack.copy()

        when {
            // Output → player inventory, reversed (hotbar first)
            slotIndex == BaseSmelterBlockEntity.OUTPUT_SLOT -> {
                if (!insertItem(stack, 3, 39, true)) return ItemStack.EMPTY
                slot.onQuickTransfer(stack, copy)
            }
            // Smelter input/fuel → player inventory
            slotIndex < 3 -> {
                if (!insertItem(stack, 3, 39, false)) return ItemStack.EMPTY
            }
            // Player inventory → try input, then fuel; otherwise move between inv and hotbar
            else -> {
                if (!insertItem(stack, BaseSmelterBlockEntity.INPUT_SLOT, 1, false) &&
                    !insertItem(stack, BaseSmelterBlockEntity.FUEL_SLOT,  2, false)) {
                    if (slotIndex < 30) { if (!insertItem(stack, 30, 39, false)) return ItemStack.EMPTY }
                    else                { if (!insertItem(stack, 3,  30, false)) return ItemStack.EMPTY }
                }
            }
        }

        if (stack.isEmpty) slot.stack = ItemStack.EMPTY else slot.markDirty()
        if (stack.count == copy.count) return ItemStack.EMPTY
        slot.onTakeItem(player, stack)
        return copy
    }
}