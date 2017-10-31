package com.ribmouth.game.states

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.ribmouth.game.Game
import com.ribmouth.game.handlers.GameStateManager

/**
 * Created by RibMouth on 31/10/2017.
 */
abstract class GameState(gsm: GameStateManager) {
    protected var gsm: GameStateManager = gsm

    lateinit protected var cam: OrthographicCamera

    init {
        cam = OrthographicCamera()
        cam.setToOrtho(false, Game.WIDTH, Game.HEIGHT)
    }

    //Abstract methods
    abstract fun handleInput()
    abstract fun update(dt: Float)
    abstract fun render(sb: SpriteBatch)
}