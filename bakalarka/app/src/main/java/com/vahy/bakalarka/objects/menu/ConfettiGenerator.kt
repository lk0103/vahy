package com.vahy.bakalarka.objects.menu

import android.graphics.Color
import kotlin.random.Random

class ConfettiGenerator {
    fun generateConfetti(xStart : Int, width : Int): MutableList<ConfettiParticle> {
        val colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA)
        val particleCount = 200
        val particleAngleRange = 60..120

        val particles = mutableListOf<ConfettiParticle>()
        repeat(particleCount) {
            val x = (0..width).random().toFloat() + xStart
            val y = (-100..-50).random().toFloat()
            val particleSizeX = Random.nextFloat() * 2f + 6f
            val particleSizeY = Random.nextFloat() * 15 + 30f

            val particleSpeed = Random.nextFloat() * 4f + 11f
            val color = colors.random()
            val angle = particleAngleRange.random().toDouble()

            val particle = ConfettiParticle(x, y, color, particleSizeX, particleSizeY, angle, particleSpeed)
            particles.add(particle)
        }

        return particles
    }
}