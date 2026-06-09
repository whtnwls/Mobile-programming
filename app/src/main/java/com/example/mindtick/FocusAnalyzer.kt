package com.example.mindtick

object FocusAnalyzer {

    fun calculateScore(
        noiseLevel: Int,
        movementLevel: Int
    ): Int {

        var score = 100

        score -= noiseLevel * 5
        score -= movementLevel * 10

        return score.coerceIn(0, 100)
    }

}