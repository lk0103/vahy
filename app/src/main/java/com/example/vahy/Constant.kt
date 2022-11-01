package com.example.vahy

class Constant(private var constant : Double) : Polynom() {
    override fun valueAt(variable: String): Double? = null

    override fun evaluate(): Double = constant

    override fun toString(): String = "$constant"


}