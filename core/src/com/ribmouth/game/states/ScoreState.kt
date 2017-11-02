package com.ribmouth.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.ribmouth.game.Game
import com.ribmouth.game.handlers.GameStateManager
import com.ribmouth.game.states.TransitionState.Type.*
import com.ribmouth.game.ui.TextImage

/**
 * Created by RibMouth on 2/11/2017.
 */
class ScoreState(gsm: GameStateManager, score: Int) : GameState(gsm) {
    private var scoreText: TextImage = TextImage("score", Game.WIDTH / 2, Game.HEIGHT / 2 + 90)
    private var image: TextImage = TextImage(score.toString(), Game.WIDTH / 2, Game.HEIGHT / 2)

    override fun handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.setState(TransitionState(gsm, this, MenuState(gsm), EXPAND))
        }
    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render(sb: SpriteBatch) {
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        sb.projectionMatrix = cam.combined

        sb.begin()
        scoreText.render(sb)
        image.render(sb)
        sb.end()
    }

    override fun dispose() {

    }

    override fun resizeUser(width: Int, height: Int) {

    }
}