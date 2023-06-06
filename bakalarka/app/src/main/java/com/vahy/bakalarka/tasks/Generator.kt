package com.vahy.bakalarka.tasks

import com.vahy.vahy.equation.Addition
import com.vahy.vahy.equation.Constant
import kotlin.random.Random

open class Generator {

    protected fun generateCons(left: Addition, right: Addition,
                               solutions: Map<String, Int>,
                               rangeNumConsLeft : Pair<Int, Int>,
                               rangeNumConsRight : Pair<Int, Int>,
                               enableConsLeft : Boolean, enableConsRight : Boolean)
            : Triple<MutableList<Constant>, MutableList<Constant>, Boolean>
    {
        val sumCons = Math.abs(left.evaluate(solutions) - right.evaluate(solutions))
        var consLeft = mutableListOf<Constant>()
        var consRight = mutableListOf<Constant>()
        var rightConfig = true

        if (canAddConsToLeft(left, right, solutions, enableConsLeft, enableConsRight)) {
            consLeft = calculateCons(sumCons, rangeNumConsLeft.first, rangeNumConsLeft.second)
        }
        else if (canAddConsToRight(left, right, solutions, enableConsLeft, enableConsRight)) {
            consRight = calculateCons(sumCons, rangeNumConsRight.first, rangeNumConsRight.second)
        }
        else if (enableConsLeft && enableConsRight) {
            val leftIsLess = left.evaluate(solutions) < right.evaluate(solutions)
            val randomNum = randomBetweenMinMax(1, 8)
            consLeft = calculateCons(randomNum + if (leftIsLess) sumCons else 0,
                rangeNumConsLeft.first, rangeNumConsLeft.second)
            consRight = calculateCons(randomNum + if (!leftIsLess) sumCons else 0,
                rangeNumConsRight.first, rangeNumConsRight.second)
        }
        else {
            if (left.evaluate(solutions) != right.evaluate(solutions) &&
                !enableConsLeft && !enableConsRight)
                rightConfig = false
        }

        if ((enableConsLeft && consLeft.isEmpty()) ||
            (enableConsRight && consRight.isEmpty()))
            rightConfig = false
        return Triple(consLeft, consRight, rightConfig)
    }

    private fun canAddConsToRight(
        left: Addition, right: Addition,
        solutions: Map<String, Int>,
        enableConsLeft : Boolean, enableConsRight : Boolean
    ) = left.evaluate(solutions) > right.evaluate(solutions) &&
            !enableConsLeft && enableConsRight

    private fun canAddConsToLeft(
        left: Addition, right: Addition,
        solutions: Map<String, Int>,
        enableConsLeft : Boolean, enableConsRight : Boolean
    ) = left.evaluate(solutions) < right.evaluate(solutions) &&
            enableConsLeft && !enableConsRight

    protected fun calculateCons(sumCons: Int, minNumCons : Int, maxNumCons : Int) : MutableList<Constant>{
        val consBorders = calculateAndDivideCons(sumCons, maxNumCons, minNumCons)
        return createConstants(consBorders, sumCons)
    }

    protected fun calculateAndDivideCons(sumCons: Int, maxNumCons: Int,
                                       minNumCons: Int): MutableList<Int>
    {
        val numCons = if (Math.min(sumCons, maxNumCons) < Math.min(sumCons, minNumCons) ||
                            Math.min(sumCons, maxNumCons) < 0 ||
                            Math.min(sumCons, minNumCons) < 0)
                            0
                    else if (Math.min(sumCons, maxNumCons) == Math.min(sumCons, minNumCons))
                            Math.min(sumCons, maxNumCons)
                    else
                            Random.nextInt(Math.min(sumCons, minNumCons),
                                            Math.min(sumCons, maxNumCons + 1))

        return divideConstant(numCons, sumCons)
    }

    protected fun divideConstant(numCons: Int, sumCons: Int): MutableList<Int> {
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


    protected fun createConstants(consBorders: MutableList<Int>, sumCons: Int): MutableList<Constant> {
        val constants = mutableListOf<Constant>()
        var previous = 0
        (0 until consBorders.size).forEach { i ->
            constants.add(Constant(consBorders[i] - previous))
            previous = consBorders[i]
        }
        constants.add(Constant(sumCons - previous))
        return constants
    }

    protected fun randomBetweenMinMax(min : Int, max: Int) =
        if (max < min || max < 0 || min < 0) 0
        else if (max == min) min
        else Random.nextInt(min, max + 1)
}