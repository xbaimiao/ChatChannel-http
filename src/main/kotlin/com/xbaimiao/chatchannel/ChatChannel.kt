package com.xbaimiao.chatchannel

import com.xbaimiao.mirai.config.WebSocketBotConfig
import com.xbaimiao.mirai.message.component.impl.PlainText
import com.xbaimiao.mirai.packet.impl.websocket.WebSocketBot
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.command
import taboolib.common.platform.function.submit
import taboolib.module.chat.uncolored
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.nms.sendMap
import taboolib.platform.BukkitPlugin
import java.net.URL
import javax.imageio.ImageIO

@RuntimeDependency(
    value = "!org.jetbrains.kotlin:kotlin-stdlib:1.5.31"
)
object ChatChannel : Plugin() {

    @Config(value = "config.yml")
    lateinit var config: ConfigFile
        private set

    val plugin by lazy { BukkitPlugin.getInstance() }

    lateinit var bot: WebSocketBot

    override fun onDisable() {
        bot.eventChancel.unregisterListener(GroupEvents)
        bot.disable()
    }

    override fun onEnable() {
        bot = WebSocketBot(
            WebSocketBotConfig(
                config.getString("url")!!,
                config.getLong("qq"),
                config.getString("authKey")!!
            )
        )
        bot.connect()
        bot.eventChancel.registerListener(GroupEvents)
        command(
            name = "qq",
            permissionDefault = PermissionDefault.TRUE
        ) {
            literal("look", optional = true) {
                dynamic {
                    execute<Player> { sender, context, argument ->
                        submit(async = true) { sender.sendMap(ImageIO.read(URL(argument))) }
                    }
                }
            }
            literal("send", optional = true) {
                dynamic {
                    execute<Player> { sender, context, argument ->
                        var message = PlaceholderAPI.setPlaceholders(
                            sender,
                            config.getString("GameToGroup")
                        )
                        message = message.replace("%msg%", argument.uncolored())
                        sender.chat(argument)
                        bot.getGroups().thenAcceptAsync { list ->
                            list.firstOrNull { it.id == 418888134L }?.sendMessage(PlainText(message))
                        }
                    }
                }
            }
        }
    }

}