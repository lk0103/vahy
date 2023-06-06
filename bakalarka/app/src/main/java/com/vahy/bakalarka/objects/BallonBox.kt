package com.vahy.bakalarka.objects

class BallonBox : EquationObjectBox() {
    init {
        maxNumberOfObj = 4
        cols = 2.0
        rows = 1.8
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        width = w
        height = h
        x = xStart
        y = yStart
        widthEqObj = ((h / rows) * 2 / 3).toInt()
        heightEqObj = (h / rows).toInt()

        calculatePositions()
        changeSizeObj()
    }


    override fun changeSizeObj() {
        (0 until insideObject.size).forEach{ i ->
            (insideObject[i] as (Ballon)).setImageParamForScale()
        }
        super.changeSizeObj()
    }


    override fun addObject(obj : EquationObject) {
        (obj as (Ballon)).setImageParamForScale()
        super.addObject(obj)
    }

    override fun calculatePositions() {
        val yBottom = y + height
        val yDelta = heightEqObj * 17 / 40
        val yDeltaFirst = heightEqObj / 4
        val xDelta = (width - 5 * (widthEqObj) / 2) / 2 + widthEqObj / 5
        val zSeq = mutableListOf(4, 3, 7, 6)

        positions = mutableListOf()
        (0..1).forEach { row ->
            var hMultiple = 1
            val hDivisor = 2
            var yShift = yDeltaFirst
            if (row >= 1){
                hMultiple = 3
                yShift += yDelta
            }
            (0..1).forEach { col ->
                var wMultiple = 3
                var wDivisor = 4
                if (col >= 1){
                    wMultiple = 21
                    wDivisor = 16
                }

                positions.add(listOf(
                    x + xDelta + widthEqObj * wMultiple / wDivisor,
                    yBottom - heightEqObj * hMultiple / hDivisor + yShift,
                    zSeq.get(0)
                ))
                zSeq.removeAt(0)
            }
        }
    }
}