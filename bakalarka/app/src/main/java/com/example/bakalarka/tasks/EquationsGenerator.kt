package com.example.bakalarka.tasks

import android.util.Log
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.vahy.equation.*
import kotlin.random.Random


class EquationsGenerator : Generator(){
    var rangeNumVarLeft : Pair<Int, Int> = Pair(0, 0)
    var rangeNumVarRight : Pair<Int, Int> = Pair(0, 0)

    var rangeNumConsLeft : Pair<Int, Int> = Pair(1, 1)
    var rangeNumConsRight : Pair<Int, Int> = Pair(1, 1)

    var rangeNumNegativeConsLeft : Pair<Int, Int> = Pair(0, 0)
    var rangeNumNegativeConsRight : Pair<Int, Int> = Pair(0, 0)

    var rangeNumBracketLeft : Pair<Int, Int> = Pair(0, 0)
    var rangeNumBracketRight : Pair<Int, Int> = Pair(0, 0)

    var rangeNumVarBracket : Pair<Int, Int> = Pair(0, 0)
    var rangeSumConsBracket : Pair<Int, Int> = Pair(0, 0)
    var rangeNumConsBracket : Pair<Int, Int> = Pair(1, 1)

    var enableConsLeft = false
    var enableConsRight = false

    var rangeVarSolutions :Pair<Int, Int> = Pair(1, 15)

    private val variable = "x"

    fun generateEquationWithNaturalSolution(): SystemOfEquations {
        val (left, right) = createLeftRightSidesEq()
        if (Equation(left, right).hasSameNumOfVarOnBothSides()) {
            return SystemOfEquations(mutableListOf())
        }

        val eq: Equation? = generateEqWithRandomSolution(left, right)

        if (eq == null) return SystemOfEquations(mutableListOf())

        val systemOfEquations = SystemOfEquations(mutableListOf(eq))
        systemOfEquations.equations.forEach { it.simplify() }
        systemOfEquations.solve()

        if (systemOfEquations.notOneSolution())
            return SystemOfEquations(mutableListOf())

        return systemOfEquations
    }

    private fun generateEqWithRandomSolution(
        left: Addition,
        right: Addition
    ): Equation? {
        var solutions = randomSolution()
        var eq: Equation? = null
        for (i in 0 until 100) {
            eq = generateEquation(left, right, solutions)
            eq?.setSolution(solutions.toMutableMap())

            if (eq != null && eq.compareLeftRight() == 0) {
                Log.i("generate", "pocet generovani: " + (i + 1))
                break
            }

            solutions = randomSolution()
        }
        return eq
    }

    private fun randomSolution(): Map<String, Int> =
        mapOf(variable to randomBetweenMinMax(rangeVarSolutions.first, rangeVarSolutions.second))

    private fun createPolynomOnlyVars(numVar: Int, numBracket: Int,
                                      bracket: Bracket?): Addition{
        val polynom = Addition(
            (mutableListOf(
                Multiplication(Variable(variable), Constant(numVar)),
                Constant(0)
            )).toMutableList()
        )
        if (bracket != null)
            polynom.addends.add(Multiplication(bracket, Constant(numBracket)))
        return polynom
    }

    private fun createLeftRightSidesEq(): Pair<Addition, Addition>{
        val numVarLeft = randomBetweenMinMax(rangeNumVarLeft.first, rangeNumVarLeft.second)
        val numVarRight = randomBetweenMinMax(rangeNumVarRight.first, rangeNumVarRight.second)
        val numBracketsLeft = randomBetweenMinMax(rangeNumBracketLeft.first, rangeNumBracketLeft.second)
        val numBracketsRight = randomBetweenMinMax(rangeNumBracketRight.first, rangeNumBracketRight.second)

        val bracket: Bracket? = generateBracket(numBracketsLeft, numBracketsRight)

        val left = createPolynomOnlyVars(numVarLeft, numBracketsLeft, bracket)
        val right = createPolynomOnlyVars(numVarRight, numBracketsRight, bracket)
        return Pair(left, right)
    }

    private fun generateEquation(left : Addition, right : Addition,
                                 solutions : Map<String, Int>): Equation? {
        val numNegativeLeft = randomBetweenMinMax(rangeNumNegativeConsLeft.first, rangeNumNegativeConsLeft.second)
        val numNegativeRight = randomBetweenMinMax(rangeNumNegativeConsRight.first, rangeNumNegativeConsRight.second)

        val consNegLeft = createNegativeConstants(numNegativeLeft)
        val consNegRight = createNegativeConstants(numNegativeRight)
        var l = Addition((left.addends + consNegLeft).toMutableList())
        var r = Addition((right.addends + consNegRight).toMutableList())

        var (consLeft, consRight, rightConfig) = generateCons(l, r, solutions,
            rangeNumConsLeft, rangeNumConsRight, enableConsLeft, enableConsRight)

        if (rightConfig) {
            l = Addition((l.addends + consLeft).toMutableList())
            r = Addition((r.addends + consRight).toMutableList())
            return Equation(l, r)
        }
        return null
    }

    private fun generateBracket(numBracketsLeft: Int, numBracketsRight: Int): Bracket? {
        var bracket: Bracket? = null
        if (numBracketsLeft > 0 || numBracketsRight > 0) {
            val numVarBracket = randomBetweenMinMax(rangeNumVarBracket.first, rangeNumVarBracket.second)
            val sumConsBracket =
                randomBetweenMinMax(rangeSumConsBracket.first, rangeSumConsBracket.second)
            bracket = Bracket(
                createPolynom(
                    numVarBracket, sumConsBracket,
                    rangeNumConsBracket, null, 0
                )
            )
        }
        return bracket
    }


    private fun createPolynom(numVar: Int, sumCons: Int, rangeNumCons: Pair<Int, Int>,
                              bracket: Bracket?, numBracket: Int): Addition {

        val polynoms : MutableList<Polynom> = mutableListOf(
            Multiplication(Variable(variable), Constant(numVar))
        )

        if (bracket != null && numBracket > 0){
            polynoms.add(Multiplication(bracket, Constant(numBracket)))
        }
        val constants = calculateCons(sumCons, rangeNumCons.first, rangeNumCons.second)
        polynoms.addAll(constants)
        return Addition(polynoms)
    }

    private fun createNegativeConstants(numNegative: Int)
                                            : MutableList<Constant>{
        return (0 until numNegative).map { -Random.nextInt(2, 10) }
            .map { Constant(it) }.toMutableList()
    }
}