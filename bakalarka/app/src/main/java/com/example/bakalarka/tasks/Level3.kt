package com.example.bakalarka.tasks


import kotlin.random.Random

class Level3: Level(){

    init {
        tasks = listOf(task1(), task2(), task3(), task4()
             , task5(),task6(), task7())
    }

    fun task1() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft = Pair(2, 4)
            generator.rangeNumConsRight = Pair(2, 2)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumVarRight = Pair(2, 4)
            generator.rangeNumConsLeft = Pair(2, 2)
            generator.enableConsLeft = true
        }

        generator.rangeVarSolutions = Pair(2, 5)

        return Pair("build", generator)
    }


    fun task2() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) >= 1) {
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
        generator.rangeVarSolutions = Pair(2, 4)

        return Pair("build", generator)
    }

    fun task3() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) >= 1) {
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
        generator.rangeVarSolutions = Pair(1, 5)

        return Pair("build", generator)
    }

    fun task4() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumVarLeft = Pair(3, 4)
            generator.rangeNumVarRight = Pair(2, 3)
            generator.rangeNumConsRight = Pair(1, 1)
            generator.rangeNumConsLeft = Pair(1, 1)
            generator.rangeNumNegativeConsLeft = Pair(1, 1)
        }
        else {
            generator.rangeNumVarRight = Pair(3, 4)
            generator.rangeNumVarLeft = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(1, 1)
            generator.rangeNumConsRight = Pair(1, 1)
            generator.rangeNumNegativeConsRight = Pair(1, 1)
        }
        generator.enableConsLeft = true
        generator.enableConsRight = true

        generator.rangeVarSolutions = Pair(2, 5)

        return Pair("build", generator)
    }

    fun task5() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumBracketLeft = Pair(2, 3)
            generator.rangeNumConsRight = Pair(2, 3)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumBracketRight = Pair(2, 3)
            generator.rangeNumConsLeft = Pair(2, 3)
            generator.enableConsLeft = true
        }
        generator.rangeNumVarBracket = Pair(1, 2)
        generator.rangeSumConsBracket = Pair(2, 5)
        generator.rangeNumConsBracket = Pair(1, 1)

        generator.rangeVarSolutions = Pair(1, 1)

        return Pair("build", generator)
    }

    fun task6() : Pair<String, EquationsGenerator>{
        val generator = EquationsGenerator()
        if (Random.nextInt(2) >= 1) {
            generator.rangeNumBracketLeft = Pair(2, 3)
            generator.rangeNumBracketRight = Pair(1, 1)
            generator.rangeNumVarRight = Pair(2, 4)
            generator.rangeNumConsRight = Pair(1, 1)
            generator.enableConsRight = true
        }
        else {
            generator.rangeNumBracketRight = Pair(2, 3)
            generator.rangeNumBracketLeft = Pair(1, 1)
            generator.rangeNumVarLeft= Pair(2, 4)
            generator.rangeNumConsLeft = Pair(1, 1)
            generator.enableConsLeft = true
        }
        generator.rangeNumVarBracket = Pair(1, 3)
        generator.rangeSumConsBracket = Pair(3, 5)
        generator.rangeNumConsBracket = Pair(1, 1)
        generator.rangeVarSolutions = Pair(1, 3)

        return Pair("build", generator)
    }

    fun task7() : Pair<String, System2EqGenerator>{
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

        generator.rangeVarSolutions = listOf(Pair(1, 3), Pair(1, 3))

        return Pair("build", generator)
    }
}