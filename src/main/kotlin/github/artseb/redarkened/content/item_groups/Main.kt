package github.artseb.redarkened.content.item_groups

import github.artseb.artlib.content.RegistrableAsset
import github.artseb.artlib.registry.Register
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.util.ModAssets
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@Register
class Main: RegistrableAsset<ItemGroup>(
    Identifier(Redarkened.MOD_ID, "rpg_main"),
    Registries.ITEM_GROUP
) {
    override fun createInstance(): ItemGroup {
        val registryKey = getRegistryKey()
        instance = FabricItemGroup.builder()
            .icon { ItemStack(ModAssets.getItem("electrum_hammer")) }
            .displayName(Text.translatable("${registryKey.registry.path}.${id.namespace}.${id.path}"))
            .build()
        return instance
    }

    override fun postRegistration() {
        ItemGroupEvents.modifyEntriesEvent(getRegistryKey()).register { entries ->
            ModAssets.getAssetsFromKey(RegistryKeys.ITEM)?.forEach { (_, asset) ->
                if (asset.group == id.path || asset.group == null) entries.add { asset.instance as Item }
            }
        }
    }
}