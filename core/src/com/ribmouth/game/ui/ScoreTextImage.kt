package com.ribmouth.game.ui

/**
 * Created by RibMouth on 2/11/2017.
 */
class ScoreTextImage(x: Float, y: Float) : TextImage("0", x, y) {
    var score: Float = 0f
        private set
    var destScore: Float = 0f
        private set
    var speed: Float = 0.15f

    fun update(dt: Float) {
        if(Math.abs(score - destScore) >= 0.0001f) {
            score += speed * (destScore - score)
        }

        if(score < 0f) {
            score = 0f
        }

        // round first then convert to string for representation
        text = score.toInt().toString()
    }

    fun addScore(amount: Float) {
        destScore = score + amount

        if(destScore < 0f) {
            destScore = 0f
        }
    }
}