package com.vahy.bakalarka.tasks

import kotlin.random.Random

class Level4 : Level(){

    init {
        tasks = listOf(task1(), task2(), task3(), task4()
            , task5(), task6(), task7())
    }

    fun task1() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 1), Pair(0, 0))
            generator.rangeNumVarRight1 = mutableListOf(Pair(0, 0), Pair(2, 3))
            if (Random.nextInt(2) >= 1) {
                generator.enableConsRight[1] = true
            }
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 1), Pair(0, 0))
            generator.rangeNumVarLeft1 = mutableListOf(Pair(0, 0), Pair(2, 3))
            if (Random.nextInt(2) >= 1) {
                generator.enableConsLeft[1] = true
            }
        }

        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(1, 1), Pair(1, 3))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(1, 1), Pair(1, 3))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }

        generator.rangeVarSolutions = listOf(Pair(1, 6), Pair(1, 6))

        return Pair("solve", generator)
    }

    fun task2() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 1), Pair(0, 0))
            generator.rangeNumVarRight1 = mutableListOf(Pair(0, 0), Pair(1, 2))
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 1), Pair(0, 0))
            generator.rangeNumVarLeft1 = mutableListOf(Pair(0, 0), Pair(1, 2))
            generator.enableConsLeft[1] = true
        }

        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(2, 3), Pair(0, 1))
            generator.rangeNumVarRight2 = mutableListOf(Pair(0, 0), Pair(1, 2))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(2, 3), Pair(0, 1))
            generator.rangeNumVarLeft2 = mutableListOf(Pair(0, 0), Pair(1, 2))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }

        generator.rangeVarSolutions = listOf(Pair(3, 9), Pair(2, 9))

        return Pair("solve", generator)
    }

    fun task3() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsRight[0] = Pair(1, 2)
            generator.enableConsRight[0] = true
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsLeft[0] = Pair(1, 2)
            generator.enableConsLeft[0] = true
        }

        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }

        generator.rangeVarSolutions = listOf(Pair(1, 5), Pair(1, 5))

        return Pair("solve", generator)
    }

    fun task4() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsRight[0] = Pair(1, 2)
            generator.enableConsRight[0] = true
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsLeft[0] = Pair(1, 2)
            generator.enableConsLeft[0] = true
        }

        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }

        generator.rangeVarSolutions = listOf(Pair(3, 9), Pair(3, 9))

        return Pair("solve", generator)
    }


    fun task5() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumVarRight1 = mutableListOf(Pair(0, 0), Pair(3, 4))
            generator.rangeNumConsRight[0] = Pair(1, 2)
            generator.enableConsRight[0] = true
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumVarLeft1 = mutableListOf(Pair(0, 0), Pair(3, 4))
            generator.rangeNumConsLeft[0] = Pair(1, 2)
            generator.enableConsLeft[0] = true
        }

        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(3, 4), Pair(0, 1))
            generator.rangeNumVarRight2 = mutableListOf(Pair(0, 0), Pair(2, 3))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(3, 4), Pair(0, 1))
            generator.rangeNumVarLeft2 = mutableListOf(Pair(0, 0), Pair(2, 3))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }

        generator.rangeVarSolutions = listOf(Pair(2, 8), Pair(5, 11))
        return Pair("solve", generator)
    }


    fun task6() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsRight[0] = Pair(1, 2)
            generator.enableConsRight[0] = true
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsLeft[0] = Pair(1, 2)
            generator.enableConsLeft[0] = true
        }

        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(2, 3), Pair(0, 0))
            generator.rangeNumVarRight2 = mutableListOf(Pair(0, 0), Pair(1, 2))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(2, 3), Pair(0, 0))
            generator.rangeNumVarLeft2 = mutableListOf(Pair(0, 0), Pair(1, 2))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }

        generator.rangeVarSolutions = listOf(Pair(2, 6), Pair(2, 6))
        return Pair("solve", generator)
    }

    fun task7() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 3), Pair(1, 2))
            generator.rangeNumVarRight1 = mutableListOf(Pair(0, 0), Pair(0, 1))
            generator.rangeNumConsRight[0] = Pair(1, 2)
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 3), Pair(1, 2))
            generator.rangeNumVarLeft1 = mutableListOf(Pair(0, 0), Pair(0, 1))
            generator.rangeNumConsLeft[0] = Pair(1, 2)
        }
        generator.enableConsRight[0] = true
        generator.enableConsLeft[0] = true

        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(2, 3), Pair(0, 1))
            generator.rangeNumVarRight2 = mutableListOf(Pair(0, 0), Pair(2, 3))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(2, 3), Pair(0, 1))
            generator.rangeNumVarLeft2 = mutableListOf(Pair(0, 0), Pair(2, 3))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }

        generator.rangeVarSolutions = listOf(Pair(4, 10), Pair(4, 10))
        return Pair("solve", generator)
    }
}