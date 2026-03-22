package github.artseb.redarkened

import github.artseb.artlib.ArtLib
import github.artseb.artlib.registry.FileDiscovery
import github.artseb.redarkened.command.ClassCommand
import github.artseb.redarkened.system.classes.ClassApplier
import github.artseb.redarkened.system.classes.PlayerClassStorage
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import software.bernie.geckolib.GeckoLib
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible

object Redarkened : ModInitializer {
	const val MOD_ID = "redarkened"
	const val DEBUG_MODE = true
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	private fun runFunctionInInstance(instance: Any, funcName: String) {
		val funcFinder = instance::class.memberFunctions.find { it.name == funcName }
		if (funcFinder != null) {
			funcFinder.isAccessible = true
			val func = funcFinder.call(instance)
			if (func is Function0<*>) {
				func.invoke()
			}
		}
	}

	override fun onInitialize() {
		ArtLib.changeDebugMode(DEBUG_MODE)

		val classes = FileDiscovery.scan("github.artseb.redarkened.content")
		val registered = mutableListOf<Any>()

		classes.forEach { clazz ->
			try {
				val instance = clazz.kotlin.createInstance()
				registered.add(instance)
			} catch (e: Exception) {
				LOGGER.error("Error registering ${clazz.name}: ${e.message}")
			}
		}

		registered.forEach { runFunctionInInstance(it, "register") }
		registered.forEach { runFunctionInInstance(it, "initialize") }
		registered.forEach { runFunctionInInstance(it, "postRegistration") }

		CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
			ClassCommand.register(dispatcher)
		}

		ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
			val player = handler.player
			val classId = PlayerClassStorage.getClass(player)
			if (classId != null) {
				ClassApplier.applyClass(player, classId)
			}
		}

		ServerPlayerEvents.COPY_FROM.register { oldPlayer, newPlayer, _ ->
			val classId = PlayerClassStorage.getClass(oldPlayer)
			if (classId != null) {
				PlayerClassStorage.setClass(newPlayer, classId)
				ClassApplier.applyClass(newPlayer, classId)
			}
		}

		GeckoLib.initialize()

		LOGGER.info("Finished loading assets!")
	}
}