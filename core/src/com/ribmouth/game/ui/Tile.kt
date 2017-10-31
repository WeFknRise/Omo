package com.ribmouth.game.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ribmouth.game.Game

/**
 * Created by RibMouth on 31/10/2017.
 */
open class Tile(x: Float, y: Float, width: Float, height: Float) : Box(x, y, width, height) {
    private val light: TextureRegion
    private val dark: TextureRegion

    var selected: Boolean = false

    init {
        val atlas = Game.res.getAtlas("pack")!!
        light = atlas.findRegion("light")
        dark = atlas.findRegion("dark")

        this.width -= 8
        this.height -= 8
    }

    open fun update(dt: Float) {
    }

    open fun render(sb: SpriteBatch) {
        if(selected) {
            sb.draw(light, x - width / 2, y - height / 2, width, height)
        } else {
            sb.draw(dark, x - width / 2, y - height / 2, width, height)
        }
    }
}