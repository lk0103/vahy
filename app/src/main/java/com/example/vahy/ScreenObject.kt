package com.example.vahy

import android.graphics.Bitmap
import android.graphics.Canvas

open class ScreenObject(private var collidable : Boolean){
    private var x = 0
    private var y = 0
    private var z = 0
    private var width = 0
    private var height = 0
    private lateinit var image : Bitmap


    fun draw(canvas: Canvas){

    }

    fun resizeBitmap(){

    }

    /*
    open fun collision(obj : ScreenObject){

    }

     */

    fun isIn(obj : ScreenObject) : Boolean{
        return false
    }
}