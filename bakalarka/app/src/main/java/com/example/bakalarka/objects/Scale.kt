package com.example.bakalarka.objects

import android.content.Context
import com.example.vahy.objects.ScreenObject

class Scale(context: Context, dragFrom : Boolean = true, dragTo : Boolean = true) :
    ScreenObject(dragFrom, dragTo){
    private val leftHolder = HolderOfWeights(context, true)
    private val rightHolder = HolderOfWeights(context, false)
}