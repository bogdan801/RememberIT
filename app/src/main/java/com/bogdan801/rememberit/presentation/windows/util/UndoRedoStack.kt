package com.bogdan801.rememberit.presentation.windows.util

/**
 * Це клас UndoRedoStack, використовується для зберення даних поля і реалізації [undo]/[redo] функціоналу для даного поля
 * @param T тип даних для збереження
 * @property stack список для збереження внесених даних
 * @property currentIndex вказівник на елемент для відображення
 * @property isUndoActive вказує чи можна дозволяти виконувати undo
 * @property isRedoActive вказує чи можна дозволяти виконувати redo
 * @property size вказує на розмір стаку
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
     * Це метод [pushValue], використовується для додавання нових значень на верх стаку.
     * Якщо вказівник [currentIndex] не на останньому індексі то всі значення після нього стираються і нове значення додається на їх місце
     * @param value значення що потрібно додати в стак
     * @return додане в стак значення
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
     * Це метод [pushDefault], використовується для додавання значення за замовчуванням в стак
     * @param value значення за замовчуванням для додавання в стак
     * @return екземпляр класу UndoRedoStack з доданим значенням за замовчуванням
     */
    fun pushDefault(value: T): UndoRedoStack<T>{
        pushValue(value)
        return this
    }

    /**
     * Це метод [undo]. Повертає попереднє значення в стаку
     * @return попереднє значення в стаку
     */
    fun undo(): T?{
        return if(isUndoActive) {
            currentIndex--
            stack[currentIndex]
        } else null
    }

    /**
     * Це метод [redo]. Повертає наступне значення в стаку
     * @return наступне значення в стаку
     */
    fun redo(): T?{
        return if(isRedoActive) {
            currentIndex++
            stack[currentIndex]
        } else null
    }

    /**
     * Це метод для опису даного класу строкою
     * @return строковий опис даного класу
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