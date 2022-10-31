package com.example.vahy

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class ScalesView(context: Context, attrs: AttributeSet)
            : View(context, attrs) {
    val screenObjects = mutableListOf<ScreenObject>()
    private var draggedObject : EquationObject? = null

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    fun draw(){

    }

    //prejde vsetky objekty a draggovanemu objektu posle
    //objekty s ktorymi mal koliziu
    fun collision(){

    }
}