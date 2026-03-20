package github.artseb.redarkened.util

import github.artseb.artlib.util.AssetRegistry
import github.artseb.redarkened.Redarkened
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys

object ModAssets {
    fun getItem(name: String): Item = AssetRegistry.ASSETS[Redarkened.MOD_ID]?.get(RegistryKeys.ITEM)?.get(name)?.instance as Item? ?: Items.AIR

    fun getAssetsFromKey(regKey: RegistryKey<*>) = AssetRegistry.ASSETS[Redarkened.MOD_ID]?.get(regKey)
}