package com.example.vahy.equation

import com.example.bakalarka.equation.Bracket

class Constant(protected var constant : Double) : Polynom() {
    override fun evaluate(variables : Map<String, Double>): Double = constant

    override fun toString(): String = constant.toString()

    override fun equals(other: Any?): Boolean {
        if (!(other is Constant)) return false
        return constant == other.constant
    }

    fun increment() {
        constant++
    }

    fun decrement() {
        constant--
    }

    override fun addToConstant(fromValue: Double, value: Double): Polynom? =
        if (constant == fromValue) Constant(constant + value) else null

    override fun removeConstant(value: Double): Polynom? =
        if (constant == value) this else null

    override fun addConstant(value: Double): Polynom? =
        if (constant == value) this else null

    fun getValue() : Double = constant
}