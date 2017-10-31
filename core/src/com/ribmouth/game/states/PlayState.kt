package com.ribmouth.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.ribmouth.game.Game
import com.ribmouth.game.handlers.GameStateManager
import com.ribmouth.game.ui.Tile

/**
 * Created by RibMouth on 31/10/2017.
 */
class PlayState(gsm: GameStateManager) : GameState(gsm) {
    object MultiTouch {
        const val MAX_FINGERS: Int = 2
    }

    private var tiles: ArrayList<ArrayList<Tile>> = arrayListOf()

    private var tileSize: Float = -1f
    private var boardOffset: Float = -1f
    private var boardHeight: Float = -1f

    init {
        createBoard(4, 4)
    }

    override fun handleInput() {
        for (i in 0 until MultiTouch.MAX_FINGERS) {
            if (Gdx.input.isTouched(i)) {
                mouse.x = Gdx.input.getX(i).toFloat()
                mouse.y = Gdx.input.getY(i).toFloat()
                cam.unproject(mouse)

                if (mouse.y >= boardOffset && mouse.y <= boardOffset + boardHeight) {
                    val row: Int = ((mouse.y - boardOffset) / tileSize).toInt()
                    val col: Int = (mouse.x / tileSize).toInt()

                    if (row >= 0 && row < tiles.count() && col >= 0 && col < tiles[row].count()) {
                        val tile = tiles[row][col]
                        tile.selected = true
                    }
                }
            }
        }
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render(sb: SpriteBatch) {
        sb.projectionMatrix = cam.combined
        sb.begin()

        // render tiles
        for (row in 0 until tiles.count()) {
            for (col in 0 until tiles[row].count()) {
                tiles[row][col].render(sb)
            }
        }

        sb.end()
    }

    private fun createBoard(numRow: Int, numCol: Int) {
        // create tiles based on fixed setting of GRID
        tileSize = Game.WIDTH / ( if (numCol >= numRow) numCol else numRow )
        boardHeight = tileSize * numRow
        boardOffset = (Game.HEIGHT - boardHeight) / 2

        tiles.clear()

        for (row in 0 until numRow) {
            tiles.add(row, arrayListOf())
            for (col in 0 until numCol) {
                tiles[row].add(col,
                        Tile(
                                col * tileSize + tileSize / 2,
                                row * tileSize + boardOffset + tileSize / 2,
                                tileSize,
                                tileSize
                        )
                )
            }
        }
    }
}