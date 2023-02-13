package com.example.vahy.equation

class Addition(var addends : MutableList<Polynom>) : Polynom(){

    override fun evaluate(): Double =
        addends.map { it.evaluate() }.sum()


    override fun valueAt(v: String): Double? =
        addends.map { it.valueAt(v) }.filter { it != null }.firstOrNull()


    override fun toString(): String =
        addends.map { it.toString() }.joinToString(separator = " + ")

    override fun addToConstant(fromValue: Double, value : Double) : Polynom?{
        for (polynom in addends.filter { it is Multiplication || it is Constant }){
            val added = polynom.addToConstant(fromValue, value)
            if (added != null) {
                if (polynom is Constant){
                    addends.remove(polynom)
                }
                if (added.evaluate() != 0.0) addends.add(added)
                return added
            }
        }
        return null
    }

    override fun addConstant(value: Double): Polynom? {
        for (polynom in addends.filter { it is Multiplication || it is Constant }){
            val added = polynom.addConstant(value)
            if (added != null) {
                if (polynom is Constant){
                    addends.remove(added)
                    addends.add(Multiplication(added, Constant(2.0)))
                }
                return added
            }
        }
        val added = Constant(value)
        addends.add(added)
        return added
    }

    override fun removeConstant(value: Double): Polynom? {
        for (polynom in addends.filter { it is Multiplication || it is Constant }){
            val added = polynom.removeConstant(value)
            if (added != null) {
                if (polynom is Constant){
                    addends.remove(added)
                }else if (polynom is Multiplication && polynom.getMultiple().evaluate() == 0.0){
                    addends.remove(polynom)
                }else if (polynom is Multiplication && polynom.getMultiple().evaluate() == 1.0){
                    addends.add(polynom.getPolynom())
                    addends.remove(polynom)
                }
                return added
            }
        }
        return null
    }

    override fun addVariable(name: String, v : Double): Polynom? {
        for (polynom in addends.filter { it is Multiplication || it is Variable }){
            val added = polynom.addVariable(name, v)
            if (added != null) {
                if (polynom is Variable){
                    addends.remove(added)
                    addends.add(Multiplication(added, Constant(2.0)))
                }
                return added
            }
        }
        val added = Variable(name, v)
        addends.add(added)
        return added
    }

    override fun removeVariable(name: String, v : Double): Polynom? {
        for (polynom in addends.filter { it is Multiplication || it is Variable }){
            val added = polynom.removeVariable(name, v)
            if (added != null) {
                if (polynom is Variable){
                    addends.remove(added)
                }else if (polynom is Multiplication && polynom.getMultiple().evaluate() == 0.0){
                    addends.remove(polynom)
                }else if (polynom is Multiplication && polynom.getMultiple().evaluate() == 1.0){
                    addends.add(polynom.getPolynom())
                    addends.remove(polynom)
                }
                return added
            }
        }
        return null
    }
}