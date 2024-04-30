open class BinarySearchTree<T : Comparable<T>> {
    private var root: Node<T>? = null

    // Вставка узла
    open fun add(value: T) {
        root = add(root, value)
    }

    protected fun add(node: Node<T>?, value: T): Node<T> {
        return if (node == null) {
            Node(value)
        } else {
            when {
                value < node.value -> {
                    node.left = add(node.left, value)
                    node
                }

                value > node.value -> {
                    node.right = add(node.right, value)
                    node
                }

                else -> node // Узел со значением value уже существует, поэтому мы ничего не делаем
            }
        }
    }

    // Удаление узла
    open fun delete(value: T) {
        root = delete(root, value)
    }

    protected fun delete(node: Node<T>?, value: T): Node<T>? {
        if (node == null) {
            return null
        }

        when {
            value == node.value -> {
                return when {
                    node.left == null -> node.right
                    node.right == null -> node.left
                    else -> {
                        var successor = node.right
                        while (successor?.left != null) {
                            successor = successor.left
                        }
                        node.value = successor!!.value
                        node.right = delete(node.right, successor.value)
                        node
                    }
                }
            }
            value < node.value -> {
                node.left = delete(node.left, value)
                return node
            }
            else -> {
                node.right = delete(node.right, value)
                return node
            }
        }
    }

    // Поиск узла
    open fun search(value: T): Node<T>? {
        return search(root, value)
    }

    protected fun search(node: Node<T>?, value: T): Node<T>? {
        return if (node == null) {
            null
        } else {
            when {
                value == node.value -> node
                value < node.value -> search(node.left, value)
                else -> search(node.right, value)
            }
        }
    }

    // Печать дерева
    fun printTree() {
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