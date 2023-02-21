package com.example.bakalarka.objects

open class ScaleVariable(protected val value : Int,
                         dragFrom : Boolean = false,
                         dragTo : Boolean = false)
    : EquationObject(dragFrom, dragTo)  {

    override fun evaluate() : Int = value
}