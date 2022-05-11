package com.bogdan801.rememberit.presentation.windows.util

class UndoRedoStack<T>(private val limit: Int = 0) {
    private val stack: MutableList<T> = mutableListOf()
    private var currentIndex: Int = -1

    val isUndoActive
        get() = currentIndex>limit

    val isRedoActive
        get() = stack.lastIndex > currentIndex

    val isEmpty
        get() = stack.isEmpty()

    val isNotEmpty
        get() = stack.isNotEmpty()

    val size get() = stack.size

    fun pushValue(value: T): T{
        if (stack.isNotEmpty() && currentIndex < stack.lastIndex){
            for (i in 0 until stack.lastIndex-currentIndex){
                stack.removeAt(currentIndex+1)
            }
        }
        stack.add(value)
        currentIndex++
        return stack[currentIndex]
    }

    fun pushDefault(value: T): UndoRedoStack<T>{
        pushValue(value)
        return this
    }

    fun topValue() = stack.last()

    fun undo(): T?{
        return if(isUndoActive){
            currentIndex--
            val element = if(stack.size>1) stack[currentIndex] else null
            element
        } else null
    }
    fun getTopAndUndo(): T{
        val top = topValue()
        undo()
        return top
    }

    fun redo(): T?{
        return if(currentIndex < stack.lastIndex){
            currentIndex++
            val element = stack[currentIndex]
            element
        } else null
    }

    override fun toString(): String {
        return buildString {
            append("Size: $size;\t")
            append("Current index: $currentIndex;\t")
            append("Can undo: $isUndoActive;\t")
            append("Can redo: $isRedoActive;\t")
            append("Contents: (")
            stack.forEach{
                append("$it ")
            }
            append(")")
        }
    }
}