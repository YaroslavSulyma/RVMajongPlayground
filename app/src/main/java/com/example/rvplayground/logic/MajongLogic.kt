package com.example.rvplayground.logic

import kotlin.random.Random

class MajongLogic(val width: Int, val height: Int) {

    private val stones = mutableListOf<Int>()
    private val stateOpen = mutableListOf<Boolean>()
    private var actionStoneIdx: Int? = null

    init {
        assert(width == 4 && height == 4)

        for (i in 0 until width * height) {
            stones.add(i / 2)
            stateOpen.add(false)
        }
        shuffle()
    }

    fun toIndex(x: Int, y: Int): Int = y * width + x

    fun getStone(x: Int, y: Int): Int = stones[toIndex(x, y)]

    fun isStoneOpen(x: Int, y: Int): Boolean = stateOpen[toIndex(x, y)]

    fun haveWon(): Boolean {
        for (i in 0 until width * height) {
            if (!stateOpen[i])
                return false
        }

        return true
    }

    fun reset() {
        for (i in 0 until width * height) {
            stateOpen[i] = false
        }
        actionStoneIdx = null
    }

    fun action(x: Int, y: Int): Pair<Int, Int>? {
        var undoPair: Pair<Int, Int>? = null
        val idx = toIndex(x, y)
        assert(stateOpen[idx] == false)

        if (actionStoneIdx != null) {
            val oldStone = stones[actionStoneIdx!!]
            val newStone = stones[idx]

            // open it even if fail
            stateOpen[idx] = true
            if (newStone != oldStone) {
                // return pair for undo
                undoPair = Pair(actionStoneIdx!!, idx)
            }

            // reset action stone
            actionStoneIdx = null
        } else {
            actionStoneIdx = idx
            stateOpen[actionStoneIdx!!] = true
        }

        return undoPair
    }

    fun performUndo(undoPair: Pair<Int, Int>) {
        assert(stateOpen[undoPair.first] == false)
        assert(stateOpen[undoPair.second] == false)

        stateOpen[undoPair.first] = false
        stateOpen[undoPair.second] = false
    }

    fun shuffle() {
        val r = Random
        for (idx1 in 0 until width * height - 1) {
            val idx2 = idx1 + 1 + r.nextInt(width * height - (idx1 + 1))
            val t = stones[idx1]
            stones[idx1] = stones[idx2]
            stones[idx2] = t
        }
    }
}