package github.artseb.redarkened.content.particles

import github.artseb.artlib.content.RegistrableAsset
import github.artseb.artlib.registry.Register
import github.artseb.redarkened.Redarkened
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.ParticleType
import net.minecraft.particle.DefaultParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

@Register
class NihiliteAshParticleType : RegistrableAsset<ParticleType<*>>(
    Identifier(Redarkened.MOD_ID, "nihilite_ash"),
    @Suppress("UNCHECKED_CAST")
    (Registries.PARTICLE_TYPE as Registry<in ParticleType<*>>)
) {
    companion object {
        lateinit var TYPE: DefaultParticleType
            private set
    }

    override fun createInstance(): ParticleType<*> =
        FabricParticleTypes.simple().also { TYPE = it }
}