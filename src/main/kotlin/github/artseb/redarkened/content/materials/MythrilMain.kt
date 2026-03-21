package github.artseb.redarkened.content.materials

import github.artseb.artlib.content.items.basic.BasicArmorSet
import github.artseb.artlib.content.items.basic.BasicMaterial
import github.artseb.artlib.content.items.basic.BasicToolSet
import github.artseb.artlib.registry.Register
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.system.item.ArmorMaterials
import github.artseb.redarkened.system.item.ToolMaterials
import net.minecraft.util.Identifier

@Register
class MythrilMain {
    val materials = BasicMaterial(Identifier(Redarkened.MOD_ID, "mythril"), ToolMaterials.MYTHRIL)
    val basicToolSet = BasicToolSet(Identifier(Redarkened.MOD_ID, "mythril"), ToolMaterials.MYTHRIL)
    val basicArmorSet = BasicArmorSet(Identifier(Redarkened.MOD_ID, "mythril"), ArmorMaterials.MYTHRIL)
    // ...

    fun register() {
        materials.register()
        basicToolSet.register()
        basicArmorSet.register()
        // ...
    }
    fun initialize() {
        materials.initialize()
        basicToolSet.initialize()
        basicArmorSet.initialize()
        // ...
    }
    fun postRegistration() {
        materials.postRegistration()
        basicToolSet.postRegistration()
        basicArmorSet.postRegistration()
        // ...
    }
}