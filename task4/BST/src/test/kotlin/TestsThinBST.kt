import kotlin.random.Random
import kotlinx.coroutines.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class TestsThinBST{
    private val nodes: Int = 500
    private fun sleepTime() = Random.nextLong(100)

    private val tree = ThinBST<Int>()
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
            repeat(nodes/2) {
                launch {
                    delay(sleepTime())
                    tree.add(headNodes[it])
                }
                launch {
                    delay(sleepTime())
                    tree.add(tailNodes[it])
                }
            }
        }

        val nodesToDelete = listRandomNodes.shuffled(Random).take(nodes / 2)

        coroutineScope {
            repeat(nodes / 2) {
                launch {
                    delay(sleepTime())
                    tree.delete(nodesToDelete[it])
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