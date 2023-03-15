package com.example.bakalarka.tasks

import kotlin.random.Random

class Level1 : Level(){

    init {
//        tasks = listOf(task1(), task2(), task3(), task4(), task5(),
//                        task6(), task7(), task8(), task9(), task10(), task11())
        tasks = listOf(task1(), task2(), task3(), task4())
    }

    fun task1() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(1, 1)
            generator.rangeNumConsRight = Pair(2, 2)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumVarRight = Pair(1, 1)
            generator.rangeNumConsLeft = Pair(2, 2)
            generator.enableConsLeft = true
        }

        generator.rangeVarSolutions = Pair(4, 8)

        return Pair("solve", generator)
    }

    fun task2() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(2, 2)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumVarRight = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(2, 2)
            generator.enableConsLeft = true
        }

        generator.rangeVarSolutions = Pair(2, 8)

        return Pair("solve", generator)
    }

    fun task3() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(2, 2)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumVarRight = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(2, 2)
            generator.enableConsLeft = true
        }

        generator.rangeVarSolutions = Pair(2, 5)

        return Pair("build", generator)
    }

    fun task4() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(1, 1)
            generator.rangeNumConsRight = Pair(2, 3)
        }
        else {
            generator.rangeNumVarRight = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(1, 1)
        }
        generator.enableConsRight = true
        generator.enableConsLeft = true
        generator.rangeVarSolutions = Pair(2, 6)

        return Pair("solve", generator)
    }

    fun task5() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(3, 4)
            generator.rangeNumConsRight = Pair(2, 3)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumVarRight = Pair(3, 4)
            generator.rangeNumConsLeft = Pair(2, 3)
            generator.enableConsLeft = true
        }

        generator.rangeVarSolutions = Pair(4, 6)

        return Pair("solve", generator)
    }


    fun task6() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(3, 4)
            generator.rangeNumConsLeft = Pair(1, 1)
            generator.rangeNumConsRight = Pair(2, 3)
        }
        else {
            generator.rangeNumVarRight = Pair(3, 4)
            generator.rangeNumConsLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(1, 1)
        }
        generator.enableConsRight = true
        generator.enableConsLeft = true
        generator.rangeVarSolutions = Pair(3, 5)

        return Pair("build", generator)
    }

    fun task7() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(3, 4)
            generator.rangeNumConsLeft = Pair(1, 1)
            generator.rangeNumConsRight = Pair(2, 3)
        }
        else {
            generator.rangeNumVarRight = Pair(3, 4)
            generator.rangeNumConsLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(1, 1)
        }
        generator.enableConsRight = true
        generator.enableConsLeft = true
        generator.rangeVarSolutions = Pair(3, 6)

        return Pair("solve", generator)
    }

    fun task8() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(2, 2)
            generator.rangeNumVarRight = Pair(1, 1)
            generator.rangeNumConsRight = Pair(2, 2)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumVarRight = Pair(2, 2)
            generator.rangeNumVarLeft = Pair(1, 1)
            generator.rangeNumConsLeft = Pair(2, 2)
            generator.enableConsLeft = true
        }

        generator.rangeVarSolutions = Pair(4, 8)

        return Pair("solve", generator)
    }

    fun task9() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(3, 4)
            generator.rangeNumVarRight = Pair(1, 2)
            generator.rangeNumConsRight = Pair(2, 2)
            generator.rangeNumConsLeft = Pair(1, 1)
        }
        else {
            generator.rangeNumVarRight = Pair(3, 4)
            generator.rangeNumVarLeft = Pair(1, 2)
            generator.rangeNumConsLeft = Pair(2, 2)
            generator.rangeNumConsRight = Pair(1, 1)
        }
        generator.enableConsRight = true
        generator.enableConsLeft = true
        generator.rangeVarSolutions = Pair(4, 9)

        return Pair("build", generator)
    }

    fun task10() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(3, 4)
            generator.rangeNumVarRight = Pair(1, 2)
            generator.rangeNumConsRight = Pair(2, 2)
            generator.rangeNumConsLeft = Pair(1, 1)
        }
        else {
            generator.rangeNumVarRight = Pair(3, 4)
            generator.rangeNumVarLeft = Pair(1, 2)
            generator.rangeNumConsLeft = Pair(2, 2)
            generator.rangeNumConsRight = Pair(1, 1)
        }
        generator.enableConsRight = true
        generator.enableConsLeft = true
        generator.rangeVarSolutions = Pair(4, 9)

        return Pair("solve", generator)
    }

    fun task11() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(4, 5)
            generator.rangeNumVarRight = Pair(2, 3)
            generator.rangeNumConsRight = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(1, 2)
        }
        else {
            generator.rangeNumVarRight = Pair(4, 5)
            generator.rangeNumVarLeft = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(1, 2)
        }
        generator.enableConsRight = true
        generator.enableConsLeft = true
        generator.rangeVarSolutions = Pair(4, 9)

        return Pair("solve", generator)
    }
}