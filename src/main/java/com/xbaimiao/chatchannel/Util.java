package com.xbaimiao.chatchannel;

import me.albert.amazingbot.bot.Bot;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.message.data.Image;

/**
 * @Author xbaimiao
 * @Date 2021/10/25 21:42
 */
public class Util {

    public static String query(Image image) {
        return Mirai.getInstance().queryImageUrl(Bot.getApi().getBot(), image);
    }

}
