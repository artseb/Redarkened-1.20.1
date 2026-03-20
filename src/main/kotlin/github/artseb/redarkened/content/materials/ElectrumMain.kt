package github.artseb.redarkened.content.materials

import github.artseb.artlib.content.items.basic.BasicMaterial
import github.artseb.artlib.content.items.basic.BasicToolSet
import github.artseb.artlib.content.items.gecko.GeckoArmorSet
import github.artseb.artlib.registry.Register
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.system.item.ArmorMaterials
import github.artseb.redarkened.system.item.ToolMaterials
import net.minecraft.util.Identifier

@Register
class ElectrumMain {
    val materialId = Identifier(Redarkened.MOD_ID, "electrum")
    val materials = BasicMaterial(materialId, ToolMaterials.ELECTRUM)
    val basicToolSet = BasicToolSet(materialId, ToolMaterials.ELECTRUM)
    val basicArmorSet = GeckoArmorSet(
        materialId,
        "knight",
        mapOf(
            3 to "generic_knight_helmet"
        ),
        "geo/knight_armor",
        "textures/armor/electrum_knight_armor",
        "animations/knight_armor.animation.json",
        "idle",
        ArmorMaterials.ELECTRUM,
    )
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