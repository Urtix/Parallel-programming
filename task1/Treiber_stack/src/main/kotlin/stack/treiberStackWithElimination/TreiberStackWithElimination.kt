package stack.treiberStackWithElimination

import Node.Node
import stack.Stack
import kotlinx.atomicfu.atomicArrayOfNulls
import java.util.concurrent.atomic.AtomicReference

private const val SIZE = 10

class TreiberStackWithElimination<T : Any>: Stack<T> {

    private enum class Done {DONE}
    private val done = Done.DONE

    private val H = AtomicReference(Node<T>(null, null))

    private val eliminationArray = atomicArrayOfNulls<Any?>(SIZE)

    override fun pop(): Any? {
        for (i in 0 until SIZE) {
            val cur = eliminationArray[i].value
            if (cur == null || cur is Done) {
                continue
            }
            if (eliminationArray[i].compareAndSet(cur, done)) {
                return (cur as Node<*>).value
            }
        }

        while (true) {
            val head = H.get() ?: return null
            if (H.compareAndSet(head, head.next)) {
                return head.value
            }
        }
    }

    override fun push(value: T) {
        if (putInArrayAndWait(value)) {
            while (true) {
                val head = H.get()
                val newHead = Node(value, head)
                if (H.compareAndSet(head, newHead)) {
                    return
                }
            }
        }
    }

    private fun putInArrayAndWait(value: T): Boolean {
        val nodeToPut: Node<T> = Node(value, null)
        var index: Int = -1
        var waitingTime = 10
        for (i in 0 until SIZE) {
            if (eliminationArray[i].compareAndSet(null, nodeToPut)) {
                index = i
                break
            }
        }
        if (index == -1) {
            return true
        }
        while(waitingTime != 0) {
            if (eliminationArray[index].value == done) {
                eliminationArray[index].value = null
                return false
            }
            waitingTime -= 1
        }
        if (eliminationArray[index].compareAndSet(nodeToPut, null)) {
            return true
        }
        eliminationArray[index].compareAndSet(done, null)
        return false
    }

    override fun top(): T? {
        val head = H.get()
        return head.value
    }
}