package com.example.bakalarka.tasks

import android.util.Log
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.vahy.equation.*
import kotlin.random.Random

class System2EqGenerator : Generator(){
    var rangeNumVarLeft1 : List<Pair<Int, Int>> = listOf(Pair(0, 0), Pair(0, 0))
    var rangeNumVarRight1 : List<Pair<Int, Int>> = listOf(Pair(0, 0), Pair(0, 0))

    var rangeNumVarLeft2 : List<Pair<Int, Int>> = listOf(Pair(0, 0), Pair(0, 0))
    var rangeNumVarRight2 : List<Pair<Int, Int>> = listOf(Pair(0, 0), Pair(0, 0))

    var rangeNumConsLeft : List<Pair<Int, Int>> = listOf(Pair(1, 1), Pair(1, 1))
    var rangeNumConsRight : List<Pair<Int, Int>> = listOf(Pair(1, 1), Pair(1, 1))

    var enableConsLeft = listOf(false, false)
    var enableConsRight = listOf(false, false)

    var rangeVarSolutions : List<Pair<Int, Int>> = listOf(Pair(1, 15), Pair(1, 15), Pair(1, 15))

    private val variables = listOf("x", "y", "z")

    fun generateSystem2DiophantineEquations(): SystemOfEquations {
        if (    rangeNumVarLeft1.size > 3 || rangeNumVarLeft2.size > 3 ||
                rangeNumVarRight1.size > 3 || rangeNumVarRight2.size > 3 ||
                rangeNumConsLeft.size > 2 || rangeNumConsRight.size > 2)
            return SystemOfEquations(mutableListOf())

        val (left, right) = createLeftRightSidesOfEqs()
//        if ((0 until left.size).any { i ->
//                Equation(left[i], right[i]).hasSameNumOfVarOnBothSides()
//            }) {
//            Log.i("generate", "left: " + left+ " right: " + right+" same sides: " +
//                    ((0 until left.size).any { i ->
//                        Equation(left[i], right[i]).hasSameNumOfVarOnBothSides()
//                    }))
//            return SystemOfEquations(mutableListOf())
//        }

        var eq: MutableList<Equation?> = generateWithRandomSolutions(left, right)

        if (eq.any { it == null}) return SystemOfEquations(mutableListOf())

        val sys2eq = SystemOfEquations(eq.map { it!! }.toMutableList())
        sys2eq.equations.forEach { it.simplify() }
        sys2eq.solve()
        return sys2eq
    }

    private fun createLeftRightSidesOfEqs(): Pair<List<Addition>, List<Addition>> {
        val numVarLeft1 = rangeNumVarLeft1.map { randomBetweenMinMax(it.first, it.second) }
        val numVarRight1 = rangeNumVarRight1.map { randomBetweenMinMax(it.first, it.second) }
        val numVarLeft2 = rangeNumVarLeft2.map { randomBetweenMinMax(it.first, it.second) }
        val numVarRight2 = rangeNumVarRight2.map { randomBetweenMinMax(it.first, it.second) }

        val left = listOf(createPolynomOnlyVars(numVarLeft1), createPolynomOnlyVars(numVarLeft2))
        val right = listOf(createPolynomOnlyVars(numVarRight1), createPolynomOnlyVars(numVarRight2))
        return Pair(left, right)
    }

    private fun generateRandomSolutions(): Map<String, Int> {
        var solutions = randomSolutionMap()
        while (solutions["x"] == solutions["y"]) {
            solutions = randomSolutionMap()
        }
        return solutions
    }

    private fun generateWithRandomSolutions(
        left: List<Addition>,
        right: List<Addition>
    ): MutableList<Equation?> {
        var solutions = generateRandomSolutions()
        var eq: MutableList<Equation?> = mutableListOf(null, null)
        for (i in 0 until 100) {
            eq = build2Equations(left, right, solutions)
            eq.forEach { it?.setSolution(solutions.toMutableMap()) }
            if (eq.all { it != null && it.compareLeftRight() == 0 }) {
                Log.i("generate", "pocet generovani: " + (i + 1))
                break
            }

            solutions = generateRandomSolutions()
        }
        return eq
    }

    private fun randomSolutionMap(): Map<String, Int> {
        val solutions = mutableMapOf(
            "x" to Random.nextInt(rangeVarSolutions[0].first, rangeVarSolutions[0].second),
            "y" to Random.nextInt(rangeVarSolutions[1].first, rangeVarSolutions[1].second)
        )
        if ((rangeNumVarLeft1.size == 3 || rangeNumVarLeft2.size == 3 ||
                    rangeNumVarRight1.size == 3 || rangeNumVarRight2.size == 3 )
            && rangeVarSolutions.size >= 3)
            solutions["z"] = Random.nextInt(rangeVarSolutions[2].first, rangeVarSolutions[2].second)
        return solutions
    }


    private fun createPolynomOnlyVars(numVar: List<Int>) =
        Addition(((0 until numVar.size).map { i ->
            Multiplication(Variable(variables[i]), Constant(numVar[i]))
        }.toMutableList() + mutableListOf(Constant(0))).toMutableList())


    private fun build2Equations(left: List<Addition>, right: List<Addition>,
                                solutions: Map<String, Int>): MutableList<Equation?>
    {
        val eq1 : MutableList<Equation?> = mutableListOf(null, null)
        (0 until 2).forEach { i ->
            val (consLeft, consRight, rightConfig) = generateCons(left[i], right[i],
                                solutions, rangeNumConsLeft[i], rangeNumConsRight[i],
                                enableConsLeft[i], enableConsRight[i])

            if (rightConfig) {
                val l = Addition((left[i].addends + consLeft).toMutableList())
                val r = Addition((right[i].addends + consRight).toMutableList())
                eq1[i] = Equation(l, r)
            }
        }
        return eq1
    }
}