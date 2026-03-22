package github.artseb.redarkened.system.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType

@Environment(EnvType.CLIENT)
class NihiliteAshParticle(
    world: ClientWorld,
    x: Double, y: Double, z: Double,
    velX: Double, velY: Double, velZ: Double
) : SpriteBillboardParticle(world, x, y, z, velX, velY, velZ) {

    private var stutterTimer = random.nextInt(8) + 4

    init {
        // Near-black with deep crimson tint, randomized slightly per particle
        red   = 0.25f + random.nextFloat() * 0.2f
        green = 0.0f
        blue  = 0.0f
        alpha = 0.75f + random.nextFloat() * 0.2f
        scale = 0.04f + random.nextFloat() * 0.07f
        maxAge = 35 + random.nextInt(25)
        velocityX = (random.nextDouble() - 0.5) * 0.04
        velocityY = 0.035 + random.nextDouble() * 0.025
        velocityZ = (random.nextDouble() - 0.5) * 0.04
        collidesWithWorld = true
    }

    override fun tick() {
        prevPosX = x; prevPosY = y; prevPosZ = z

        // Stutter — random direction kick mid-flight, the "wrong" drift
        stutterTimer--
        if (stutterTimer <= 0) {
            velocityX += (random.nextDouble() - 0.5) * 0.06
            velocityZ += (random.nextDouble() - 0.5) * 0.06
            // Occasional crimson flash — briefly spike red channel
            if (random.nextInt(5) == 0) red = (red + 0.4f).coerceAtMost(1.0f)
            stutterTimer = random.nextInt(7) + 3
        } else {
            // Bleed red back down toward base
            red = (red - 0.03f).coerceAtLeast(0.2f)
        }

        // Fade out in second half of life
        if (age > maxAge * 0.5f) {
            alpha = ((maxAge - age).toFloat() / (maxAge * 0.5f)).coerceIn(0f, 1f)
        }

        move(velocityX, velocityY, velocityZ)
        velocityX *= 0.96
        velocityY *= 0.98
        velocityZ *= 0.96

        age++
        if (age >= maxAge) markDead()
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    @Environment(EnvType.CLIENT)
    class Factory(private val sprites: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            type: DefaultParticleType, world: ClientWorld,
            x: Double, y: Double, z: Double,
            velX: Double, velY: Double, velZ: Double
        ) = NihiliteAshParticle(world, x, y, z, velX, velY, velZ).also { it.setSprite(sprites) }
    }
}