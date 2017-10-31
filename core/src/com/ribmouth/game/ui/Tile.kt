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

    private val selected: Boolean = false
    private var maxWidth: Float = width - 8
    private var maxHeight: Float = height - 8

    init {
        val atlas = Game.res.getAtlas("pack")!!
        light = atlas.findRegion("light")
        dark = atlas.findRegion("dark")
    }

    open fun update(dt: Float) {
        if (width > maxWidth) {
                width = maxWidth
            }
        if (width < 0) {
                width = 0f
            }
        if (height > maxHeight) {
                height = maxHeight
            }
        if (height < 0) {
                height = 0f
            }
    }

    open fun render(sb: SpriteBatch) {
        sb.draw(light, x - width / 2, y - height / 2, width, height)
    }
}