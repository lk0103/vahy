package com.example.vahy.equation

import com.example.bakalarka.equation.Bracket

class Variable(protected var variable : String) : Polynom(){

    override fun simplify(): Polynom? = this

    override fun evaluate(variables : Map<String, Int>): Int =
        variables[variable] ?: 0

    override fun findAllVariables(): Set<String> = setOf(variable)

    override fun toString(): String = variable

    override fun equals(other: Any?): Boolean {
        if (!(other is Variable)) return false
        return variable == other.variable
    }

    override fun removeVariable(name: String): Polynom? =
        if (variable == name) this else null

    override fun addVariable(name: String): Polynom? =
        if (variable == name) this else null

    @JvmName("getVariable1")
    fun getVariable() : String = variable
}