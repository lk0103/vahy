package com.example.vahy.equation

abstract class Polynom {
    abstract fun valueAt(variable : String) : Double?
    abstract fun evaluate() : Double

    //odstrani sa x aj ked sa nezhoduje hodnota v
    open fun removeVariable(name: String, v : Double) : Polynom? = null

    //prida sa x aj ked sa nezhoduje hodnota v
    open fun addVariable(name: String, v : Double) : Polynom? = null

    open fun removeConstant(value : Double) : Polynom? = null

    open fun addConstant(value : Double) : Polynom? = null

    open fun addToConstant(fromValue: Double, value : Double) : Polynom? = null
}