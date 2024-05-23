import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import stack.treiberStack.TreiberStack
import stack.treiberStackWithElimination.TreiberStackWithElimination
import java.io.File
import kotlin.random.Random
import kotlin.system.measureNanoTime

fun main() = runBlocking {

    val stack = TreiberStack<Int>()
    var srTS = 0L
    var srTSWE = 0L
    var res = 0f
    repeat(100) {
        srTS += testTS()
    }
    srTS /= 100

    repeat(100) {
        srTSWE += testTSWE()
    }
    srTSWE /= 100

    res = srTS.toFloat() / srTSWE.toFloat()
    println(res)

    val file = File("random.txt")
    file.appendText("\n$res")


}

suspend fun testTS (): Long = coroutineScope{
    val stack = TreiberStack<Int>()

    val time = measureNanoTime {
        launch {
            for (i in 0..10_000) {
                if (Random.nextBoolean()) {
                    stack.push(i) // Срабатывает операция push
                } else {
                    stack.pop() // Срабатывает операция pop
                }
            }
        }
        launch {
            for (i in 0..10_000) {
                if (Random.nextBoolean()) {
                    stack.push(i) // Срабатывает операция push
                } else {
                    stack.pop() // Срабатывает операция pop
                }
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
                if (Random.nextBoolean()) {
                    stack.push(i) // Срабатывает операция push
                } else {
                    stack.pop() // Срабатывает операция pop
                }
            }
        }
        launch {
            for (i in 0..10_000) {
                if (Random.nextBoolean()) {
                    stack.push(i) // Срабатывает операция push
                } else {
                    stack.pop() // Срабатывает операция pop
                }
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