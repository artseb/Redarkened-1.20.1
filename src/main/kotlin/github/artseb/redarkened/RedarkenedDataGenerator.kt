package github.artseb.redarkened

import github.artseb.artlib.datagen.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

object RedarkenedDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		val pack = fabricDataGenerator.createPack()

		pack.addProvider { output -> Language(output as FabricDataOutput, Redarkened.MOD_ID) }
		pack.addProvider { output -> Model(output as FabricDataOutput, Redarkened.MOD_ID) }
		pack.addProvider { output -> Recipe(output as FabricDataOutput, Redarkened.MOD_ID) }
		pack.addProvider { output -> LootTable(output as FabricDataOutput, Redarkened.MOD_ID) }
	}
}