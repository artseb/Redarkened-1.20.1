package github.artseb.redarkened.util

import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.content.classes.RegistrableClass
import net.minecraft.util.Identifier

object ModClasses {
    val CLASSES = mutableMapOf<Identifier, RegistrableClass>()

    fun register(id: Identifier, gameClass: RegistrableClass) {
        CLASSES[id] = gameClass

        if (Redarkened.DEBUG_MODE) {
            Redarkened.LOGGER.info("Added class: $id")
        }
    }
}