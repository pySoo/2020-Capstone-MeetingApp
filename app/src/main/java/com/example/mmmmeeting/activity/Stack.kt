package com.example.mmmmeeting.activity

//generic stack class with the added functionality of peeking the second to top element.
// hacky way of typing the array, so don't return the stack array ever
class Stack<T> constructor(maxSize: Int) {
    private val stack: Array<T?>
    private var size: Int
    fun push(obj: T): Boolean {
        if (size >= stack.size) return false
        stack.get(size) = obj
        size++
        return true
    }

    fun pop(): T? {
        if (isEmpty) throw IndexOutOfBoundsException()
        size--
        val out: T? = stack.get(size)
        stack.get(size) = null
        return out
    }

    fun peek(): T? {
        return stack.get(size - 1)
    }

    //peek the element directly under the topmost
    fun peekUnder(): T? {
        return stack.get(size - 2)
    }

    operator fun get(index: Int): T? {
        if (index < 0 || index >= size) throw IndexOutOfBoundsException()
        return stack.get(index)
    }

    fun size(): Int {
        return size
    }

    val isEmpty: Boolean
        get() {
            return size == 0
        }

    init {
        stack = arrayOfNulls<Any>(maxSize) as Array<T?>
        size = 0
    }
}