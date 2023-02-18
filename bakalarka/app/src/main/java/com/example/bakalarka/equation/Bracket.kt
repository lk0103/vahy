package com.example.bakalarka.equation

import android.util.Log
import com.example.vahy.equation.Addition
import com.example.vahy.equation.Polynom

class Bracket(var polynom : Addition) : Polynom(){

    override fun evaluate(variables : Map<String, Double>): Double =
        polynom.evaluate(variables)

    override fun findAllVariables(): Set<String> = polynom.findAllVariables()

    override fun findAllBrackets(): Set<Bracket> = setOf(this)

    override fun containsBracket() : Boolean = true

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
}