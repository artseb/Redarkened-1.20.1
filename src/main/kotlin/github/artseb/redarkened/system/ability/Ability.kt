package github.artseb.redarkened.system.ability

import github.artseb.redarkened.Redarkened
import net.minecraft.util.Identifier

data class Ability(
    val name: String,
    val cooldownTicks: Int,
    val type: AbilityType
) {
    val id: Identifier = Identifier(Redarkened.MOD_ID, "ability/$name")
}