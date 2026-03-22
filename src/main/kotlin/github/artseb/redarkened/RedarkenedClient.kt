package github.artseb.redarkened

import github.artseb.redarkened.content.particles.NihiliteAshParticleType
import github.artseb.redarkened.content.screens.SmelterHandlerType
import github.artseb.redarkened.system.item.HammerItem
import github.artseb.redarkened.system.particle.NihiliteAshParticle
import github.artseb.redarkened.system.screen.BaseSmelterScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback
import net.minecraft.client.gui.screen.ingame.HandledScreens

object RedarkenedClient : ClientModInitializer {
	override fun onInitializeClient() {
		HandledScreens.register(SmelterHandlerType.TYPE, ::BaseSmelterScreen)

		ParticleFactoryRegistry.getInstance()
			.register(NihiliteAshParticleType.TYPE, NihiliteAshParticle::Factory)

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