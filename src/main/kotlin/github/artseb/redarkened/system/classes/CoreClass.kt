package github.artseb.redarkened.system.classes

import github.artseb.redarkened.system.ability.Ability
import github.artseb.redarkened.system.item.ProficiencyLevel
import github.artseb.redarkened.system.item.WeaponTier

data class CoreClass(
    val healthModifier: Float = 0f,
    val defenseModifier: Float = 0f,
    val damageModifier: Float = 0f,
    val movementSpeedModifier: Double = 0.0,
    val attackSpeedModifier: Float = 0f,
    val knockbackResistanceModifier: Float = 0f,
    val weaponProficiencies: Map<WeaponTier, ProficiencyLevel> = emptyMap(),
    val abilities: List<Ability> = emptyList()
)