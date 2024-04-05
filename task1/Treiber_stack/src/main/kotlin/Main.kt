import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import stack.treiberStack.TreiberStack
import stack.treiberStackWithElimination.TreiberStackWithElimination
import java.lang.Thread.sleep
import kotlin.coroutines.coroutineContext
import kotlin.system.measureNanoTime

fun main() = runBlocking {
    val stack = TreiberStack<Int>()
    var srTS = 0L
    var srTSWE = 0L
    repeat(100) {
        srTS += testTS()
    }
    srTS /= 100

    repeat(100) {
        srTSWE += testTSWE()
    }
    srTSWE /= 100
    println(srTS - srTSWE)

}

suspend fun testTS (): Long = coroutineScope{
    val stack = TreiberStack<Int>()

    val time = measureNanoTime {
        launch {
            for (i in 0..10_000) {
                stack.push((0..100).random())
            }
        }

        launch {
            for (i in 0 until 10_000) {
                stack.pop()
            }
        }
    }

    return@coroutineScope time
}

suspend fun testTSWE (): Long = coroutineScope {
    val stack = TreiberStackWithElimination<Int>()

    val time = measureNanoTime {
        launch {
            for (i in 0..10_000) {
                stack.push((0..100).random())
            }
        }

        launch {
            for (i in 0 until 10_000) {
                stack.pop()
            }
        }
    }

    return@coroutineScope time
}

fun println (stack: TreiberStackWithElimination<Int>) {
    while (stack.top() != null) {
        print("${stack.top()} ")
        stack.pop()
    }
}