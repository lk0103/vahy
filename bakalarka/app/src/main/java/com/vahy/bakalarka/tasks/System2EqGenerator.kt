package com.vahy.bakalarka.tasks

import android.util.Log
import com.vahy.bakalarka.equation.Equation
import com.vahy.bakalarka.equation.SystemOfEquations
import com.vahy.vahy.equation.*
import kotlin.random.Random

class System2EqGenerator : Generator(){
    var rangeNumVarLeft1 : List<Pair<Int, Int>> = listOf(Pair(0, 0), Pair(0, 0))
    var rangeNumVarRight1 : List<Pair<Int, Int>> = listOf(Pair(0, 0), Pair(0, 0))

    var rangeNumVarLeft2 : List<Pair<Int, Int>> = listOf(Pair(0, 0), Pair(0, 0))
    var rangeNumVarRight2 : List<Pair<Int, Int>> = listOf(Pair(0, 0), Pair(0, 0))

    var rangeNumConsLeft : MutableList<Pair<Int, Int>> = mutableListOf(Pair(1, 1), Pair(1, 1))
    var rangeNumConsRight : MutableList<Pair<Int, Int>> = mutableListOf(Pair(1, 1), Pair(1, 1))

    var enableConsLeft : MutableList<Boolean> = mutableListOf(false, false)
    var enableConsRight : MutableList<Boolean> = mutableListOf(false, false)

    var rangeVarSolutions : List<Pair<Int, Int>> = listOf(Pair(1, 15), Pair(1, 15), Pair(1, 15))

    private val variables = listOf("x", "y", "z")

    private var generatedSolution : Map<String, Int> = mapOf()

    fun generateSystem2DiophantineEquations(): SystemOfEquations {
        if (    rangeNumVarLeft1.size > 3 || rangeNumVarLeft2.size > 3 ||
                rangeNumVarRight1.size > 3 || rangeNumVarRight2.size > 3 ||
                rangeNumConsLeft.size > 2 || rangeNumConsRight.size > 2)
            return SystemOfEquations(mutableListOf())

        val (left, right) = createLeftRightSidesOfEqs()

        var eq: MutableList<Equation?> = generateWithRandomSolutions(left, right)

        if (eq.any { it == null}) return SystemOfEquations(mutableListOf())

        Log.i("generate", "range var solutions: " + rangeVarSolutions)
        val sys2eq = SystemOfEquations(eq.map { it!! }.toMutableList())
        sys2eq.equations.forEach { it.simplify() }
        sys2eq.solve()
        if (sys2eq.notOneSolution() || generatedSolution != sys2eq.solutions) {
            Log.i("generate", "INFINITE SOLUTIONS " + sys2eq.solutions + " " + sys2eq)
            return SystemOfEquations(mutableListOf())
        }
        Log.i("generate", "real solutions: " + sys2eq.solutions)
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
        Log.i("generate", "generated solutions: " + solutions)
        while (solutions["x"] == solutions["y"]) {
            solutions = randomSolutionMap()
            Log.i("generate", "generated solutions: " + solutions)
        }
        generatedSolution = solutions
        return solutions
    }

    private fun generateWithRandomSolutions(
        left: List<Addition>,
        right: List<Addition>
    ): MutableList<Equation?> {
        var solutions = generateRandomSolutions()
        var eq: MutableList<Equation?> = mutableListOf(null, null)
        for (i in 0 until 30) {
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