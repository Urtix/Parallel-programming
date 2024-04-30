import kotlinx.coroutines.sync.Mutex

open class ThinBST<T : Comparable<T>> {
    private var root: NodeMutex<T>? = null

    // Вставка узла
    open suspend fun add(value: T) {
        Mutex().lock()
        if (root != null) {
            root!!.lock()
            root = add(root, value, null)
        } else {
            root = NodeMutex(value, null)
        }
    }

    protected suspend fun add(node: NodeMutex<T>?, value: T, parent: NodeMutex<T>?): NodeMutex<T> {
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
                        node.right = NodeMutex(value, parent)
                    }
                    node
                }
                else -> node // Узел со значением value уже существует, поэтому мы ничего не делаем
            }
        }
    }

    // Удаление узла
    open suspend fun delete(value: T) {
        Mutex().lock()
        if (root != null) {
            root!!.lock()
            Mutex().unlock()
            root = delete(root, value)
        } else {
            root = NodeMutex(value, null)
            Mutex().unlock()
        }
    }

    protected suspend fun delete(node: NodeMutex<T>?, value: T): NodeMutex<T>? {
        if (node == null) {
            return null
        }
        when {
            value == node.value -> {
                when {
                    node.left == null -> {
                        node.unlock()
                        node.parent?.unlock()
                        return node.right
                    }
                    node.right == null -> {
                        node.unlock()
                        node.parent?.unlock()
                        return node.left
                    }
                    else -> {
                        var successor = node.right
                        while (successor?.left != null) {
                            successor = successor.left
                        }
                        node.value = successor!!.value
                        node.right = delete(node.right, successor.value)
                        node.unlock()
                        node.parent?.unlock()
                        return node
                    }
                }
            }
            value < node.value -> {
                if (node.left == null ){
                    node.unlock()
                    return null
                } else if (node.left?.value != value){
                    node.left?.lock()
                    node.unlock()
                    node.left = delete(node.left, value)
                    return node
                } else {
                    node.left?.lock()
                    node.left = delete(node.left, value)
                    return node
                }
            }
            else -> {
                if (node.right == null ){
                    node.unlock()
                    return null
                } else {
                    node.right?.lock()
                    node.right = delete(node.right, value)
                    return node
                }
            }
        }
    }

    // Поиск узла
    open suspend fun search(value: T): NodeMutex<T>? {
        return if (root == null) null
        else {
            root?.lock()
            search(root, value)
        }
    }

    protected suspend fun search(node: NodeMutex<T>?, value: T): NodeMutex<T>? {
        if (node == null) {
            return null
        } else {
            when {
                value == node.value -> {
                    node.unlock()
                    return node
                }
                value < node.value -> {
                    if (node.left != null) {
                        node.unlock()
                        node.left?.lock()
                         return search(node.left, value)
                    } else {
                        node.unlock()
                        return null
                    }
                }
                else -> {
                    if (node.right != null) {
                        node.unlock()
                        node.right?.lock()
                        return search(node.right, value)
                    } else {
                        node.unlock()
                        return null
                    }
                }
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
        println(node.left?.value)
        println(node.left?.left?.value)
        println(node.left?.left?.parent?.parent?.value)
    }
}