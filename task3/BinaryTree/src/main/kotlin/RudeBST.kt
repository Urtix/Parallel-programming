class RudeBST<T : Comparable<T>> : BinarySearchTree<T>() {
    private val lock = Any()

    private var root: Node<T>? = null

    override fun add(value: T) {
        synchronized(lock) {
            root = add(root, value)
        }
    }

    override fun search(value: T): Node<T>? {
        synchronized(lock) {
            return search(root, value)
        }
    }

    override fun delete(value: T) {
        synchronized(lock) {
            root = delete(root, value)
        }
    }
}