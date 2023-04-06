package com.example.bakalarka.objects.menu

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import kotlin.random.Random

class ConfettiParticle(
    private val x: Float,
    private val y: Float,
    private val color: Int,
    private val sizeX: Float,
    private val sizeY: Float,
    private val angle: Double,
    private val speed: Float
) {
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        color = this@ConfettiParticle.color
    }

    private val velocity = PointF(speed * Math.cos(angle).toFloat(),
                                speed * Math.sin(angle).toFloat())
    private val gravity = PointF(0f, 0.1f)
    private var rotation = (0..359).random().toFloat()

    var bounds = RectF(x, y, x + sizeX, y + sizeY)
    private var shape = "R"

    init {
        if (Random.nextInt(0, 2) < 1){
            shape = "C"
            bounds = RectF(x, y, x + sizeX * 2, y + sizeX * 2)
        }
    }

    fun draw(canvas: Canvas?) {
        if (shape == "R") {
            canvas?.save()
            canvas?.rotate(rotation, bounds.centerX(), bounds.centerY())
            canvas?.drawRect(bounds, paint)
            canvas?.restore()
        }else
            canvas?.drawOval(bounds, paint)
        move()
    }

    private fun move() {
        bounds.offset(velocity.x, velocity.y)
        velocity.offset(gravity.x, gravity.y)
        rotation += 3f
    }
}