package com.ribmouth.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.ribmouth.game.handlers.Content
import com.ribmouth.game.handlers.Difficulty
import com.ribmouth.game.handlers.Difficulty.*
import com.ribmouth.game.handlers.GameStateManager
import com.ribmouth.game.states.PlayState

class Game : ApplicationAdapter() {

    /**
     * Normally, properties declared as having a non-null type must be initialized in the constructor.
     * However, fairly often this is not convenient. For example, properties can be initialized through dependency injection,
     * or in the setup method of a unit test. In this case, you cannot supply a non-null initializer in the constructor,
     * but you still want to avoid null checks when referencing the property inside the body of a class.
     *
     * To handle this case, you can mark the property with the lateinit modifier
     */
    private lateinit var gsm: GameStateManager
    private lateinit var sb: SpriteBatch

    /**
    * Way of making static final variables
    * Note that, even though the members of companion objects look like static members in other languages,
    * at runtime those are still instance members of real objects, and can, for example, implement interfaces
    */
    companion object {
        const val TITLE = "OMO"
        const val WIDTH = 480f
        const val HEIGHT= 800f

        var res: Content = Content()
            private set
    }

    override fun create() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)

        sb = SpriteBatch()
        gsm = GameStateManager(this)

        // load resources
        res.loadAtlas("pack.pack", "pack")

        //Push state
        gsm.pushState(PlayState(gsm, EASY))
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        gsm.update(Gdx.graphics.deltaTime)
        gsm.render(sb)
    }

    override fun dispose() {

    }
}