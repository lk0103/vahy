package com.example.vahy

open class ScaleValue(private val value : Int,
                      private var collidable : Boolean = false,
                      private var dragable : Boolean = true)
    : EquationObject(collidable, dragable)  {

    override fun evaluate() : Int = value

    fun doubleClick(){

    }

    fun longClick(){

    }
}