package com.example.vahy.equation

class Constant(protected var constant : Double) : Polynom() {
    override fun valueAt(variable: String): Double? = null

    override fun evaluate(): Double = constant

    override fun toString(): String = constant.toString()

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
}