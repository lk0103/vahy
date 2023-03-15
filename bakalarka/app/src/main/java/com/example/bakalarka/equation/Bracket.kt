package com.example.bakalarka.equation

import com.example.vahy.equation.Addition
import com.example.vahy.equation.Polynom

class Bracket(var polynom : Addition) : Polynom(){

    override fun simplify(): Polynom? {
        val simplified = polynom.simplify()
        if (simplified != null && simplified is Addition)
            polynom = simplified
        return this
    }

    override fun evaluate(variables : Map<String, Int>): Int =
        polynom.evaluate(variables)

    override fun countNumVariableTypes(): MutableMap<String, Int> {
        val numVarTypes = mutableMapOf<String, Int>()
        polynom.addends.forEach { pol ->
            pol.countNumVariableTypes().forEach{
                val newValue = (numVarTypes.getOrDefault(it.key, 0) + it.value)
                numVarTypes[it.key] = newValue
            }
        }
        return numVarTypes
    }

    override fun countNumConsValues(): MutableMap<Int, Int> {
        val numConsValuess = mutableMapOf<Int, Int>()
        polynom.addends.forEach { pol ->
            pol.countNumConsValues().forEach{
                val newValue = (numConsValuess.getOrDefault(it.key, 0) + it.value)
                numConsValuess[it.key] = newValue
            }
        }
        return numConsValuess
    }

    override fun countNumBrackets(): MutableMap<Bracket, Int> =
        mutableMapOf(this to 1)

    override fun setAllBracketInsides(bracketInside: Addition) {
        polynom = bracketInside.copy() as Addition
    }

    override fun toString(): String =
        "(" + polynom.toString() + ")"

    override fun equals(other: Any?): Boolean {
        if (!(other is Bracket)) return false
        return other.polynom == polynom
    }

    override fun addBracket(bracket: Bracket): Polynom? =
        if (this == bracket) this else null

    override fun removeBracket(bracket: Bracket): Polynom? =
        if (this == bracket) this else null

    override fun copy(): Polynom {
        return Bracket(polynom.copy() as Addition)
    }
}