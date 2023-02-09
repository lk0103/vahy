package com.example.bakalarka.objects

class PackageBox: EquationObjectBox() {
    init {
        maxNumberOfObj = 8
        cols = 2.0
        rows = 3.0
    }

    override fun calculatePositions() {
        val yBottom = y + height
        val yDelta = heightEqObj * 19 / 40
        val yDeltaFirst = heightEqObj / 5
        val xShift = widthEqObj / 20
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
                var wMultiple = 11
                var wDivisor = 16
                if (col >= 1){
                    wMultiple = 7
                    wDivisor = 4
                }

                positions.add(listOf(
                    x + xDelta + widthEqObj * wMultiple / wDivisor + xShift * (row % 2),
                    yBottom - heightEqObj * hMultiple / hDivisor + yShift,
                    zSeq.get(0)
                ))
                zSeq.removeAt(0)
            }
        }
    }

    override fun returnListInsideVariableTypes() : List<EquationObject> =
        insideObject.flatMap { (it as Package).insideObject }
            .filter { it is ScaleVariable}.toList()


}