package com.xbaimiao.chatchannel

import com.xbaimiao.chatchannel.Switch.isSwitch
import me.albert.amazingbot.bot.Bot
import me.albert.amazingbot.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.LightApp
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import taboolib.common.platform.event.SubscribeEvent
import java.util.*

object GroupEvents {

    @SubscribeEvent
    fun chat(event: GroupMessageEvent) {
        val userId = event.userID

        val uuid: UUID? = Bot.getApi().getPlayer(userId)

        val name = if (uuid == null) {
            event.event.sender.nameCard
        } else Bukkit.getOfflinePlayer(uuid).name

        val msg = StringBuilder()

        val message = event.event.message
        val group = event.event.group
        val images = ArrayList<Image>()
        message.forEach { m ->
            if (m is Image) {
                msg.append(ChatChannel.config.getString("imageFormat"))
                images.add(m)
            } else if (m is At) {
                val atId: Long = m.target
                msg.append("§a").append("@").append(Objects.requireNonNull(group[atId])!!.nick)
                sendAt(name!!, m)
            } else if (m.contentToString().contains("\"app\":")) {
                msg.append("§f[§aAPP§f]")
            } else if (m is LightApp) {
                msg.append("§f[§aAPP§f]")
            } else {
                msg.append(m.contentToString())
            }
        }

        val prefix: String = ChatChannel.config.getString("format").replace("%user%", name!!)
        val text = prefix.replace("%msg%", msg.toString())
        sendMessage(images, text)
    }

    private fun sendMessage(images: List<Image>, text: String) {
        if (images.isEmpty()) {
            for (player in Bukkit.getOnlinePlayers()) {
                if (player.isSwitch()) {
                    player.sendMessage(text)
                }
            }
            return
        }
        sendImage(images, text)
    }

    private fun sendImage(images: List<Image>, text: String) {
        var text = text
        val imageFormat = ChatChannel.config.getString("imageFormat")
        var imageAt = 0
        val tellrawJson = TextComponent()

        text = text.replace(imageFormat, "¤")
        var stringBuilder = StringBuilder()
        for (element in text) {
            val charAt = element
            if (charAt == '¤') {
                tellrawJson.addExtra(stringBuilder.toString())
                stringBuilder = StringBuilder()
                val image = images[imageAt]
                imageAt++
                val t = TextComponent(imageFormat)
                t.hoverEvent = HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    ComponentBuilder(ChatChannel.config.getString("imageHover")).create()
                )
                val url = Util.query(image)
                t.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/qq look $url")
                tellrawJson.addExtra(t)
                continue
            }
            stringBuilder.append(charAt)
        }
        for (player in Bukkit.getOnlinePlayers()) {
            if (player.isSwitch()) {
                player.spigot().sendMessage(tellrawJson)
            }
        }
    }

    private fun sendAt(name: String, at: At) {
        val atId: Long = at.target
        val uuid: UUID? = Bot.getApi().getPlayer(atId)
        if (uuid != null) {
            val player1 = Bukkit.getOfflinePlayer(uuid)
            if (player1.isOnline) {
                player1.player!!.sendTitle(
                    ChatChannel.config.getString("At.title").replace("%player%", name),
                    ChatChannel.config.getString("At.subTitle").replace("%player%", name),
                    20, 20, 20
                )
            }
        }
    }
}