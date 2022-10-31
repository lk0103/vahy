package com.example.vahy

import android.graphics.Bitmap

open class Variable(private val value : Int,
                    private var collidable : Boolean = false,
                    private var dragable : Boolean = true)
    : EquationObject(collidable, dragable)  {

    override fun evaluate() : Int = value
}