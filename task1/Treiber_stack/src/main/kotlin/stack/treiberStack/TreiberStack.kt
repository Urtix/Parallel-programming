package stack.treiberStack

import Node.Node
import stack.Stack
import java.util.concurrent.atomic.AtomicReference


class TreiberStack<T>: Stack<T> {
    private val H = AtomicReference(Node<T>(null, null))

    override fun pop(): T? {
        while (true) { // Cas loop
            val head = H.get()
            if (H.compareAndSet(head, head.next))
                return head.value
        }
    }

    override fun push(value: T) {
        while (true) { // Cas loop
            val head = H.get()
            val newHead = Node(value, head)
            if (H.compareAndSet(head, newHead)) {
                return
            }
        }
    }

    override fun top(): T? {
        val head = H.get()
        return head.value
    }
}