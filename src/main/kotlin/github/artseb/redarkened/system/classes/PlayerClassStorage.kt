package github.artseb.redarkened.system.classes

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object PlayerClassStorage {
    private const val CLASS_KEY = "RedarkenedClass"

    @JvmStatic
    fun setClass(player: ServerPlayerEntity, classId: Identifier) {
        val nbt = NbtCompound()
        player.writeCustomDataToNbt(nbt)
        nbt.putString(CLASS_KEY, classId.toString())
        player.readCustomDataFromNbt(nbt)
    }

    @JvmStatic
    fun getClass(player: PlayerEntity): Identifier? {
        val nbt = NbtCompound()
        player.writeCustomDataToNbt(nbt)
        return if (nbt.contains(CLASS_KEY)) Identifier(nbt.getString(CLASS_KEY)) else null
    }

    @JvmStatic
    fun clearClass(player: ServerPlayerEntity) {
        val nbt = NbtCompound()
        player.writeCustomDataToNbt(nbt)
        nbt.remove(CLASS_KEY)
        player.readCustomDataFromNbt(nbt)
    }

    @JvmStatic
    fun readClassFromNbt(nbt: NbtCompound): Identifier? {
        return if (nbt.contains(CLASS_KEY)) Identifier(nbt.getString(CLASS_KEY)) else null
    }

    @JvmStatic
    fun writeClassToNbt(nbt: NbtCompound, classId: Identifier?) {
        if (classId != null) {
            nbt.putString(CLASS_KEY, classId.toString())
        } else {
            nbt.remove(CLASS_KEY)
        }
    }
}