import stack.treiberStackWithElimination.TreiberStackWithElimination
import java.lang.Thread.sleep
fun println (stack: TreiberStackWithElimination<Int>) {
    while (stack.top() != null) {
        print("${stack.top()} ")
        stack.pop()
    }
}
fun main() {
    val stack = TreiberStackWithElimination<Int>()

    Thread {
        stack.push(1)
    }.start()

    Thread {
        stack.push(2)
    }.start()

    Thread {
        stack.push(3)
    }.start()

    sleep(5000)
    println(stack)
}