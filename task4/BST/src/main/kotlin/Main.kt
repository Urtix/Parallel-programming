import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val tree = OptimisticBST<Int>()

    tree.add(1)
    tree.add(3)
    tree.add(2)
    tree.add(0)
    tree.delete(1)


    tree.printTree()

}