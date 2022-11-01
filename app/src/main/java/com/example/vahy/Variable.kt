package com.example.vahy

class Variable(private var variable : String,
               private var value : Double) : Polynom(){

    override fun evaluate(): Double = value


    override fun valueAt(v: String): Double? =
        if (v == variable ) value
        else null


    override fun toString(): String = "$variable"
}