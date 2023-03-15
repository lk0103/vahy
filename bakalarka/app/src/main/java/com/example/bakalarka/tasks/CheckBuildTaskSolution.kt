package com.example.bakalarka.tasks

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.bakalarka.MainActivity
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.ui.main.BuildScaleFromEqView
import com.example.vahy.ScalesView
import com.example.vahy.equation.Addition

class CheckBuildTaskSolution {
    lateinit var mainActivity : MainActivity

    fun checkSolution(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        clickedView.onTouchEvent(event)
        if (clickedView !is BuildScaleFromEqView || !clickedView.checkSolution){
            return false
        }
        clickedView.checkSolution = false

        val userEq = scalesView.getEquations()
        val taskEq = clickedView.getEquations()

        if (userEq.size != taskEq.size) {
            Toast.makeText(mainActivity, "wrong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (userEq.size == 1){
            return chechSysOf1Eq(userEq, taskEq, mainActivity)
        }

        if (userEq.size == 2){
            if (checkSysOf2Eq(userEq, taskEq, mainActivity))
                return true
        }

        Toast.makeText(mainActivity, "wrong", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun chechSysOf1Eq(userEq: List<Equation>, taskEq: List<Equation>,
                              mainActivity: MainActivity
    ): Boolean {
        val res = checkIf2EquationsSame(userEq[0], taskEq[0])
        Toast.makeText(
            mainActivity, if (res) "right"
            else "wrong", Toast.LENGTH_SHORT
        ).show()
        return res
    }

    private fun checkSysOf2Eq(userEq: List<Equation>, taskEq: List<Equation>,
                              mainActivity: MainActivity
    ): Boolean {
        if (checkIf2EquationsSame(userEq[0], taskEq[0]) &&
            checkIf2EquationsSame(userEq[1], taskEq[1])) {
            Toast.makeText(mainActivity, "right", Toast.LENGTH_SHORT).show()
            return true
        }
        if (checkIf2EquationsSame(userEq[0], taskEq[1]) &&
            checkIf2EquationsSame(userEq[1], taskEq[0])) {
            Toast.makeText(mainActivity, "right", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    fun checkIf2EquationsSame(eq1 : Equation, eq2 : Equation) : Boolean{
        Log.i("generate", "eq1: " + eq1 + " eq2: " + eq2)
        varMapsCombinations().forEach { varMap ->
            Log.i("generate", "varMap: " + varMap)
            if (checkIf2PolynomsSame(eq1.left, eq2.left, varMap)  &&
                checkIf2PolynomsSame(eq1.right, eq2.right, varMap))
                return true
        }
        return false
    }

    fun checkIf2PolynomsSame(p1 : Addition, p2 : Addition, varMap : Map<String, String>) : Boolean{
        Log.i("generate", "polynom1: " + p1 + " polynom2: " + p2)
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
            Log.i("generate", "nesedia cons: " + cons1 + " " + cons2)
            return true
        }
        return false
    }

    private fun checkVars(p1: Addition, p2: Addition, varMap: Map<String, String>): Boolean {
        val vars1 = p1.countNumVariableTypes()
        val vars2 = p2.countNumVariableTypes()
        if (vars1.size != vars2.size ||
            vars1.any { v1 -> vars2[varMap[v1.key]] != v1.value }) {
            Log.i("generate", "nesedia vars: " + vars1 + " " + vars2)
            return true
        }
        return false
    }

    private fun checkBrackets(brackets1: MutableMap<Bracket, Int>,
                              brackets2: MutableMap<Bracket, Int>): Boolean {
        if (brackets1.size != brackets2.size || brackets1.size > 1 ||
            (brackets1.size == 1 &&
                    brackets1[brackets1.keys.toList()[0]] != brackets2[brackets2.keys.toList()[0]])) {
            Log.i("generate", "nesedia brackets1 : " + brackets1 + " " + brackets2)
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
            Log.i("generate", "nesedia brackets2 : " + brackets1 + " " + brackets2)
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