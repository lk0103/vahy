package com.vahy.bakalarka.tasks

import android.view.MotionEvent
import android.view.View
import com.vahy.bakalarka.equation.Bracket
import com.vahy.bakalarka.equation.Equation
import com.vahy.bakalarka.ui.main.BuildScaleMenuView
import com.vahy.vahy.ScalesView
import com.vahy.vahy.equation.Addition

class CheckBuildTaskSolution {

    fun checkSolution(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        clickedView.onTouchEvent(event)
        if (clickedView !is BuildScaleMenuView || !clickedView.checkSolution)
            return false
        clickedView.checkSolution = false

        val userEq = scalesView.getEquations()
        val taskEq = clickedView.getEquations()

        if (userEq.size != taskEq.size)
            return false

        if (userEq.size == 1)
            return checkSysOf1Eq(userEq, taskEq)

        if (userEq.size == 2 && checkSysOf2Eq(userEq, taskEq))
            return true

        return false
    }

    private fun checkSysOf1Eq(userEq: List<Equation>, taskEq: List<Equation>): Boolean {
        val res = varMapsCombinations().any { varMap ->
            checkIf2EquationsSame(userEq[0], taskEq[0], varMap)}
        return res
    }

    private fun checkSysOf2Eq(userEq: List<Equation>, taskEq: List<Equation>): Boolean {
        if (varMapsCombinations().any { varMap ->
                checkIf2EquationsSame(userEq[0], taskEq[0], varMap) &&
                        checkIf2EquationsSame(userEq[1], taskEq[1], varMap) }) {
            return true
        }
        if (varMapsCombinations().any { varMap ->
                checkIf2EquationsSame(userEq[0], taskEq[1], varMap) &&
            checkIf2EquationsSame(userEq[1], taskEq[0], varMap)}) {
            return true
        }
        return false
    }

    fun checkIf2EquationsSame(eq1 : Equation, eq2 : Equation, varMap: Map<String, String>) : Boolean{
        return (checkIf2PolynomsSame(eq1.left, eq2.left, varMap)  &&
            checkIf2PolynomsSame(eq1.right, eq2.right, varMap))
                ||
                checkIf2PolynomsSame(eq1.left, eq2.right, varMap)  &&
                checkIf2PolynomsSame(eq1.right, eq2.left, varMap)
    }

    fun checkIf2PolynomsSame(p1 : Addition, p2 : Addition, varMap : Map<String, String>) : Boolean{
        if (checkCons(p1, p2)) return false
        if (checkVars(p1, p2, varMap)) return false

        val brackets1 = p1.countNumBrackets()
        val brackets2 = p2.countNumBrackets()
        if (checkBrackets(brackets1, brackets2)) return false
        if (checkInsideBrackets(brackets1, brackets2, varMap)) return false
        return true
    }

    private fun checkCons(p1: Addition, p2: Addition): Boolean {
        val cons1 = p1.countNumConsValues()
        val cons2 = p2.countNumConsValues()
        if (cons1.size != cons2.size ||
            cons1.any { c1 -> cons2[c1.key] != c1.value }) {
            return true
        }
        return false
    }

    private fun checkVars(p1: Addition, p2: Addition, varMap: Map<String, String>): Boolean {
        val vars1 = p1.countNumVariableTypes()
        val vars2 = p2.countNumVariableTypes()
        if (vars1.size != vars2.size ||
            vars1.any { v1 -> vars2[varMap[v1.key]] != v1.value }) {
            return true
        }
        return false
    }

    private fun checkBrackets(brackets1: MutableMap<Bracket, Int>,
                              brackets2: MutableMap<Bracket, Int>): Boolean {
        if (brackets1.size != brackets2.size || brackets1.size > 1 ||
            (brackets1.size == 1 &&
                    brackets1[brackets1.keys.toList()[0]] != brackets2[brackets2.keys.toList()[0]])) {
            return true
        }
        return false
    }

    private fun checkInsideBrackets(brackets1: MutableMap<Bracket, Int>,
                                    brackets2: MutableMap<Bracket, Int>,
                                    varMap: Map<String, String>): Boolean {
        if (brackets1.size > 0 && !checkIf2PolynomsSame(
                brackets1.keys.first().polynom,
                brackets2.keys.first().polynom, varMap)) {
            return true
        }
        return false
    }

    fun varMapsCombinations(): List<Map<String, String>>{
        val varMap = mutableListOf<Map<String, String>>()
        val vars = listOf("x", "y", "z")
        for (x in vars){
            for (y in vars){
                if (x == y) continue
                for (z in vars){
                    if (z == x || z == y) continue
                    varMap.add(mutableMapOf("x" to x, "y" to y, "z" to z))
                }
            }
        }
        return varMap
    }
}