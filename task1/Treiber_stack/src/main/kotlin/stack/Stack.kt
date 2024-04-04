package stack

interface Stack<T> {
    fun pop() : Any?
    fun push(value: T)
    fun top() : T?
}