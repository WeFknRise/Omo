package com.ribmouth.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20

class Game : ApplicationAdapter() {

    /*
    * Way of making static final variables
    * Note that, even though the members of companion objects look like static members in other languages,
    * at runtime those are still instance members of real objects, and can, for example, implement interfaces
    */
    companion object {
        const val TITLE = "OMO"
        const val WIDTH = 480f
        const val HEIGHT= 800f
    }

    override fun create() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun dispose() {

    }
}