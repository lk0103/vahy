package com.example.bakalarka.objects

open class ScaleVariable(private val value : Int,
                         touchable : Boolean = false,
                         draggable : Boolean = true)
    : EquationObject(touchable, draggable)  {

    override fun evaluate() : Int = value
}