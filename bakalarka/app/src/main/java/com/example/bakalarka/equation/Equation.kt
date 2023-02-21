package com.example.bakalarka.equation

import com.example.vahy.equation.Addition

class Equation(val left : Addition, val right : Addition) {
    var solutions = mutableMapOf<String, Int>()

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
        solutions = s
    }

    fun leftEqualsRight() : Boolean = left.evaluate(solutions) == right.evaluate(solutions)
}