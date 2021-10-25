package com.xbaimiao.chatchannel

import org.bukkit.entity.Player

/**
 * @Author xbaimiao
 * @Date 2021/10/25 21:17
 */
object Switch {

    val map = HashMap<String, Boolean>()

    fun Player.setSwitch(boolean: Boolean) {
        map[this.name] = boolean
    }

    fun Player.isSwitch(): Boolean {
        return map[this.name] ?: true
    }

}