package com.example.vahy.equation

import com.example.bakalarka.equation.Bracket

abstract class Polynom {
    open fun simplify() : Polynom? = null

    abstract fun evaluate(variables : Map<String, Int>) : Int

    open fun findAllVariables() : Set<String> = setOf()

    open fun containsBracket() : Boolean = false

    open fun findAllBrackets() : Set<Bracket> = setOf()

    open fun removeVariable(name: String) : Polynom? = null

    open fun addVariable(name: String) : Polynom? = null

    open fun removeConstant(value : Int) : Polynom? = null

    open fun addConstant(value : Int) : Polynom? = null

    open fun addToConstant(fromValue: Int, value : Int) : Polynom? = null

    open fun addBracket(bracket: Bracket) : Polynom? = null

    open fun removeBracket(bracket: Bracket) : Polynom? = null
}