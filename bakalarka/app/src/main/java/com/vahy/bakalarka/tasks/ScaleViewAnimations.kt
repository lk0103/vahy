package com.vahy.bakalarka.tasks

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.CountDownTimer
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.vahy.bakalarka.objects.menu.ConfettiGenerator
import com.vahy.bakalarka.objects.menu.ConfettiParticle
import com.vahy.bakalarka.objects.menu.FailSuccessIcon
import com.vahy.bakalarka.objects.menu.LevelNumber
import com.vahy.vahy.ScalesView
import com.vahy.vahy.objects.ScreenObject

class ScaleViewAnimations(private val context: Context, private val scalesView: ScalesView) {

    var messageResult : CountDownTimer? = null
    var messageNewLevel : CountDownTimer? = null

    private var confettiAnimator : ValueAnimator? = null
    private var particles : List<ConfettiParticle> = listOf()

    private var animationDuration: Long = 1300

    private var screenObjects : MutableList<ScreenObject> = mutableListOf()

    fun earthquakeAnimation(){
        scalesView.getScales().forEach { it.earthquakeAnimation(scalesView) }
    }

    fun failSuccessShow(success : Boolean){
        scalesView.screenTouchDisabled = true
        val icon = FailSuccessIcon(context, success)
        screenObjects.add(icon)
        icon.changeSizeInScaleView(scalesView.widthView, scalesView.heightView)
        scalesView.invalidate()

        if (success)
            startConfettiAnimation()
        else
            scalesView.getScales().forEach { it.earthquakeAnimation(scalesView) }

        messageResult = object : CountDownTimer(animationDuration, animationDuration){
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                cancelResultMessage()
            }
        }.start()
    }

    fun startConfettiAnimation() {
        val widthWithConfetti = scalesView.widthView / 15
        particles = ConfettiGenerator().generateConfetti(widthWithConfetti / 2,
            scalesView.widthView - widthWithConfetti)
        confettiAnimator = ValueAnimator.ofInt(0, particles.size - 1)
        confettiAnimator?.duration = animationDuration
        confettiAnimator?.interpolator = LinearInterpolator()
        confettiAnimator?.addUpdateListener {
            scalesView.invalidate()
        }
        confettiAnimator?.start()
        confettiAnimator?.doOnEnd {
            particles = listOf()
        }
    }

    fun unlockedLevel(){
        if (scalesView.showNewLevelMessage < 1 ||
            scalesView.showNewLevelMessage > NUM_LEVELS)
            return
        scalesView.screenTouchDisabled = true
        val icon = LevelNumber(context, scalesView.showNewLevelMessage)

        icon.showLock(true, 0)
        icon.changeSizeInScaleView(scalesView.widthView, scalesView.heightView)
        screenObjects.add(icon)
        scalesView.invalidate()

        val distance = scalesView.heightView / 3  + scalesView.heightView / 5
        val oneStep = distance / 50F
        val interval = 20L
        val time = (distance / oneStep) * interval * 2
        messageNewLevel = object : CountDownTimer(time.toLong(), interval){
            override fun onTick(p0: Long) {
                if (p0 > time - time / 2 ) {
                    icon.move(icon.x, icon.y + oneStep.toInt())
                    if (p0 < time - time / 4 ) {
                        icon.showLock(true, 1)
                    }
                    scalesView.invalidate()
                    return
                }

                icon.setLocked(false)
                icon.showLock(true, 2)
                scalesView.invalidate()
            }

            override fun onFinish() {
                cancelNewLevelMessage()
            }
        }.start()

        scalesView.showNewLevelMessage = -1
    }

    fun cancelShownResultMessage(){
        if (messageResult == null)
            return
        messageResult?.cancel()
        cancelResultMessage()
    }

    fun cancelResultMessage() {
        screenObjects.filter { it is FailSuccessIcon }.forEach { icon ->
            screenObjects.remove(icon)
        }
        scalesView.screenTouchDisabled = false
        scalesView.invalidate()
        messageResult = null

        confettiAnimator?.cancel()
        confettiAnimator = null
        particles = listOf()
    }

    fun cancelShownNewLevelMessage(){
        if (messageNewLevel == null)
            return
        messageNewLevel?.cancel()
        cancelNewLevelMessage()
    }

    fun cancelNewLevelMessage() {
        screenObjects.filter {it is LevelNumber }.forEach { icon ->
            screenObjects.remove(icon)
        }
        scalesView.screenTouchDisabled = false
        scalesView.invalidate()
        messageNewLevel = null
    }

    fun draw(canvas: Canvas?, paint: Paint) {
        particles.forEach { it.draw(canvas) }

        if (screenObjects.any { it is FailSuccessIcon || it is LevelNumber }) {
            drawMessage(canvas, paint)
        }
    }

    fun drawMessage(canvas: Canvas?, paint: Paint) {
        val p = paint.alpha
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.alpha = 100

        canvas?.drawRect(Rect(0, 0, scalesView.widthView, scalesView.heightView), paint)
        paint.alpha = p

        screenObjects.filter { it is FailSuccessIcon || it is LevelNumber }.forEach {
            it.draw(canvas!!, paint)
        }
    }
}