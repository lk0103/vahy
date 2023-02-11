package com.example.bakalarka.objects

open class ScaleVariable(protected val value : Int,
                         touchable : Boolean = false,
                         draggable : Boolean = true)
    : EquationObject(touchable, draggable)  {

    override fun evaluate() : Int = value
}