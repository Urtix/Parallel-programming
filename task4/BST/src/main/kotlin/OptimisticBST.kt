
open class OptimisticBST<T : Comparable<T>> : ThinBST<T>()  {
    private var root: NodeMutex<T>? = null

    // Вставка узла
    override suspend fun add(value: T) {
        root = if (root?.value != null) {
            add(root, value, null)
        } else {
            NodeMutex(value, null)
        }
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
                        val newNode = doubleSearch(root, node.value)
                        if ((newNode == node) && (newNode.parent == node.parent)) {
                            node.left = NodeMutex(value, parent)
                            node.unlock()
                            node.parent?.unlock()
                            
                        } else {
                            add(root, value, null)
                        }
                    } else {
                        node.left = add(node.left, value, null)
                    }
                    node
                }

                value > node.value -> {
                    if (node.right == null) {
                        node.lock()
                        node.parent?.lock()
                        val newNode = doubleSearch(root, node.value)
                        if ((newNode == node) && (newNode.parent == node.parent)) {
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
    override suspend fun delete(value: T) {
        root = optimisticDelete(root, value)
    }

    private suspend fun optimisticDelete(node: NodeMutex<T>?, value: T): NodeMutex<T>? {
        if (node == null) {
            return null
        }

        when {
            value == node.value -> {
                node.parent?.lock()
                node.lock()
                val newNode = doubleSearch(root, node.value)
                if ((newNode == node) && (newNode.parent == node.parent)) {
                    return when {
                        node.left == null -> {
                            node.parent?.unlock()
                            node.unlock()
                            node.right
                        }
                        node.right == null -> {
                            node.parent?.unlock()
                            node.unlock()
                            node.left
                        }
                        else -> {
                            var successor = node.right
                            while (successor?.left != null) {
                                successor = successor.left
                            }
                            node.right?.lock()
                            node.right = thinDelete(node.right, successor!!.value)
                            node.value = successor.value
                            node.parent?.unlock()
                            node.unlock()
                            return node
                        }
                    }
                } else {
                    node.parent?.unlock()
                    node.unlock()
                    return optimisticDelete(root, value)
                }
            }
            value < node.value -> {
                node.left = optimisticDelete(node.left, value)
                return node
            }
            else -> {
                node.right = optimisticDelete(node.right, value)
                return node
            }
        }
    }

    // Поиск узла
    override suspend fun search(value: T): Any? {
        return search(root, value)
    }

    private suspend fun search(node: NodeMutex<T>?, value: T): Any? {
        return if (node == null) {
            null
        } else {
            when {
                value == node.value -> {
                    node.parent?.lock()
                    node.lock()
                    val newNode = doubleSearch(root, value)
                    if ((newNode == node) && (newNode.parent == node.parent)) {
                        node.unlock()
                        node.parent?.unlock()
                        return node.value
                    } else {
                        search(root, value)
                    }
                }
                value < node.value -> search(node.left, value)
                else -> search(node.right, value)
            }
        }
    }

    private fun doubleSearch(node: NodeMutex<T>?, value: T): NodeMutex<T>? {
        return if (node == null) {
            null
        } else {
            when {
                value == node.value -> node
                value < node.value -> doubleSearch(node.left, value)
                else -> doubleSearch(node.right, value)
            }
        }
    }

    // Печать дерева
    override fun printTree() {
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