package com.example.mmmmeeting.activity

object GrahamScan {
    //finds the convex hull of a set of points
    fun convexHull(points: Array<Point?>): Array<Point?> {
        var points: Array<Point?> = points
        val hull: Array<Point?>

        //find the lowest point by y value
        // and sort the points by angle (P, P_lowest, x_axis)
        val lowest: Point? = findLowestPoint(points)
        for (point: Point? in points) point!!.setAngle(lowest)
        sortByAngle(points, lowest)

        //degenerate case: remove co-linear points
        points = removeCollinear(points, lowest)

        //convex hull of less than 3 points is itself
        if (points.size < 3) {
            hull = arrayOfNulls(points.size)
            for (i in points.indices) hull.get(i) = points.get(i)
            return hull
        }

        //create processing stack with first 3 points
        val stack: Stack<Point> = Stack(points.size)
        var index: Int = 0
        while (stack.size() < 3) {
            if (points.get(index) != null) stack.push(points.get(index)!!)
            index++
        }

        //process the other points
        for (i in index until points.size) {
            var p1: Point? = stack.peekUnder()
            var p2: Point? = stack.peek()
            val p3: Point? = points.get(i)
            var ccw: Boolean = Point.Companion.orientation(p1, p2, p3)
            while (!ccw) {
                stack.pop()
                p1 = stack.peekUnder()
                p2 = stack.peek()
                ccw = Point.Companion.orientation(p1, p2, p3)
            }
            stack.push((points.get(i))!!)
        }

        //read the stack
        hull = arrayOfNulls(stack.size())
        for (i in hull.indices) hull.get(i) = stack.get(i)
        return hull
    }

    //linear scan to find lowest y coordinate, ties broken by lower x coordinate
    fun findLowestPoint(points: Array<Point?>): Point? {
        var lowest: Point? = points.get(0)
        for (p: Point? in points) {
            if ((p!!.y < lowest!!.y) || ((p.y == lowest.y) && p.x < lowest.x)) lowest = p
        }
        return lowest
    }

    //given a point p and a set of points P, heap sort P by angle to p
    fun sortByAngle(points: Array<Point?>, p: Point?) {
        for (i in points.size / 2 - 1 downTo 0) angleHeapify(points, points.size, i)
        for (i in points.indices.reversed()) {
            val temp: Point? = points.get(0)
            points.get(0) = points.get(i)
            points.get(i) = temp
            angleHeapify(points, i, 0)
        }
    }

    //rebalance the points in the heap by their angles
    fun angleHeapify(points: Array<Point?>, size: Int, index: Int) {
        var biggest: Int = index
        val left: Int = 2 * index + 1
        val right: Int = 2 * index + 2
        if (left < size && points.get(left)!!.angle > points.get(biggest)!!.angle) biggest = left
        if (right < size && points.get(right)!!.angle > points.get(biggest)!!.angle) biggest = right
        if (biggest != index) {
            val temp: Point? = points.get(index)
            points.get(index) = points.get(biggest)
            points.get(biggest) = temp
            angleHeapify(points, size, biggest)
        }
    }

    //if there are multiple co-linear points (P_lowest, P_i, ..., P_n), only keep the furthest one
    fun removeCollinear(points: Array<Point?>, p: Point?): Array<Point?> {
        var numRemoved: Int = 0
        var currAngleInd: Int = points.size - 1
        for (i in points.indices.reversed()) {
            if (points.get(i) === p) continue
            if (points.get(i)!!.angle == points.get(currAngleInd)!!.angle && i != currAngleInd) {
                val p1: Point? = points.get(currAngleInd)
                val p2: Point? = points.get(i)
                if (p2!!.distanceTo(p) >= p1!!.distanceTo(p)) {
                    points.get(currAngleInd) = null
                    currAngleInd = i
                } else {
                    points.get(i) = null
                }
                numRemoved++
            } else currAngleInd = i
        }
        var index: Int = 0
        val newPoints: Array<Point?> = arrayOfNulls(points.size - numRemoved)
        for (i in newPoints.indices) {
            while (points.get(index) == null) index++
            newPoints.get(i) = points.get(index)
            index++
        }
        return newPoints
    }
}