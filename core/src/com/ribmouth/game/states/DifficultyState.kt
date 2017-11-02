package com.ribmouth.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.ribmouth.game.Game
import com.ribmouth.game.handlers.Difficulty
import com.ribmouth.game.handlers.GameStateManager
import com.ribmouth.game.ui.TextImage

/**
 * Created by RibMouth on 1/11/2017.
 */
class DifficultyState(gsm: GameStateManager) : GameState(gsm) {
    private var difficulties: Array<TextImage> = Array(Difficulty.values().size, { i -> TextImage(Difficulty.values()[i].name, Game.WIDTH / 2, Game.HEIGHT / 2 + 100 - 70 * i) })
    private var backButton: TextImage = TextImage("back", Game.WIDTH / 2, 70.0f)

    override fun handleInput() {
        if (Gdx.input.justTouched()) {
            mouse.x = Gdx.input.x.toFloat()
            mouse.y = Gdx.input.y.toFloat()
            cam.unproject(mouse)

            (0 until difficulties.size)
                    .filter { difficulties[it].contains(mouse.x, mouse.y) }
                    .forEach { gsm.setState(PlayState(gsm, Difficulty.values()[it])) }

            if (backButton.contains(mouse.x, mouse.y)) gsm.setState(MenuState(gsm))
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
        for (difficulty in difficulties) {
            difficulty.render(sb)
        }
        backButton.render(sb)
        sb.end()
    }

    override fun dispose() {

    }

    override fun resizeUser(width: Int, height: Int) {

    }
}