package com.ribmouth.game.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * Created by RibMouth on 2/11/2017.
 */
class GlowTile(x: Float, y: Float, width: Float, height: Float, type: Type = Type.GROW) : Tile(x, y, width, height) {
    companion object {
        const val EFFECT_DURATION: Float = 0.5f
    }

    enum class Type {
        GROW,
        SHRINK
    }

    private var alpha: Float = 1.0f
    var type: Type = type

    var shouldBeRemoved: Boolean = false
    var timer: Float = 0f
    var speed: Float = 200.0f

    override fun update(dt: Float) {
        timer += dt

        // either grow or shrink depends on the type set
        if (type == Type.GROW) {
            width += dt * speed
            height += dt * speed
        } else if (type == Type.SHRINK) {
            width -= dt * speed
            height -= dt * speed
        }

        if (timer >= EFFECT_DURATION) {
            shouldBeRemoved = true
        }
    }

    override fun render(sb: SpriteBatch) {
        alpha = 1 - timer / EFFECT_DURATION

        // get current color from spritebatch
        val color = sb.color

        if (wrong) {
            // set tint color to light red
            sb.setColor(255.0f / 255.0f, 61f / 255f, 61f / 255f, alpha)
        } else {
            sb.setColor(1f, 1f, 1f, alpha)
        }

        sb.draw(light, x - width / 2, y - height / 2, width, height)
        sb.color = color
    }
}