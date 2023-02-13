package com.example.bakalarka.equation

import com.example.vahy.equation.Polynom

class Bracket(private var polynom : Polynom) : Polynom(){

    override fun evaluate(): Double =
        polynom.evaluate()


    override fun valueAt(v: String): Double? =
        polynom.valueAt(v)


    override fun toString(): String =
        "(" + polynom.toString() + ")"

}