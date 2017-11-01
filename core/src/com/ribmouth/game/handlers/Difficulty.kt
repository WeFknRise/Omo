package com.ribmouth.game.handlers

/**
 * Created by RibMouth on 1/11/2017.
 */
enum class Difficulty(val numRows: Int, val numCols: Int, val maxLevel: Int) {
    BEGINNER(3, 3, 3),
    EASY(3, 3, 5),
    NORMAL(4, 4, 6),
    HARD(5, 5, 8),
    EXTREME(6, 6, 10);

    fun numGlowing(currentLevel: Int): Int {
        when (this) {
            BEGINNER -> return 1
            EASY -> {
                if(currentLevel in 1..3) return 3
                if(currentLevel in 4..5) return 4
            }
            NORMAL -> {
                if(currentLevel in 1..2) return 4
                if(currentLevel in 3..4) return 5
                if(currentLevel in 5..6) return 6
            }
            HARD -> {
                if(currentLevel in 1..2) return 6
                if(currentLevel in 3..4) return 7
                if(currentLevel in 5..6) return 8
                if(currentLevel in 7..8) return 9
            }
            EXTREME -> {
                if(currentLevel in 1..2) return 8
                if(currentLevel in 3..4) return 9
                if(currentLevel in 5..6) return 10
                if(currentLevel in 7..8) return 11
                if(currentLevel in 9..10) return 12
            }
        }

        return 1
    }
}