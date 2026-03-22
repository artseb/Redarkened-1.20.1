package github.artseb.redarkened.system.block

import github.artseb.artlib.content.RegistrableAsset
import github.artseb.artlib.system.block.BlockModel
import github.artseb.redarkened.system.blockentity.BaseSmelterBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.DefaultParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

abstract class BaseSmelterBlock(
    id: Identifier,
    settings: FabricBlockSettings,
    blockModel: BlockModel,
    tags: Array<String>? = null
) : RegistrableAsset<Block>(id, Registries.BLOCK, tags) {

    // Exposed so NihiliteSmelterBlock can pass it into its BlockEntity constructor
    lateinit var blockEntityType: BlockEntityType<out BaseSmelterBlockEntity>
        private set

    // ── Abstract ambient ─────────────────────────────────────────────────────

    abstract fun getAmbientParticle(): DefaultParticleType?
    abstract fun getAmbientSound(): SoundEvent?

    // ── Child: BlockItem ──────────────────────────────────────────────────────

    private inner class SmelterBlockItem
        : RegistrableAsset<BlockItem>(id, Registries.ITEM, null, "rpg_blocks") {
        override fun createInstance() = BlockItem(this@BaseSmelterBlock.instance, Item.Settings())
    }

    // ── Child: BlockEntityType ────────────────────────────────────────────────

    private inner class SmelterBlockEntityType
        : RegistrableAsset<BlockEntityType<*>>(
        Identifier(id.namespace, "${id.path}_entity"),
        @Suppress("UNCHECKED_CAST")
        (Registries.BLOCK_ENTITY_TYPE as Registry<in BlockEntityType<*>>)
    ) {
        @Suppress("UNCHECKED_CAST")
        override fun createInstance(): BlockEntityType<*> =
            BlockEntityType.Builder.create(
                { pos, state -> createBlockEntity(pos, state) },
                this@BaseSmelterBlock.instance
            ).build(null).also {
                blockEntityType = it as BlockEntityType<out BaseSmelterBlockEntity>
            }
    }

    // ── The actual Block object ───────────────────────────────────────────────
    // Captured as a field so we can close over `this@BaseSmelterBlock` in
    // getTicker — impossible to do if we passed an anonymous object to super().

    private val smelterBlock = object : Block(settings), BlockEntityProvider {

        init {
            defaultState = stateManager.defaultState
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(Properties.LIT, false)
        }

        override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
            builder.add(Properties.HORIZONTAL_FACING, Properties.LIT)
        }

        override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
            defaultState
                .with(Properties.HORIZONTAL_FACING, ctx.horizontalPlayerFacing.opposite)
                .with(Properties.LIT, false)

        override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
            this@BaseSmelterBlock.createBlockEntity(pos, state)

        @Deprecated("Deprecated in Java")
        override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

        override fun randomDisplayTick(
            state: BlockState, world: World, pos: BlockPos, random: Random
        ) {
            if (!state.get(Properties.LIT)) return

            val px = pos.x + 0.5
            val py = pos.y + 1.0
            val pz = pos.z + 0.5

            // Ambient sound — low probability per tick call
            val sound = this@BaseSmelterBlock.getAmbientSound()
            if (sound != null && random.nextInt(20) == 0) {
                world.playSound(
                    px, py, pz, sound,
                    SoundCategory.BLOCKS,
                    0.6f,
                    0.85f + random.nextFloat() * 0.3f,
                    false
                )
            }

            // Ash particles — spawn from top face, offset toward facing front
            val particle = this@BaseSmelterBlock.getAmbientParticle() ?: return
            val facing = state.get(Properties.HORIZONTAL_FACING)
            val ox = facing.offsetX * 0.15
            val oz = facing.offsetZ * 0.15

            repeat(random.nextInt(3) + 1) {
                world.addParticle(
                    particle,
                    px + ox + (random.nextDouble() - 0.5) * 0.35,
                    py,
                    pz + oz + (random.nextDouble() - 0.5) * 0.35,
                    (random.nextDouble() - 0.5) * 0.015,
                    0.03 + random.nextDouble() * 0.02,
                    (random.nextDouble() - 0.5) * 0.015
                )
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : BlockEntity> getTicker(
            world: World, state: BlockState, type: BlockEntityType<T>
        ): BlockEntityTicker<T>? {
            if (!this@BaseSmelterBlock::blockEntityType.isInitialized) return null
            if (type != this@BaseSmelterBlock.blockEntityType) return null
            return BlockEntityTicker { w, p, s, be ->
                (be as BaseSmelterBlockEntity).tick(w, p, s)
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onUse(
            state: BlockState, world: World, pos: BlockPos,
            player: PlayerEntity, hand: Hand, hit: BlockHitResult
        ): ActionResult {
            if (world.isClient) return ActionResult.SUCCESS
            val be = world.getBlockEntity(pos) as? NamedScreenHandlerFactory
                ?: return ActionResult.PASS
            player.openHandledScreen(be)
            return ActionResult.SUCCESS
        }
    }

    // ── RegistrableAsset impl ─────────────────────────────────────────────────

    private lateinit var itemAsset: SmelterBlockItem
    private lateinit var entityTypeAsset: SmelterBlockEntityType

    init { model = blockModel }

    abstract fun createBlockEntity(pos: BlockPos, state: BlockState): BaseSmelterBlockEntity

    override fun createInstance(): Block = smelterBlock

    override fun register() {
        super.register()
        itemAsset       = SmelterBlockItem().also       { it.register() }
        entityTypeAsset = SmelterBlockEntityType().also { it.register() }
    }

    override fun initialize() {
        super.initialize()
        itemAsset.initialize()
        entityTypeAsset.initialize()
    }

    override fun postRegistration() {
        super.postRegistration()
        itemAsset.postRegistration()
        entityTypeAsset.postRegistration()
    }
}