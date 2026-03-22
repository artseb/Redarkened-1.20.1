package github.artseb.redarkened.content.screens

import github.artseb.artlib.content.RegistrableAsset
import github.artseb.artlib.registry.Register
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.system.screen.BaseSmelterScreenHandler
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier

@Register
class SmelterHandlerType : RegistrableAsset<ScreenHandlerType<*>>(
    Identifier(Redarkened.MOD_ID, "smelter"),
    @Suppress("UNCHECKED_CAST")
    (Registries.SCREEN_HANDLER as Registry<in ScreenHandlerType<*>>)
) {
    companion object {
        lateinit var TYPE: ScreenHandlerType<BaseSmelterScreenHandler>
            private set
    }

    override fun createInstance(): ScreenHandlerType<*> =
        ScreenHandlerType(
            { syncId, inv -> BaseSmelterScreenHandler(TYPE, syncId, inv) },
            FeatureFlags.VANILLA_FEATURES
        ).also {
            @Suppress("UNCHECKED_CAST")
            TYPE = it
        }
}