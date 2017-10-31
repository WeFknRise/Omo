package com.ribmouth.game.states

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.ribmouth.game.Game
import com.ribmouth.game.handlers.GameStateManager
import com.ribmouth.game.ui.Tile

/**
 * Created by RibMouth on 31/10/2017.
 */
class PlayState(gsm: GameStateManager) : GameState(gsm) {
    private var tiles: ArrayList<ArrayList<Tile>> = arrayListOf()

    private var tileSize: Float = -1f
    private var boardOffset: Float = -1f
    private var boardHeight: Float = -1f

    init {
        createBoard(4, 4)
    }

    override fun handleInput() {
        //TODO("not implemented")
    }

    override fun update(dt: Float) {
        // tiles
        for (row in 0 until tiles.count()) {
            for (col in 0 until tiles[row].count()) {
                tiles[row][col].update(dt)
            }
        }
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