import java.util.concurrent.atomic.AtomicReference

data class Node(val value: Int?, var next: Node?)

val H = AtomicReference<Node>(Node(null, null))

fun pop(): Int? {
    while (true) { // Cas loop
        val oldHead = H.get()
        val newHead = oldHead.next
        if (H.compareAndSet(oldHead, newHead)) {
            return oldHead.value
        }
    }
}

fun push(x: Int) {
    while (true) { // Cas loop
        val oldHead = H.get()
        val newHead = Node(x, oldHead)
        if (H.compareAndSet(oldHead, newHead)) {
            return
        }
    }
}

fun top(): Int? {
    while (true) { // Cas loop
        val head = H.get()
        if (head.value == null) {
            return null
        } else {
            return head.value
        }
    }
}


fun main(args: Array<String>) {
    push(2)
    push(3)
    push(4)
    println(top())
    pop()
    println(top())
}