package com.example.vahy.equation

import com.example.bakalarka.equation.Bracket

//lava strana bude variable, constant, bracked a prava strana bude nasobok
class Multiplication(private var polynom : Polynom,
                     private var multiple : Constant
) : Polynom(){

    fun getMultiple() : Constant = multiple

    fun getPolynom() : Polynom = polynom

    override fun evaluate(): Double =
        polynom.evaluate() * multiple.evaluate()


    override fun valueAt(v: String): Double? {
        val leftValueAt = polynom.valueAt(v)
        if (polynom.valueAt(v) != null) return leftValueAt

        val rightValueAt = multiple.valueAt(v)
        if (multiple.valueAt(v) != null) return rightValueAt

        return null
    }


    override fun toString(): String = multiple.toString() + "*" + polynom.toString()
//        if (polynom is Constant && polynom.evaluate() == 1.0)
//            multiple.toString()
//        else if (multiple.evaluate() == 1.0)
//            polynom.toString()
//        else multiple.toString() + "*" + polynom.toString()


    override fun addToConstant(fromValue: Double, value : Double) : Polynom? {
        if (polynom is Bracket) return null
        val added = polynom.addToConstant(fromValue, value)
        if (added != null){
            multiple.decrement()
        }
        return added
    }

    override fun addConstant(value: Double): Polynom? {
        if (polynom is Bracket) return null
        val added = polynom.addConstant(value)
        if (added != null){
            multiple.increment()
        }
        return added
    }

    override fun removeConstant(value: Double): Polynom? {
        if (polynom is Bracket) return null
        val added = polynom.removeConstant(value)
        if (added != null){
            multiple.decrement()
        }
        return added
    }

    override fun addVariable(name: String, v : Double): Polynom? {
        if (polynom is Bracket) return null
        val added = polynom.addVariable(name, v)
        if (added != null){
            multiple.increment()
        }
        return added
    }

    override fun removeVariable(name: String, v : Double): Polynom? {
        if (polynom is Bracket) return null
        val added = polynom.removeVariable(name, v)
        if (added != null){
            multiple.decrement()
        }
        return added
    }

}