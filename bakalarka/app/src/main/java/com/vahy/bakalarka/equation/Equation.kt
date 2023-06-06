package com.vahy.bakalarka.equation

import com.vahy.vahy.equation.Addition

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


    fun findBracket() : Bracket? = (left.findAllBrackets() + right.findAllBrackets())
        .toList().firstOrNull()

    fun findAllBrackets() = (left.findAllBrackets() + right.findAllBrackets()).toSet()

    fun setAllBracketInsides(bracketInside : Addition){
        left.setAllBracketInsides(bracketInside)
        right.setAllBracketInsides(bracketInside)
    }

    override fun toString() : String{
        val left = left.toString()
        val right = right.toString()
        return (if (left.isEmpty()) "?" else left) + " = " + (if (right.isEmpty()) "?" else right)
    }

    fun setSolution(s : MutableMap<String, Int>){
        solutions = s
    }

    fun hasSameNumOfVarOnBothSides() : Boolean{
        val lCountVars = left.countNumVariableTypes()
        val rCountVars = right.countNumVariableTypes()
        return lCountVars.any { (rCountVars[it.key] ?: 0) == it.value }
    }

    fun leftEqualsRight() : Boolean = left.evaluate(solutions) == right.evaluate(solutions)

    fun compareLeftRight() : Int {
        val l = left.evaluate(solutions)
        val r = right.evaluate(solutions)
        if (l == r) return 0
        else if (l < r) return 1
        else return -1
    }

    fun isIncomplete() = left.toString().isEmpty() || right.toString().isEmpty()


    fun copy() : Equation {
        val copy = Equation(left.copy() as Addition, right.copy() as Addition)
        copy.setSolution(solutions)
        return copy
    }
}