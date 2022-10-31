package com.example.vahy

import android.graphics.Bitmap

class holderOfWeights(private val left : Boolean,
                      private val collidable : Boolean = true)
    : ScreenObject(collidable) {
    private val equationObject = mutableListOf<EquationObject>()

    init {
        //calculate x, y, z, width, height, image
    }

    fun move(){

    }

    fun addEquationObject(obj : EquationObject){

    }

    fun organizeObjects(){

    }
}