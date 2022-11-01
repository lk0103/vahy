package com.example.vahy

open class ScaleVariable(private val value : Int,
                    private var collidable : Boolean = false,
                    private var dragable : Boolean = true)
    : EquationObject(collidable, dragable)  {

    override fun evaluate() : Int = value
}