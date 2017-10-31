package com.ribmouth.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.ribmouth.game.Game

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = Game.WIDTH.toInt()
        config.height = Game.HEIGHT.toInt()
        config.title = Game.TITLE
        LwjglApplication(Game(), config)
    }
}