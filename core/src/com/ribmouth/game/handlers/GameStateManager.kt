package com.ribmouth.game.handlers

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.ribmouth.game.Game
import com.ribmouth.game.states.GameState
import java.util.*

/**
 * Created by RibMouth on 31/10/2017.
 */
class GameStateManager(game: Game) : Disposable {
    var game: Game
        private set // the setter is private and has the default implementation
    private var gameStates: Stack<GameState>

    //Instead of defining a constructor use init block -> params are the ones defined next to class
    init {
        this.game = game
        this.gameStates = Stack()
    }

    fun update(dt: Float) {
        this.gameStates.peek().update(dt)
    }

    fun render(sb: SpriteBatch) {
        this.gameStates.peek().render(sb)
    }

    fun setState(gameState: GameState) {
        this.gameStates.forEach { /*it.dispose() */ }
        this.gameStates.clear()
        this.gameStates.push(gameState)
    }

    fun pushState(gameState: GameState) {
        this.gameStates.push(gameState)
    }

    fun popState() {
        val g = this.gameStates.pop()
        //g.dispose()
    }

    override fun dispose() {
        TODO("not implemented")
    }
}