package com.bogdan801.rememberit.presentation.windows.util

/**
 * This is the UndoRedoStack, is being used to save field value and to make [undo]/[redo] feature for this field possible
 * @param T type of the data to save
 * @property stack list of the saved data
 * @property currentIndex pointer to the element to show
 * @property isUndoActive show if the undo action is possible
 * @property isRedoActive show if the redo action is possible
 * @property size size of the stack
 */
class UndoRedoStack<T> {
    private val stack: MutableList<T> = mutableListOf()
    private var currentIndex: Int = -1

    val isUndoActive
        get() = currentIndex>0

    val isRedoActive
        get() = stack.lastIndex > currentIndex

    val size get() = stack.size

    /**
     * This is [pushValue] method, it is being used to add new value on top fo the stack.
     * @param value current value to add to a stack
     * @return added value
     */
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

    /**
     * This is [pushDefault] method, it is used to add default value to a stack
     * @param value the value to add
     * @return instance of the class UndoRedoStack with default value inside
     */
    fun pushDefault(value: T): UndoRedoStack<T>{
        pushValue(value)
        return this
    }

    /**
     * This is an [undo] method. It returns the previous value in the stack
     * @return previous value in the stack
     */
    fun undo(): T?{
        return if(isUndoActive) {
            currentIndex--
            stack[currentIndex]
        } else null
    }

    /**
     * This is an [redo] method. It returns next value in the stack
     * @return next value in the stack
     */
    fun redo(): T?{
        return if(isRedoActive) {
            currentIndex++
            stack[currentIndex]
        } else null
    }

    /**
     * This is a method for describing this class with a string
     * @return string class description
     */
    override fun toString(): String {
        return buildString {
            append("Size: ${stack.size};\t")
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