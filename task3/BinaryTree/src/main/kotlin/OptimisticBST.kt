import kotlinx.coroutines.sync.Mutex

open class OptimisticBST<T : Comparable<T>>  {
    private var root: NodeMutex<T>? = null

    // Вставка узла
    open suspend fun add(value: T) {
        root = add(root, value, null)
    }

    private suspend fun add(node: NodeMutex<T>?, value: T, parent: NodeMutex<T>?): NodeMutex<T> {
        return if (node == null) {
            NodeMutex(value, parent)
        } else {
            when {
                value < node.value -> {
                    if (node.left == null) {
                        node.lock()
                        node.parent?.lock()
                        val new_node = double_search(root, node.value)
                        if ((new_node == node) && (new_node.parent == node.parent)) {
                            node.left = NodeMutex(value, parent)
                            node.unlock()
                            node.parent?.unlock()
                            
                        } else {
                            add(root, value, null)
                        }
                    } else {
                        root = add(root, value, null)
                    }
                    node
                }

                value > node.value -> {
                    if (node.right == null) {
                        node.lock()
                        node.parent?.lock()
                        val new_node = double_search(root, node.value)
                        if ((new_node == node) && (new_node.parent == node.parent)) {
                            node.right = NodeMutex(value, parent)
                            node.unlock()
                            node.parent?.unlock()

                        } else {
                            root = add(root, value, null)
                        }
                    } else {
                        node.right = add(node.right, value, node)
                    }
                    node
                }

                else -> node // Узел со значением value уже существует, поэтому мы ничего не делаем
            }
        }
    }

    // Удаление узла
// Удаление узла
    open suspend fun delete(value: T) {
        root = delete(root, value)
    }

    private suspend fun delete(node: NodeMutex<T>?, value: T): NodeMutex<T>? {
        if (node == null) {
            return null
        }

        when {
            value == node.value -> {
                node.parent?.lock()
                node.lock()
                val new_node = double_search(root, node.value)
                if ((new_node == node) && (new_node.parent == node.parent)) {
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
                } else {
                    return delete(root, value)
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
    open suspend fun search(value: T): NodeMutex<T>? {
        return search(root, value)
    }

    private suspend fun search(node: NodeMutex<T>?, value: T): NodeMutex<T>? {
        return if (node == null) {
            null
        } else {
            when {
                value == node.value -> {
                    node.parent?.lock()
                    node.lock()
                    val new_node = double_search(root, value)
                    if ((new_node == node) && (new_node.parent == node.parent)) {
                        node.unlock()
                        node.parent?.unlock()
                        return node
                    } else {
                        search(root, value)
                    }
                }
                value < node.value -> search(node.left, value)
                else -> search(node.right, value)
            }
        }
    }

    private fun double_search(node: NodeMutex<T>?, value: T): NodeMutex<T>? {
        return if (node == null) {
            null
        } else {
            when {
                value == node.value -> node
                value < node.value -> double_search(node.left, value)
                else -> double_search(node.right, value)
            }
        }
    }

    // Печать дерева
    fun printTree() {
        printTree(root)
    }

    private fun printTree(node: NodeMutex<T>?) {
        if (node == null) {
            return
        }
        printTree(node.left)
        print("${node.value} ")
        printTree(node.right)
    }
}