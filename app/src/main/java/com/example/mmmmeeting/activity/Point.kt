package com.example.mmmmeeting.activity

import com.google.android.gms.maps.model.LatLng

class Point constructor(var x: Double, var y: Double) {
    var angle: Double = 0.0

    //angle of this point with respect to p
    fun setAngle(p: Point?) {
        angle = Math.toDegrees(Math.atan2(y - p!!.y, x - p.x))
        if (angle < 0) angle += 360.0
    }

    //distance from this point to the other
    fun distanceTo(other: Point?): Double {
        val xDiff: Double = Math.abs(x - other!!.x)
        val yDiff: Double = Math.abs(y - other.y)
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff)
    }

    public override fun toString(): String {
        return "[" + x + "," + y + "]"
    }

    public override fun equals(obj: Any?): Boolean {
        if (obj is Point) {
            val o: Point = obj
            return x == o.x && y == o.y
        }
        return false
    }

    fun getposition(): LatLng {
        return LatLng(x, y)
    }

    companion object {
        //returns true for ccw, false for cw
        fun orientation(p1: Point?, p2: Point?, p3: Point?): Boolean {
            return (p2!!.y - p1!!.y) * (p3!!.x - p2.x) - (p2.x - p1.x) * (p3.y - p2.y) < 0
        }
    }
}