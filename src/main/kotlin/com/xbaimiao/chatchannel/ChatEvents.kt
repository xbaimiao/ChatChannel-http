package com.xbaimiao.chatchannel

import com.xbaimiao.mirai.message.component.impl.PlainText
import org.bukkit.event.player.AsyncPlayerChatEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.uncolored

/**
 * @Author xbaimiao
 * @Date 2021/10/25 21:26
 */
object ChatEvents {

    @SubscribeEvent
    fun chat(event: AsyncPlayerChatEvent) {
        ChatChannel.bot.getGroups().thenAcceptAsync { list ->
            list.firstOrNull { it.id == 418888134L }?.sendMessage(PlainText("${event.player.name}: ${event.message.uncolored()}"))
        }
    }

}