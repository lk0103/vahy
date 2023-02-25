package com.example.bakalarka.equation

import android.util.Log
import com.example.vahy.equation.Addition

class Equation(val left : Addition, val right : Addition) {
    var solutions = mutableMapOf<String, Int>()

    fun simplify() {
        left.simplify()
        right.simplify()
    }

    fun findAllVariables() = (left.findAllVariables().toSet() +
            right.findAllVariables().toSet()).toList()

    fun findVariablesStrings() = solutions.keys.toList()

    fun containsBracket() : Boolean =
        left.containsBracket()  || right.containsBracket()

    fun allBracketsSame() : Boolean {
        val brackets = (left.findAllBrackets() + right.findAllBrackets())
            .toList()
        return brackets.all { it == brackets[0] }
    }

    fun findBracket() : Bracket? = (left.findAllBrackets() + right.findAllBrackets())
        .toList().firstOrNull()

    fun findAllBrackets() = (left.findAllBrackets() + right.findAllBrackets()).toSet()

    override fun toString() : String = left.toString() + " = " + right.toString()

    fun setSolution(s : MutableMap<String, Int>){
        Log.i("equality", "niekto meni solutions: povodne solutions: " + solutions +
                            " new solutions: " + s.toString())
        solutions = s
    }

    fun leftEqualsRight() : Boolean = left.evaluate(solutions) == right.evaluate(solutions)

    fun compareLeftRight() : Int {
        val l = left.evaluate(solutions)
        val r = right.evaluate(solutions)
        Log.i("equality", "left: " + l + " right: " + r)
        Log.i("equality", "solutions: " + solutions)
        if (l == r) return 0
        else if (l < r) return 1
        else return -1
    }
}