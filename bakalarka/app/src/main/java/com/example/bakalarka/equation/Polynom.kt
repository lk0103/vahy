package com.example.vahy.equation

import com.example.bakalarka.equation.Bracket

abstract class Polynom {
    abstract fun evaluate(variables : Map<String, Double>) : Double

    open fun findAllVariables() : Set<String> = setOf()

    open fun containsBracket() : Boolean = false

    open fun findAllBrackets() : Set<Bracket> = setOf()

    open fun removeVariable(name: String) : Polynom? = null

    open fun addVariable(name: String) : Polynom? = null

    open fun removeConstant(value : Double) : Polynom? = null

    open fun addConstant(value : Double) : Polynom? = null

    open fun addToConstant(fromValue: Double, value : Double) : Polynom? = null

    open fun addBracket(bracket: Bracket) : Polynom? = null

    open fun removeBracket(bracket: Bracket) : Polynom? = null
}