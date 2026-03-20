package github.artseb.redarkened.content.classes.vanguard

import github.artseb.artlib.registry.Register
import github.artseb.redarkened.content.classes.RegistrableClass
import github.artseb.redarkened.system.ability.Ability
import github.artseb.redarkened.system.ability.AbilityType
import github.artseb.redarkened.system.classes.ClassType
import github.artseb.redarkened.system.classes.CoreClass
import github.artseb.redarkened.system.item.ProficiencyLevel
import github.artseb.redarkened.system.item.WeaponTier

@Register
class Vanguard: RegistrableClass(
    "vanguard",
    ClassType.CORE
) {
    override fun createInstance(): CoreClass {
        return CoreClass(
            0.3f,
            0.2f,
            0f,
            -0.1,
            -0.05f,
            0.1f,
            mapOf(
                WeaponTier.HEAVY to ProficiencyLevel.ADEPT,
                WeaponTier.MEDIUM to ProficiencyLevel.ADEPT,
                WeaponTier.LIGHT to ProficiencyLevel.LIMITED
            ),
            listOf(
                Ability(
                    "brace",
                    20 * 20,
                    AbilityType.DEFENSIVE
                ),
                Ability(
                    "indomitable_will",
                    20 * 35,
                    AbilityType.CORE
                ),
                Ability(
                    "groundbreaker",
                    20 * 25,
                    AbilityType.CORE
                )
            )
        )
    }

}