package com.ribmouth.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.ribmouth.game.Game
import com.ribmouth.game.handlers.GameStateManager
import com.ribmouth.game.states.TransitionState.Type.*
import com.ribmouth.game.ui.Graphic
import com.ribmouth.game.ui.TextImage

/**
 * Created by RibMouth on 1/11/2017.
 */
class MenuState(gsm: GameStateManager) : GameState(gsm) {
    private var title: Graphic = Graphic(Game.res.getAtlas("pack")!!.findRegion("omo"), Game.WIDTH / 2, Game.HEIGHT / 2 + 100)
    private var play: TextImage = TextImage("play", Game.WIDTH / 2, Game.HEIGHT / 2 - 50)
    //private var highScore: TextImage = TextImage("scores", Game.WIDTH / 2, Game.HEIGHT / 2 - 130)

    override fun handleInput() {
        if (Gdx.input.justTouched()) {
            mouse.x = Gdx.input.x.toFloat()
            mouse.y = Gdx.input.y.toFloat()
            cam.unproject(mouse)

            if (play.contains(mouse.x, mouse.y)) {
                gsm.setState(TransitionState(gsm, this, DifficultyState(gsm), BLACK_FADE))
            }
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

        title.render(sb)
        play.render(sb)
        //highScore.render(sb)

        sb.end()
    }

    override fun dispose() {

    }

    override fun resizeUser(width: Int, height: Int) {

    }
}