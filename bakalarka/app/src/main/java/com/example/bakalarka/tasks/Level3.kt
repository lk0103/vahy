package com.example.bakalarka.tasks

import android.icu.text.TimeZoneFormat.ParseOption
import kotlin.random.Random

class Level3: Level(){

    init {
//        tasks = listOf(task1(), task2(), task3(), task4(), task5(),
//            task6(), task7(), task8(), task9(), task10())
        tasks = listOf(task1(), task2(), task3(), task4())
    }

    fun task1() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsRight[0] = Pair(1, 2)
            generator.enableConsRight[0] = true
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsLeft[0] = Pair(1, 2)
            generator.enableConsLeft[0] = true
        }

        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }

        generator.rangeVarSolutions = listOf(Pair(1, 3), Pair(1, 3))

        return Pair("solve", generator)
    }

    fun task2() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsRight[0] = Pair(1, 2)
            generator.enableConsRight[0] = true
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsLeft[0] = Pair(1, 2)
            generator.enableConsLeft[0] = true
        }

        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }

        generator.rangeVarSolutions = listOf(Pair(3, 5), Pair(3, 5))

        return Pair("solve", generator)
    }

    fun task3() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsRight[0] = Pair(1, 2)
            generator.enableConsRight[0] = true
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsLeft[0] = Pair(1, 2)
            generator.enableConsLeft[0] = true
        }

        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }

        generator.rangeVarSolutions = listOf(Pair(3, 5), Pair(3, 5))
        return Pair("solve", generator)
    }

    fun task4() : Pair<String, System2EqGenerator>{
        val generator = System2EqGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsRight[0] = Pair(1, 2)
            generator.enableConsRight[0] = true
        } else {
            generator.rangeNumVarRight1 = mutableListOf(Pair(1, 1), Pair(1, 2))
            generator.rangeNumConsLeft[0] = Pair(1, 2)
            generator.enableConsLeft[0] = true
        }

        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsRight[1] = Pair(1, 2)
            generator.enableConsRight[1] = true
        } else {
            generator.rangeNumVarRight2 = mutableListOf(Pair(1, 1), Pair(3, 4))
            generator.rangeNumConsLeft[1] = Pair(1, 2)
            generator.enableConsLeft[1] = true
        }
        generator.rangeVarSolutions = listOf(Pair(3, 5), Pair(3, 5))
        return Pair("build", generator)
    }
}