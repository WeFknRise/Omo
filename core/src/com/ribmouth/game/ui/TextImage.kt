package com.ribmouth.game.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.ribmouth.game.Game

/**
 * Created by RibMouth on 1/11/2017.
 */
class TextImage(text: String, x: Float, y: Float) : Box(x, y, 50.0f * text.length + SPACING * (text.length - 1), 50.0f) {
    companion object {
        const val SIZE = 50
        const val SPACING = 4
    }

    private var fontSheets: Array<Array<TextureRegion>>
    var text: String = text.toLowerCase() //Only lower case as it is the same in the bitmap
        set(value) {
            width = 50.0f
            field = value
        }

    init {
        // process TextureRegion
        val sheet = Game.res.getAtlas("pack")!!.findRegion("fontsheet")
        fontSheets = sheet.split(SIZE, SIZE)
    }

    fun render(sb: SpriteBatch) {
        for (i in 0 until text.length) {
            var index: Int = 'a'.toInt()
            when (text[i]) {
                in 'a'..'z' -> index = text[i] - 'a'
                in '0'..'9' -> index = text[i] - '0' + 27
                ' ' -> index = 26 // hard-code as per sheet is designed that way
                '+' -> index = 37 // hard-code as per sheet is designed that way, 26 chars + 10 digits + 1 space
                '-' -> index = 38 // hard-code as per sheet is designed that way, 26 chars + 10 digits + 1 space + 1 (+ symbol)
            }

            val row = index / fontSheets[0].size
            val col = index % fontSheets[0].size
            sb.draw(fontSheets[row][col], (x - width / 2 + SIZE * i) + SPACING * i, y - height / 2)
        }
    }
}