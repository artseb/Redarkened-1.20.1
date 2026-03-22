package github.artseb.redarkened.content.sounds

import github.artseb.artlib.content.RegistrableAsset
import github.artseb.artlib.registry.Register
import github.artseb.redarkened.Redarkened
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

@Register
class VoidFurnaceSounds : RegistrableAsset<SoundEvent>(
    Identifier(Redarkened.MOD_ID, "block.void_furnace.ambient"),
    Registries.SOUND_EVENT
) {
    companion object {
        lateinit var AMBIENT: SoundEvent
            private set
    }

    override fun createInstance(): SoundEvent =
        SoundEvent.of(id).also { AMBIENT = it }
}