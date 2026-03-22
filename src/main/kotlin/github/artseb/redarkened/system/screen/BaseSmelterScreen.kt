package github.artseb.redarkened.system.screen

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class BaseSmelterScreen(
    handler: BaseSmelterScreenHandler,
    inventory: PlayerInventory,
    title: Text
) : HandledScreen<BaseSmelterScreenHandler>(handler, inventory, title) {

    companion object {
        // Placeholder: reuse vanilla furnace texture until a custom one is made
        private val TEXTURE = Identifier("textures/gui/container/furnace.png")
    }

    override fun init() {
        super.init()
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
    }

    override fun drawBackground(context: DrawContext, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, TEXTURE)

        val x = (width  - backgroundWidth)  / 2
        val y = (height - backgroundHeight) / 2

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight)

        // Fuel flame — drawn bottom-up
        if (handler.isBurning()) {
            val k = (handler.getFuelProgress() * 13).toInt()
            context.drawTexture(TEXTURE, x + 56, y + 36 + 12 - k, 176, 12 - k, 14, k + 1)
        }

        // Cook progress arrow — grows left to right
        val arrowWidth = (handler.getCookProgress() * 24).toInt()
        context.drawTexture(TEXTURE, x + 79, y + 34, 176, 14, arrowWidth, 16)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context)
        super.render(context, mouseX, mouseY, delta)
        drawMouseoverTooltip(context, mouseX, mouseY)
    }
}