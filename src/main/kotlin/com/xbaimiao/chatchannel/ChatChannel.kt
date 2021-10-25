package com.xbaimiao.chatchannel

import com.xbaimiao.chatchannel.Switch.isSwitch
import com.xbaimiao.chatchannel.Switch.setSwitch
import me.albert.amazingbot.bot.Bot
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.command
import taboolib.module.chat.uncolored
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile
import taboolib.module.nms.sendMap
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.sendLang

@RuntimeDependency(
    value = "!org.jetbrains.kotlin:kotlin-stdlib:1.5.31"
)
object ChatChannel : Plugin() {

    @Config(value = "config.yml")
    lateinit var config: SecuredFile
        private set

    val plugin by lazy { BukkitPlugin.getInstance() }

    override fun onEnable() {
        command(
            name = "qq",
            permissionDefault = PermissionDefault.TRUE
        ) {
            literal("look", optional = true) {
                dynamic {
                    execute<Player> { sender, context, argument ->
                        sender.sendMap(argument)
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
                        Bot.getApi().bot.groups.forEach {
                            Bot.getApi().sendGroupMsg(it.id.toString(), message)
                        }
                    }
                }
            }
            literal("onOff", optional = true) {
                execute<Player> { sender, context, argument ->
                    if (sender.isSwitch()) {
                        sender.setSwitch(false)
                        sender.sendLang("off")
                    } else {
                        sender.setSwitch(true)
                        sender.sendLang("on")
                    }
                }
            }
        }
    }

}