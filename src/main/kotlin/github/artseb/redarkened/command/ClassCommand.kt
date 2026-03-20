package github.artseb.redarkened.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import github.artseb.redarkened.Redarkened
import github.artseb.redarkened.system.classes.ClassApplier
import github.artseb.redarkened.system.classes.ClassType
import github.artseb.redarkened.system.classes.PlayerClassStorage
import github.artseb.redarkened.util.ModClasses
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object ClassCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            CommandManager.literal("class")
                .then(
                    CommandManager.argument("className", StringArgumentType.word())
                    .suggests { ctx, builder ->
                        ModClasses.CLASSES.forEach { (_, registrableClass) ->
                            if (registrableClass.classType == ClassType.SUB) {
                                builder.suggest(registrableClass.name)
                            }
                        }
                        builder.buildFuture()
                    }
                    .executes { ctx ->
                        val player = ctx.source.player!!
                        val className = StringArgumentType.getString(ctx, "className")
                        val classId = Identifier(Redarkened.MOD_ID, "sub/$className")
                        if (ModClasses.CLASSES[classId] != null) {
                            PlayerClassStorage.setClass(player, classId)
                            ClassApplier.applyClass(player, classId)
                            ctx.source.sendFeedback({ Text.literal("Class set to $className") }, false)
                            1
                        } else {
                            ctx.source.sendError(Text.literal("Unknown class"))
                            0
                        }
                    }
                )
                .then(
                    CommandManager.literal("clear")
                    .executes { ctx ->
                        val player = ctx.source.player!!
                        PlayerClassStorage.clearClass(player)
                        ClassApplier.clearClass(player)
                        ctx.source.sendFeedback({ Text.literal("Class cleared") }, false)
                        1
                    }
                )
        )
    }
}