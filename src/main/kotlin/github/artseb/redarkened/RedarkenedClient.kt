package github.artseb.redarkened

import github.artseb.redarkened.system.item.HammerItem
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback

object RedarkenedClient : ClientModInitializer {
	override fun onInitializeClient() {
		ClientPreAttackCallback.EVENT.register { _, player, _ ->
			val mainHand = player.mainHandStack
			if (mainHand.item is HammerItem) {
				val progress = player.getAttackCooldownProgress(0.5f)
				if (progress < 1.0f) {
					return@register true
				}
			}
			false
		}
	}
}