package com.xbaimiao.chatchannel

import com.xbaimiao.mirai.entity.MemberFriend
import com.xbaimiao.mirai.event.GroupMessageEvent
import com.xbaimiao.mirai.message.component.impl.At
import com.xbaimiao.mirai.message.component.impl.Image
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.greenrobot.eventbus.Subscribe

object GroupEvents {

    @Subscribe
    fun chat(event: GroupMessageEvent) {
        if (event.group.id != 418888134L) {
            return
        }
        val sender = event.sender as MemberFriend
        val name = sender.nickName
        val msg = StringBuilder()

        val message = event.component
        val images = ArrayList<Image>()
        message.toList().forEach { m ->
            when (m) {
                is Image -> {
                    msg.append(ChatChannel.config.getString("imageFormat"))
                    images.add(m)
                }
                is At -> {
                    msg.append("§a").append("@").append(m.display)
                }
                else -> msg.append(m.serializeToPlainText())
            }
        }

        val prefix: String = ChatChannel.config.getString("format")!!.replace("%user%", name)
        val text = prefix.replace("%msg%", msg.toString())
        sendMessage(images, text)
    }

    private fun sendMessage(images: List<Image>, text: String) {
        if (images.isEmpty()) {
            for (player in Bukkit.getOnlinePlayers()) {
                player.sendMessage(text)
            }
            return
        }
        sendImage(images, text)
    }

    private fun sendImage(images: List<Image>, text: String) {
        val imageFormat = ChatChannel.config.getString("imageFormat")!!
        var imageAt = 0
        val tellrawJson = TextComponent()

        val replaceText = text.replace(imageFormat, "¤")
        var stringBuilder = StringBuilder()
        for (element in replaceText) {
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
                val url = image.queryUrl()
                t.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/qq look $url")
                tellrawJson.addExtra(t)
                continue
            }
            stringBuilder.append(charAt)
        }
        for (player in Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(tellrawJson)
        }
    }

}