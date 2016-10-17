package ru.nobirds.k2048

import kotlin.support.AbstractIterator
import java.util.ArrayList

public class Matrix(val size: Int, val builder: (Int, Int) -> Int) {

    private val matrix = Array<IntArray> (size) { x -> intArrayFor(size) { y -> builder(x, y) } }

    public val indices:IntRange = 0..size-1

    public val invertIndices: IntProgression = size - 1 downTo 0

    public fun column(index:Int): Vector = ColVector(this, index)

    public fun row(index:Int): Vector = RowVector(this, index)

    public fun eachRow(block: (Vector) -> Unit) {
        indices.forEach {
            block(row(it))
        }
    }

    public fun eachColumn(block: (Vector)->Unit) {
        indices.forEach {
            block(column(it))
        }
    }

    operator fun get(x: Int, y: Int): Int = matrix[x][y]

    operator fun set(x: Int, y: Int, value: Int) { matrix[x][y] = value }

    public fun copy(): Matrix = Matrix(size) { x, y -> this[x, y] }

    public fun setAll(builder: (Int, Int) -> Int) {
        indices.forEach { x ->
            indices.forEach { y ->
                set(x, y, builder(x, y))
            }
        }
    }

    public fun copyFrom(matrix:Matrix) {
        if(size != matrix.size)
            throw IllegalArgumentException("Matrices sizes is defferent ${size} and ${matrix.size}")

        setAll { x, y -> matrix[x, y] }
    }

    public fun allIndices(predicate:(Int, Int)->Boolean):Boolean =
            indices.all { x ->
                indices.all { y -> predicate(x, y) }
            }

    public fun anyIndex(predicate:(Int, Int)->Boolean):Boolean =
            indices.any { x ->
                indices.any { y -> predicate(x, y) }
            }

    public fun findAllPositions(predicate:(Int)->Boolean):List<Pair<Int, Int>> {
        val result = ArrayList<Pair<Int, Int>>()

        indices.forEach { x ->
            indices.forEach { y ->
                if(predicate(get(x, y)))
                    result.add(Pair(x, y))
            }
        }

        return result
    }

    override operator fun equals(other: Any?) = other is Matrix && this[x, y] == other[x, y]

    override fun toString() = StringBuilder().apply {
        eachRow { row ->
            append("\n")
            row.forEach { append("$it ") }
        }

        append("\n")
    }.toString()
}

