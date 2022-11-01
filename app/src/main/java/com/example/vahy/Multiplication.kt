package com.example.vahy

class Multiplication(private var left : Polynom,
                     private var right : Polynom) : Polynom(){

    override fun evaluate(): Double =
        left.evaluate() * right.evaluate()


    override fun valueAt(v: String): Double? {
        val leftValueAt = left.valueAt(v)
        if (left.valueAt(v) != null) return leftValueAt

        val rightValueAt = right.valueAt(v)
        if (right.valueAt(v) != null) return rightValueAt

        return null
    }


    override fun toString(): String =
        if (left is Constant && left.evaluate() == 0.0)
            right.toString()
        else if (right is Constant && right.evaluate() == 0.0)
            left.toString()
        else left.toString() + "*" + right.toString()

}