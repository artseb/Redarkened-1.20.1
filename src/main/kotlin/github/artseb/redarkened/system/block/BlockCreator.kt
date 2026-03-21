package github.artseb.redarkened.system.block

import github.artseb.artlib.content.RegistrableAsset
import github.artseb.artlib.system.block.BlockModel
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

open class BlockCreator(id: Identifier, val block: Block, blockModel: BlockModel, tags: Array<String>? = null
) : RegistrableAsset<Block>(id, Registries.BLOCK, tags) {
    private lateinit var itemAsset: ModBlockItem

    inner class ModBlockItem : RegistrableAsset<BlockItem>(id, Registries.ITEM, null, "rpg_blocks") {
        override fun createInstance(): BlockItem = BlockItem(this@BlockCreator.instance, Item.Settings())
    }
    init { model = blockModel }
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