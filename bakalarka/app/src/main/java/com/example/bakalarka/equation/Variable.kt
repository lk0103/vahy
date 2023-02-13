package com.example.vahy.equation

class Variable(protected var variable : String,
               protected var value : Double) : Polynom(){

    override fun evaluate(): Double = value

    override fun valueAt(v: String): Double? =
        if (v == variable) value
        else null

    override fun toString(): String = variable

    override fun removeVariable(name: String, v : Double): Polynom? =
        if (variable == name) this else null

    override fun addVariable(name: String, v : Double): Polynom? =
        if (variable == name) this else null
}