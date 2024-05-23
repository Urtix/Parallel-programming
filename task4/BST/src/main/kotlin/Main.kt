import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
    val bst = OptimisticBST<Int>()
    coroutineScope {
        launch {
            bst.add(1)
            bst.add(2)
            bst.add(0)
            bst.add(5)
            bst.add(4)
            bst.add(8)
            bst.delete(1)
        }
    }
    bst.printTree()

}