package com.example.bakalarka.objects

class CubeBox: EquationObjectBox() {

    init {
        maxNumberOfObj = 8
        cols = 2.2
        rows = 3.0
    }

    override fun calculatePositions() {
        val yBottom = y + height
        val yDelta = heightEqObj / 3
        val yDeltaFirst = heightEqObj / 5
        val xDelta = (width - 5 * (widthEqObj) / 2) / 2
        val zSeq = mutableListOf(10, 9, 8, 7, 6, 5, 4, 3)

        positions = mutableListOf()
        var hMultiple = -1
        val hDivisor = 2
        var yShift = 0
        (0..3).forEach { row ->
            hMultiple += 2
            yShift = yDeltaFirst + row * yDelta
            (0..1).forEach { col ->
                var wMultiple = 7
                val wDivisor = 4
                if (col >= 1){
                    wMultiple = 3
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