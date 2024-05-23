class RudeBST<T : Comparable<T>> : BinarySearchTree<T>() {
    private val lock = Any()

    private var root: Node<T>? = null

    override fun add(value: T) {
        synchronized(lock) {
            root = add(root, value)
        }
    }

    override fun search(value: T): Any? {
        synchronized(lock) {
            return search(root, value)
        }
    }

    override fun delete(value: T) {
        synchronized(lock) {
            root = delete(root, value)
        }
    }

    override fun printTree() {
        printTree(root)
    }

    private fun printTree(node: Node<T>?) {
        if (node == null) {
            return
        }
        printTree(node.left)
        print("${node.value} ")
        printTree(node.right)
    }
}