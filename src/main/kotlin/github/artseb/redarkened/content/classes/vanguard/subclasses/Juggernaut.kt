package github.artseb.redarkened.content.classes.vanguard.subclasses

import github.artseb.artlib.registry.Register
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.content.classes.RegistrableClass
import github.artseb.redarkened.system.ability.Ability
import github.artseb.redarkened.system.ability.AbilityType
import github.artseb.redarkened.system.classes.ClassType
import github.artseb.redarkened.system.classes.CoreClass
import net.minecraft.util.Identifier

@Register
class Juggernaut: RegistrableClass(
    "juggernaut",
    ClassType.SUB,
    Identifier(Redarkened.MOD_ID, "core/vanguard")
) {
    override fun createInstance(): CoreClass {
        return CoreClass(
            healthModifier = 0.25f,
            knockbackResistanceModifier = 0.15f,
            movementSpeedModifier = -0.1, // additional slow
            abilities = listOf(
                Ability(
                    "unstoppable_charge",
                    20 * 45,
                    AbilityType.SUBCLASS
                )
            )
        )
    }
}