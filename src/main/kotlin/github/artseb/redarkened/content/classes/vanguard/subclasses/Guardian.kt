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
class Guardian: RegistrableClass(
    "guardian",
    ClassType.SUB,
    Identifier(Redarkened.MOD_ID, "core/vanguard")
) {
    override fun createInstance(): CoreClass {
        return CoreClass(
            defenseModifier = 0.2f,
            healthModifier = 0.1f,
            abilities = listOf(
                Ability(
                    "shield_wall",
                    20 * 40,
                    AbilityType.SUBCLASS
                )
            )
        )
    }
}