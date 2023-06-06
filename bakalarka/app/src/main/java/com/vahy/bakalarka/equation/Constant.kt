package com.vahy.vahy.equation


class Constant(protected var constant : Int) : Polynom() {

    override fun simplify(): Polynom? =
        if (constant == 0) null
        else this

    override fun evaluate(variables : Map<String, Int>): Int = constant

    override fun countNumConsValues(): MutableMap<Int, Int> =
        mutableMapOf(constant to 1)

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

    override fun addToConstant(fromValue: Int, value: Int): Polynom? =
        if (constant == fromValue) Constant(constant + value) else null

    override fun removeConstant(value: Int): Polynom? =
        if (constant == value) this else null

    override fun addConstant(value: Int): Polynom? =
        if (constant == value) this else null

    fun getValue() : Int = constant

    override fun copy(): Polynom {
        return Constant(constant)
    }
}