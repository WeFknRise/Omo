package com.ribmouth.game.ui

/**
 * Created by RibMouth on 31/10/2017.
 *
 * The open annotation on a class is the opposite of Java's final: it allows others to inherit from this class.
 * By default, all classes in Kotlin are final
 */
open class Box(x: Float, y: Float, width: Float, height: Float) {
    protected var x: Float = x
    protected var y: Float = y
    protected var width: Float = width
    protected var height: Float = height

    fun contains(x: Float, y: Float): Boolean {
        return x > this.x - width / 2 &&
                x < this.x + width / 2 &&
                y > this.y - height / 2 &&
                y < this.y + height / 2
    }
}