package com.example.bakalarka.objects

import android.content.Context
import com.example.vahy.objects.ScreenObject

class Scale(context: Context, touchable : Boolean = true) :
    ScreenObject(touchable){
    private val leftHolder = HolderOfWeights(context, true)
    private val rightHolder = HolderOfWeights(context, false)
}