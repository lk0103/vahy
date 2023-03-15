package com.example.bakalarka.tasks

import kotlin.random.Random

class Level2 : Level(){

    init {
//        tasks = listOf(task1(), task2(), task3(), task4(), task5(),
//            task6(), task7(), task8(), task9(), task10())
        tasks = listOf(task3(), task1(), task4(), task5(), task6())
    }

    fun task1() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(2, 3)
            generator.rangeNumVarRight = Pair(1, 1)
            generator.rangeNumConsRight = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(1, 1)
            generator.rangeNumNegativeConsLeft = Pair(1, 1)
        }
        else {
            generator.rangeNumVarRight = Pair(2, 3)
            generator.rangeNumVarLeft = Pair(1, 1)
            generator.rangeNumConsLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(1, 1)
            generator.rangeNumNegativeConsRight = Pair(1, 1)
        }
        generator.enableConsLeft = true
        generator.enableConsRight = true

        generator.rangeVarSolutions = Pair(3, 8)

        return Pair("solve", generator)
    }

    fun task2() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumVarLeft = Pair(3, 5)
            generator.rangeNumVarRight = Pair(1, 3)
            generator.rangeNumConsRight = Pair(1, 2)
            generator.rangeNumConsLeft = Pair(1, 1)
            generator.rangeNumNegativeConsLeft = Pair(1, 1)
        }
        else {
            generator.rangeNumVarRight = Pair(3, 5)
            generator.rangeNumVarLeft = Pair(1, 3)
            generator.rangeNumConsLeft = Pair(1, 2)
            generator.rangeNumConsRight = Pair(1, 1)
            generator.rangeNumNegativeConsRight = Pair(1, 1)
        }
        generator.enableConsLeft = true
        generator.enableConsRight = true

        generator.rangeVarSolutions = Pair(3, 8)

        return Pair("build", generator)
    }

    fun task3() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumBracketLeft = Pair(1, 1)
            generator.rangeNumConsRight = Pair(1, 2)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumBracketRight = Pair(1, 1)
            generator.rangeNumConsLeft = Pair(1, 2)
            generator.enableConsLeft = true
        }
        generator.rangeNumVarBracket = Pair(1, 1)
        generator.rangeSumConsBracket = Pair(1, 4)
        generator.rangeNumConsBracket = Pair(1, 1)
        generator.rangeVarSolutions = Pair(2, 5)

        return Pair("solve", generator)
    }

    fun task4() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumBracketLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(2, 2)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumBracketRight = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(2, 2)
            generator.enableConsLeft = true
        }
        generator.rangeNumVarBracket = Pair(1, 1)
        generator.rangeSumConsBracket = Pair(2, 4)
        generator.rangeNumConsBracket = Pair(1, 1)
        generator.rangeVarSolutions = Pair(3, 7)

        return Pair("solve", generator)
    }

    fun task5() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumBracketLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(2, 3)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumBracketRight = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(2, 3)
            generator.enableConsLeft = true
        }
        generator.rangeNumVarBracket = Pair(2, 3)
        generator.rangeSumConsBracket = Pair(2, 10)
        generator.rangeNumConsBracket = Pair(1, 2)
        generator.rangeVarSolutions = Pair(3, 6)

        return Pair("build", generator)
    }


    fun task6() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumBracketLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(2, 3)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumBracketRight = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(2, 3)
            generator.enableConsLeft = true
        }
        generator.rangeNumVarBracket = Pair(2, 3)
        generator.rangeSumConsBracket = Pair(3, 8)
        generator.rangeNumConsBracket = Pair(1, 2)
        generator.rangeVarSolutions = Pair(3, 8)

        return Pair("solve", generator)
    }

    fun task7() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumBracketLeft = Pair(3, 5)
            generator.rangeNumBracketRight = Pair(1, 1)
            generator.rangeNumConsRight = Pair(3, 4)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumBracketRight = Pair(3, 5)
            generator.rangeNumBracketLeft = Pair(1, 1)
            generator.rangeNumConsLeft = Pair(3, 4)
            generator.enableConsLeft = true
        }
        generator.rangeNumVarBracket = Pair(1, 1)
        generator.rangeSumConsBracket = Pair(3, 6)
        generator.rangeNumConsBracket = Pair(1, 2)
        generator.rangeVarSolutions = Pair(8, 12)

        return Pair("solve", generator)
    }

    fun task8() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumBracketLeft = Pair(2, 3)
            generator.rangeNumVarRight = Pair(2, 4)
            generator.rangeNumConsRight = Pair(1, 2)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumBracketRight = Pair(2, 3)
            generator.rangeNumVarLeft= Pair(2, 4)
            generator.rangeNumConsLeft = Pair(1, 2)
            generator.enableConsLeft = true
        }
        generator.rangeNumVarBracket = Pair(1, 3)
        generator.rangeSumConsBracket = Pair(3, 10)
        generator.rangeNumConsBracket = Pair(1, 2)
        generator.rangeVarSolutions = Pair(3, 7)

        return Pair("build", generator)
    }

    fun task9() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumBracketLeft = Pair(2, 3)
            generator.rangeNumVarRight = Pair(2, 4)
            generator.rangeNumConsRight = Pair(1, 2)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumBracketRight = Pair(2, 3)
            generator.rangeNumVarLeft= Pair(2, 4)
            generator.rangeNumConsLeft = Pair(1, 2)
            generator.enableConsLeft = true
        }
        generator.rangeNumVarBracket = Pair(1, 1)
        generator.rangeSumConsBracket = Pair(1, 8)
        generator.rangeNumConsBracket = Pair(1, 2)
        generator.rangeVarSolutions = Pair(3, 7)

        return Pair("solve", generator)
    }

    fun task10() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) > 1) {
            generator.rangeNumBracketLeft = Pair(2, 3)
            generator.rangeNumVarLeft = Pair(0, 2)
            generator.rangeNumVarRight = Pair(2, 3)
            generator.rangeNumConsRight = Pair(1, 2)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumBracketRight = Pair(2, 3)
            generator.rangeNumVarRight = Pair(0, 2)
            generator.rangeNumVarLeft= Pair(2, 3)
            generator.rangeNumConsLeft = Pair(1, 2)
            generator.enableConsLeft = true
        }
        generator.rangeNumVarBracket = Pair(1, 1)
        generator.rangeSumConsBracket = Pair(2, 8)
        generator.rangeNumConsBracket = Pair(1, 2)
        generator.rangeVarSolutions = Pair(4, 10)

        return Pair("solve", generator)
    }
}