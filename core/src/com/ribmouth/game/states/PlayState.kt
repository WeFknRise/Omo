package com.ribmouth.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.utils.Array
import com.ribmouth.game.Game
import com.ribmouth.game.handlers.Difficulty
import com.ribmouth.game.handlers.GameStateManager
import com.ribmouth.game.states.TransitionState.Type.*
import com.ribmouth.game.ui.*

/**
 * Created by RibMouth on 31/10/2017.
 */
class PlayState(gsm: GameStateManager, difficulty: Difficulty) : GameState(gsm) {
    companion object {
        const val SHOW_TIMER: Float = 4.0f
        const val GLOW_ON_SECOND: Float = 3.0f
        const val LEVEL_TIMER: Float = 5.0f
        const val RIGHT_MULTIPLY: Float = 10.0f
        const val WRONG_DEDUCT: Float = 5.0f
        const val TIME_WAIT_UNTIL_GOTO_SCORE_STATE: Int = 1
    }

    object MultiTouch {
        const val MAX_FINGERS: Int = 2
    }

    private var score: ScoreTextImage = ScoreTextImage(Game.WIDTH / 2, Game.HEIGHT - 70)
    private var scoreTimer: Float = LEVEL_TIMER

    private var level: Int = 1
    private var difficulty: Difficulty = difficulty

    private var tiles: ArrayList<ArrayList<SizingTile>> = arrayListOf()
    private var selected: Array<Tile> = Array() //user selected
    private var finished: Array<Tile> = Array() //solution
    private var glows: Array<GlowTile> = Array() //solution

    private var showing: Boolean = true
    private var showTimer: Float = 0.0f

    private var tileSize: Float = -1f
    private var boardOffset: Float = -1f
    private var boardHeight: Float = -1f

    private var light: TextureRegion
    private var dark: TextureRegion

    private var prevPosTouched: kotlin.Array<Pair<Int, Int>> = kotlin.Array(MultiTouch.MAX_FINGERS, { _ -> Pair(-1, -1) })

    // handle to wait after game is done then go to score state
    private var isNeedToWaitBeforeTreatAsDone: Boolean = false
    private var doneTimer: Float = 0.0f

    // back button
    private var backButton: TextImage = TextImage("back", Game.WIDTH / 2, 70f)

    init {
        val texture = Game.res.getAtlas("pack")!!
        light = texture.findRegion("light")
        dark = texture.findRegion("dark")

        createBoard(difficulty.numRows, difficulty.numCols)
        createFinished(difficulty.numGlowing(level))
    }

    override fun handleInput() {
        for (i in 0 until MultiTouch.MAX_FINGERS) {
            if (Gdx.input.isTouched(i)) {
                mouse.x = Gdx.input.getX(i).toFloat()
                mouse.y = Gdx.input.getY(i).toFloat()
                cam.unproject(mouse)

                // back button
                if (backButton.contains(mouse.x, mouse.y)) {
                    gsm.setState(TransitionState(gsm, this, DifficultyState(gsm), EXPAND))
                }

                //tiles
                if (!showing && !isNeedToWaitBeforeTreatAsDone) {
                    if (mouse.y >= boardOffset && mouse.y <= boardOffset + boardHeight) {
                        val row: Int = ((mouse.y - boardOffset) / tileSize).toInt()
                        val col: Int = (mouse.x / tileSize).toInt()

                        if (row >= 0 && row < tiles.count() && col >= 0 && col < tiles[row].count() &&
                                (row != prevPosTouched[i].first || col != prevPosTouched[i].second)) {
                            val tile = tiles[row][col]
                            tile.selected = !tile.selected
                            val glow = GlowTile(tile.x, tile.y, tileSize, tileSize)

                            if (tile.selected) {
                                selected.add(tile)

                                if (!finished.contains(tile)) {
                                    tile.wrong = true
                                    glow.wrong = true
                                    score.addScore(-WRONG_DEDUCT)
                                } else {
                                    tile.wrong = false
                                }

                                // add glow (grow type) effect
                                glows.add(glow)
                            } else {
                                selected.removeValue(tile, true)

                                // add glow (shrink type) effect
                                glow.type = GlowTile.Type.SHRINK
                                if (tile.wrong) {
                                    glow.wrong = true
                                }
                                glows.add(glow)
                            }

                            if (isFinished()) {
                                level++

                                val scoreToAdd = scoreTimer * RIGHT_MULTIPLY
                                score.addScore(scoreToAdd)

                                if (level > difficulty.maxLevel) {
                                    isNeedToWaitBeforeTreatAsDone = true
                                    doneTimer = 0f
                                } else {
                                    createBoard(difficulty.numRows, difficulty.numCols)
                                    createFinished(difficulty.numGlowing(level))

                                    // reset scoreTextImage timer
                                    scoreTimer = LEVEL_TIMER
                                }
                            }
                        }

                        // save for previous touched position
                        prevPosTouched[i] = Pair(row, col)
                    }
                }
            }

            // update previous position if not touch anymore
            if (!Gdx.input.isTouched(i)) {
                prevPosTouched[i] = Pair(-1, -1)
            }
        }
    }

    override fun update(dt: Float) {
        handleInput()
        showObjective(dt)
        checkScoreTimer(dt)

        // update glows
        for (i in glows.count()-1 downTo 0) {
            glows[i].update(dt)

            if (glows[i].shouldBeRemoved) {
                glows.removeIndex(i)
            }
        }

        //score
        score.update(dt)

        // tiles
        for (row in 0 until tiles.count()) {
            for (col in 0 until tiles[row].count()) {
                tiles[row][col].update(dt)
            }
        }

        if (isNeedToWaitBeforeTreatAsDone) {
            doneTimer += dt

            if (doneTimer >= TIME_WAIT_UNTIL_GOTO_SCORE_STATE) {
                // switch to another state thus no need to check whether code enter inside this statement again
                difficultyFinished()
            }
        }
    }

    override fun render(sb: SpriteBatch) {
        sb.projectionMatrix = cam.combined
        sb.begin()

        //render score
        score.render(sb)

        // render level indicator
        val allLevelsWidth = (2 * difficulty.maxLevel - 1) * 10
        for (i in 0 until difficulty.maxLevel) {
            if (i + 1 <= level) {
                sb.draw(light, Game.WIDTH / 2 - allLevelsWidth / 2 + 20 * i, Game.HEIGHT - 125, 10f, 10f)
            } else {
                sb.draw(dark, Game.WIDTH / 2 - allLevelsWidth / 2 + 20 * i, Game.HEIGHT - 125, 10f, 10f)
            }
        }

        // render tiles
        for (row in 0 until tiles.count()) {
            for (col in 0 until tiles[row].count()) {
                tiles[row][col].render(sb)
            }
        }

        // render back button
        backButton.render(sb)

        // render glow
        for (g in glows) {
            g.render(sb)
        }

        sb.end()
    }

    override fun dispose() {

    }

    override fun resizeUser(width: Int, height: Int) {

    }

    private fun checkScoreTimer(dt: Float) {
        if (!showing) {
            scoreTimer -= dt
        }
    }

    private fun showObjective(dt: Float) {
        if (showing) {
            showTimer += dt

            if (showTimer > GLOW_ON_SECOND) {
                if (showTimer % 0.15f < 0.07f) {
                    for (f in finished) {
                        f.selected = true
                    }
                } else {
                    for (f in finished) {
                        f.selected = false
                    }
                }
            }

            if (showTimer > SHOW_TIMER) {
                showing = false

                for (tile in finished) {
                    tile.selected = false
                }
            }
        }
    }

    private fun isFinished(): Boolean {
        if (finished.count() != selected.count()) return false

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
            } while (finished.contains(tiles[row][col], true))

            finished.add(tiles[row][col])
            tiles[row][col].selected = true
        }
    }

    private fun difficultyFinished() {
        gsm.setState(TransitionState(gsm, this, ScoreState(gsm, score.destScore.toInt()), EXPAND))
    }
}