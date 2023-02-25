package com.example.bakalarka.tasks

import android.util.Log
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.vahy.equation.*
import kotlin.random.Random

class EquationsGenerator {
    var rangeNumVarLeft : Pair<Int, Int> = Pair(0, 0)
    var rangeNumVarRight : Pair<Int, Int> = Pair(0, 0)

    var rangeSumConsLeft : Pair<Int, Int> = Pair(0, 0)
    var rangeSumConsRight : Pair<Int, Int> = Pair(0, 0)

    var rangeNumConsLeft : Pair<Int, Int> = Pair(1, 1)
    var rangeNumConsRight : Pair<Int, Int> = Pair(1, 1)

    var rangeNumNegativeConsLeft : Pair<Int, Int> = Pair(0, 0)
    var rangeNumNegativeConsRight : Pair<Int, Int> = Pair(0, 0)

    var rangeNumBracketLeft : Pair<Int, Int> = Pair(0, 0)
    var rangeNumBracketRight : Pair<Int, Int> = Pair(0, 0)

    var rangeNumVarBracket : Pair<Int, Int> = Pair(0, 0)
    var rangeSumConsBracket : Pair<Int, Int> = Pair(0, 0)
    var rangeNumConsBracket : Pair<Int, Int> = Pair(1, 1)

    fun generateLinearEquationWithNaturalSolution(): SystemOfEquations {
        //////////////////////////////////////////////////////
        ///zistit ci sa da nejako zabezpecit aby som generovala len s prirodzenym riesenim
        var systemOfEquations = generateLinearEquation()
        systemOfEquations.solve()
        var count = 0
        while ( !systemOfEquations.hasSolution()){
            systemOfEquations = generateLinearEquation()
            systemOfEquations.solve()
            count++
        }
        Log.i("generate", "pocet pokusov generovania: " + count)
        return systemOfEquations
    }

    private fun generateLinearEquation(): SystemOfEquations {
        val numVarLeft = randomBetweenMinMax(rangeNumVarLeft.first, rangeNumVarLeft.second)
        val numVarRight = randomBetweenMinMax(rangeNumVarRight.first, rangeNumVarRight.second)
        val sumConsLeft = randomBetweenMinMax(rangeSumConsLeft.first, rangeSumConsLeft.second)
        val sumConsRight = randomBetweenMinMax(rangeSumConsRight.first, rangeSumConsRight.second)
        val numBracketsLeft = randomBetweenMinMax(rangeNumBracketLeft.first, rangeNumBracketLeft.second)
        val numBracketsRight = randomBetweenMinMax(rangeNumBracketRight.first, rangeNumBracketRight.second)
        val numNegativeLeft = randomBetweenMinMax(rangeNumNegativeConsLeft.first, rangeNumNegativeConsLeft.second)
        val numNegativeRight = randomBetweenMinMax(rangeNumNegativeConsRight.first, rangeNumNegativeConsRight.second)

        var bracket : Bracket? = null
        if (numBracketsLeft > 0 || numBracketsRight > 0){
            val numVarBracket = randomBetweenMinMax(rangeNumVarBracket.first, rangeNumVarBracket.second)
            val sumConsBracket = randomBetweenMinMax(rangeSumConsBracket.first, rangeSumConsBracket.second)
            bracket = Bracket(createPolynom(numVarBracket, sumConsBracket,
                            rangeNumConsBracket, null, 0))
        }

        val left = createPolynom(numVarLeft, sumConsLeft, rangeNumConsLeft, bracket,
                        numBracketsLeft, numNegativeLeft)
        val right = createPolynom(numVarRight, sumConsRight, rangeNumConsRight, bracket,
                        numBracketsRight, numNegativeRight)

        val equation = Equation(left, right)
        equation.simplify()
        return SystemOfEquations(mutableListOf(equation))
    }

    private fun createPolynom(numVar: Int, sumCons: Int, rangeNumCons: Pair<Int, Int>,
                              bracket: Bracket?, numBracket: Int,
                              numNegative : Int = 0): Addition {

        val polynoms = mutableListOf<Polynom>(
            Multiplication(Variable("x"), Constant(numVar))
        )
        if (bracket != null && numBracket > 0){
            polynoms.add(Multiplication(bracket, Constant(numBracket)))
        }
        val constants = calculateCons(sumCons, rangeNumCons.first, rangeNumCons.second, numNegative)
        polynoms.addAll(constants)
        return Addition(polynoms)
    }

    private fun calculateCons(sumCons: Int, minNumCons : Int, maxNumCons : Int,
                              numNegative : Int) : MutableList<Constant>{
        ////////////////////////////////////////////////////
        ////VYPISOVAT MAXNUMCONS, MINNUMCONS, SUMCONS
//        Log.i("generate", "sumCons: " + sumCons + " minNumCons: " + minNumCons +
//        " maxNumCons: " + maxNumCons)
//        Log.i("generate", "min NumCons: " + Math.min(sumCons, minNumCons) +
//                " max NumCOns: " + Math.min(sumCons, maxNumCons + 1))
        val numCons = if (Math.min(sumCons, maxNumCons) < Math.min(sumCons, minNumCons) ||
            Math.min(sumCons, maxNumCons) < 0 || Math.min(sumCons, minNumCons) < 0) 0
            else if (Math.min(sumCons, maxNumCons) == Math.min(sumCons, minNumCons)) Math.min(sumCons, maxNumCons)
            else
            Random.nextInt(
                Math.min(sumCons, minNumCons),
                Math.min(sumCons, maxNumCons + 1)
            )
//        Log.i("generate", "generated numCons: " + numCons)

        val consBorders = divideConstant(numCons, sumCons)

        return createConstants(consBorders, sumCons, numNegative)
    }

    private fun divideConstant(numCons: Int, sumCons: Int): MutableList<Int> {
        val consBorders = mutableListOf<Int>()
        (1 until numCons).forEach { i ->
            var border = Random.nextInt(1, sumCons)
            while (consBorders.contains(border)) {
                border = Random.nextInt(1, sumCons)
            }
            consBorders.add(border)
        }

        consBorders.sort()
        return consBorders
    }

    private fun createConstants(consBorders: MutableList<Int>, sumCons: Int,
                            numNegative: Int): MutableList<Constant> {
        val constants = mutableListOf<Constant>()
        var previous = 0
        (0 until consBorders.size).forEach { i ->
            constants.add(Constant(consBorders[i] - previous))
            previous = consBorders[i]
        }
        constants.add(Constant(sumCons - previous))
        return if (numNegative > 0) createNegativeConstants(constants, numNegative)
                else constants
    }

    private fun createNegativeConstants(constants : MutableList<Constant>, numNegative: Int)
                                            : MutableList<Constant>{
        constants.shuffle()
        (0 until Math.min(constants.size, numNegative)).forEach { i ->
            constants[i].makeNegative()
        }
        return constants
    }


    private fun randomBetweenMinMax(min : Int, max: Int) =
        if (max < min || max < 0 || min < 0) 0
        else if (max == min) min
        else Random.nextInt(min, max + 1)

}