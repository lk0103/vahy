package com.example.vahy.equation

import com.example.bakalarka.equation.Bracket

abstract class Polynom {
    open fun simplify() : Polynom? = null

    abstract fun evaluate(variables : Map<String, Int>) : Int

    open fun countNumVariableTypes() : MutableMap<String, Int> = mutableMapOf()

    open fun countNumConsValues() : MutableMap<Int, Int> = mutableMapOf()

    open fun countNumBrackets() : MutableMap<Bracket, Int> = mutableMapOf()

    fun findAllVariables() : Set<String> = countNumVariableTypes().keys.toSet()

    fun containsBracket() : Boolean = countNumBrackets().keys.size > 0

    fun findAllBrackets() : Set<Bracket> = countNumBrackets().keys.toSet()

    open fun setAllBracketInsides(bracketInside : Addition){

    }

    open fun removeVariable(name: String) : Polynom? = null

    open fun addVariable(name: String) : Polynom? = null

    open fun removeConstant(value : Int) : Polynom? = null

    open fun addConstant(value : Int) : Polynom? = null

    open fun addToConstant(fromValue: Int, value : Int) : Polynom? = null

    open fun addBracket(bracket: Bracket) : Polynom? = null

    open fun removeBracket(bracket: Bracket) : Polynom? = null

    open fun copy() : Polynom = Addition(mutableListOf())
}