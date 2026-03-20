package github.artseb.redarkened.content.classes

import github.artseb.artlib.registry.Registrable
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.system.classes.ClassType
import github.artseb.redarkened.system.classes.CoreClass
import github.artseb.redarkened.util.ModClasses
import net.minecraft.util.Identifier

abstract class RegistrableClass(
    val name: String,
    val classType: ClassType,
    val parentId: Identifier? = null,
): Registrable {
    lateinit var instance: CoreClass

    abstract fun createInstance(): CoreClass

    override fun register() {
        if (Redarkened.DEBUG_MODE) {
            Redarkened.LOGGER.info("Registering class: $name")
        }
        instance = createInstance()

        ModClasses.register(Identifier(Redarkened.MOD_ID, "${classType.name.lowercase()}/$name"), this)
    }

    override fun initialize() {
        if (Redarkened.DEBUG_MODE) {
            Redarkened.LOGGER.info("Initializing class: $name")
        }
    }

    override fun postRegistration() {
        if (Redarkened.DEBUG_MODE) {
            Redarkened.LOGGER.info("Post register triggering class: $name")
        }
    }
}