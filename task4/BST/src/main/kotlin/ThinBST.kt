import kotlinx.coroutines.sync.Mutex

open class ThinBST<T : Comparable<T>> {
    private var root: NodeMutex<T>? = null

    // Вставка узла
    open suspend fun add(value: T) {
        Mutex().lock()
        root = if (root != null) {
            root?.lock()
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
                    if (node.left != null) {
                        node.left?.lock()
                        node.unlock()
                        node.left = add(node.left, value, node)
                    } else {
                        node.unlock()
                        node.left?.lock()
                        node.left = NodeMutex(value, parent)
                    }
                    node
                }
                value > node.value -> {
                    if (node.right != null) {
                        node.right?.lock()
                        node.unlock()
                        node.right = add(node.right, value, node)
                    } else {
                        node.unlock()
                        node.right?.lock()
                        node.right = NodeMutex(value, parent)
                    }
                    node
                }
                else -> {
                    node.unlock()
                    node // Узел со значением value уже существует, поэтому мы ничего не делаем
                }
            }
        }
    }

    // Удаление узла
    open suspend fun delete(value: T) {
        Mutex().lock()
        if (root != null) {
            root?.lock()
            root = thinDelete(root, value)
        }
    }

     protected suspend fun thinDelete(node: NodeMutex<T>?, value: T): NodeMutex<T>? {
        if (node == null) {
            return null
        }
        when {
            value == node.value -> {
                when {
                    node.left == null -> {
                        node.unlock()
                        return node.right
                    }
                    node.right == null -> {
                        node.unlock()
                        return node.left
                    }
                    else -> {
                        node.right?.lock()
                        var successor = node.right
                        while (successor?.left != null) {
                            successor = successor.left
                        }
                        node.value = successor!!.value
                        node.unlock()
                        node.right = thinDelete(node.right, successor.value)
                        return node
                    }
                }
            }
            value < node.value -> {
                return if (node.left == null ){
                    node.unlock()
                    null
                } else {
                    node.left?.lock()
                    node.unlock()
                    node.left = thinDelete(node.left, value)
                    node
                }
            }
            else -> {
                return if (node.right == null ){
                    node.unlock()
                    null
                } else {
                    node.right?.lock()
                    node.unlock()
                    node.right = thinDelete(node.right, value)
                    node
                }
            }
        }
    }

    // Поиск узла
    open suspend fun search(value: T): Any? {
        return if (root == null) null
        else {
            root?.lock()
            search(root, value)
        }
    }

    private suspend fun search(node: NodeMutex<T>?, value: T): Any? {
        if (node == null) {
            return null
        } else {
            when {
                value == node.value -> {
                    node.unlock()
                    return node.value
                }
                value < node.value -> {
                    return if (node.left != null) {
                        node.unlock()
                        node.left?.lock()
                        search(node.left, value)
                    } else {
                        node.unlock()
                        null
                    }
                }
                else -> {
                    return if (node.right != null) {
                        node.unlock()
                        node.right?.lock()
                        search(node.right, value)
                    } else {
                        node.unlock()
                        null
                    }
                }
            }
        }
    }

    // Печать дерева
    open fun printTree() {
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