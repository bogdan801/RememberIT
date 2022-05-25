package com.bogdan801.rememberit

import com.bogdan801.rememberit.presentation.windows.util.UndoRedoStack
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    /**
     * Тест на перевірку функції undo в стаку
     */
    @Test
    fun undoTest() {
        val undoRedoStack = UndoRedoStack<Int>()
        undoRedoStack.pushValue(0)
        undoRedoStack.pushValue(1)
        undoRedoStack.pushValue(2)

        assertEquals(undoRedoStack.undo(), 1)
    }

    /**
     * Тест на перевірку функції redo в стаку
     */
    @Test
    fun redoTest() {
        val undoRedoStack = UndoRedoStack<Int>()
        undoRedoStack.pushValue(0)
        undoRedoStack.pushValue(1)
        undoRedoStack.pushValue(2)
        undoRedoStack.undo()

        assertEquals(undoRedoStack.redo(), 2)
    }

    /**
     * Тест на перевірку можливості виконання undo функції
     */
    @Test
    fun isUndoPossibleTest() {
        val undoRedoStack = UndoRedoStack<Int>()
        undoRedoStack.pushValue(0)
        undoRedoStack.pushValue(1)

        assert(undoRedoStack.isUndoActive)
    }

    /**
     * Тест на перевірку можливості виконання redo функції
     */
    @Test
    fun isRedoPossibleTest() {
        val undoRedoStack = UndoRedoStack<Int>()
        undoRedoStack.pushValue(0)
        undoRedoStack.pushValue(1)
        undoRedoStack.undo()

        assert(undoRedoStack.isRedoActive)
    }
}