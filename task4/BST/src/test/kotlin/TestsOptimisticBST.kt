import kotlin.random.Random
import kotlinx.coroutines.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class TestsOptimisticBST{
    private val nodes: Int = 1000
    private val tree = OptimisticBST<Int>()
    private var listRandomNodes = (0..nodes).shuffled().take(nodes)

    @Test
    fun `one thread test`() = runBlocking {
        repeat(nodes) {
            tree.add(listRandomNodes[it])
        }

        repeat(nodes) {
            tree.delete(listRandomNodes[it])
        }

        for (key in listRandomNodes) {
            assertEquals(null, tree.search(key))
        }

    }

    @Test
    fun `parallel test`() = runBlocking {
        val headNodes = listRandomNodes.subList(0, listRandomNodes.size / 2)
        val tailNodes = listRandomNodes.subList(listRandomNodes.size / 2, listRandomNodes.size)
        coroutineScope {

            launch {
                delay(50)
                repeat(nodes/2) {
                    tree.add(headNodes[it])
                }
            }
            launch {
                delay(50)
                repeat(nodes/2) {
                    tree.add(tailNodes[it])
                }
            }

        }

        val nodesToDelete = listRandomNodes.shuffled(Random).take(nodes / 2)
        val headNodesToDelete = nodesToDelete.subList(0, nodesToDelete.size / 2)
        val tailNodesToDelete = nodesToDelete.subList(nodesToDelete.size / 2, nodesToDelete.size)

        coroutineScope {
            launch {
                delay(50)
                repeat(nodes / 4) {
                    tree.delete(headNodesToDelete[it])
                }
            }
            launch {
                delay(50)
                repeat(nodes / 4) {
                    tree.delete(tailNodesToDelete[it])
                }
            }
        }

        for (key in listRandomNodes) {
            if (key !in nodesToDelete) {
                assertEquals(key, tree.search(key))
            } else {
                assertEquals(null, tree.search(key))
            }
        }

    }
}