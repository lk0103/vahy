package com.example.vahy.objects

import com.example.bakalarka.objects.EquationObject
import com.example.bakalarka.objects.ScaleValue
import com.example.bakalarka.objects.ScaleVariable

class OpenPackage(private var collidable : Boolean = true)
    : ScreenObject(collidable) {

    private var insideObject = mutableListOf<EquationObject>()

    init {
        //calculate x, y, z, width, height, image
    }

    fun addObjectIn(obj : EquationObject){
        if (obj is ScaleValue || obj is ScaleVariable)
            insideObject.add(obj)
    }
}