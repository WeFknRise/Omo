package com.ribmouth.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.utils.Array
import com.ribmouth.game.Game
import com.ribmouth.game.handlers.Difficulty
import com.ribmouth.game.handlers.GameStateManager
import com.ribmouth.game.ui.SizingTile
import com.ribmouth.game.ui.Tile

/**
 * Created by RibMouth on 31/10/2017.
 */
class PlayState(gsm: GameStateManager, difficulty: Difficulty) : GameState(gsm) {
    companion object {
        const val SHOW_TIMER: Float = 4.0f
        const val GLOW_ON_SECOND: Float = 3.0f
    }

    object MultiTouch {
        const val MAX_FINGERS: Int = 2
    }

    private var level: Int = 1
    private var difficulty: Difficulty = difficulty

    private var tiles: ArrayList<ArrayList<SizingTile>> = arrayListOf()
    private var selected: Array<Tile> = Array() //user selected
    private var finished: Array<Tile> = Array() //solution

    private var showing: Boolean = true
    private var showTimer: Float = 0.0f

    private var tileSize: Float = -1f
    private var boardOffset: Float = -1f
    private var boardHeight: Float = -1f

    init {
        createBoard(difficulty.numRows, difficulty.numCols)
        createFinished(difficulty.numGlowing(level))
    }

    override fun handleInput() {
        for (i in 0 until MultiTouch.MAX_FINGERS) {
            if (!showing && Gdx.input.isTouched(i)) {
                mouse.x = Gdx.input.getX(i).toFloat()
                mouse.y = Gdx.input.getY(i).toFloat()
                cam.unproject(mouse)

                if (mouse.y >= boardOffset && mouse.y <= boardOffset + boardHeight) {
                    val row: Int = ((mouse.y - boardOffset) / tileSize).toInt()
                    val col: Int = (mouse.x / tileSize).toInt()
                    val tile = tiles[row][col]
                    if(!tile.selected) {
                        tile.selected = true
                        selected.add(tile)

                        if(isFinished()) {
                            level++

                            if(level > difficulty.maxLevel) {
                                difficultyFinished()
                            } else {
                                createBoard(difficulty.numRows, difficulty.numCols)
                                createFinished(difficulty.numGlowing(level))
                            }
                        }
                    }


                    /*if (row >= 0 && row < tiles.count() && col >= 0 && col < tiles[row].count()) {
                        val tile = tiles[row][col]
                        tile.selected = !tile.selected

                        if(tile.selected) {
                            selected.add(tile)
                        } else {
                            selected.removeValue(tile, true)
                        }
                    }

                    if(isFinished()) {
                        createBoard(5, 5)
                        createFinished(3)
                    }*/
                }
            }
        }
    }

    override fun update(dt: Float) {
        handleInput()
        showObjective(dt)

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

    private fun showObjective(dt: Float) {
        if(showing) {
            showTimer += dt

            if(showTimer > GLOW_ON_SECOND) {
                if(showTimer % 0.15f < 0.07f) {
                    for (f in finished) {
                        f.selected = true
                    }
                } else {
                    for (f in finished) {
                        f.selected = false
                    }
                }
            }

            if(showTimer > SHOW_TIMER) {
                showing = false

                for (tile in finished) {
                    tile.selected = false
                }
            }
        }
    }

    private fun isFinished(): Boolean {
        if(finished.count() != selected.count()) return false

        return finished.all { selected.contains(it, true) }
    }

    private fun createBoard(numRow: Int, numCol: Int) {
        // create tiles based on fixed setting of GRID
        tileSize = Game.WIDTH / (if (numCol >= numRow) numCol else numRow)
        boardHeight = tileSize * numRow
        boardOffset = (Game.HEIGHT - boardHeight) / 2

        tiles.clear()

        for (row in 0 until numRow) {
            tiles.add(row, arrayListOf())
            for (col in 0 until numCol) {
                tiles[row].add(col, SizingTile(col * tileSize + tileSize / 2, row * tileSize + boardOffset + tileSize / 2, tileSize, tileSize))
                tiles[row][col].timer = -((numRow - row) * 0.02f) - col * 0.05f
            }
        }
    }

    private fun createFinished(numTilesToLight: Int) {
        selected.clear()
        finished.clear()

        showing = true
        showTimer = 0f

        for (i in 0 until numTilesToLight) {
            var row: Int
            var col: Int

            do {
                row = random(tiles.count() - 1)
                col = random(tiles[row].count() - 1)
            } while(finished.contains(tiles[row][col], true))

            finished.add(tiles[row][col])
            tiles[row][col].selected = true
        }
    }

    private fun difficultyFinished() {
        Gdx.app.exit()
    }
}