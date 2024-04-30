suspend fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
    val bst = ThinBST<Int>()
    bst.add(1)
    bst.add(2)
    bst.add(3)
    bst.add(0)
    bst.printTree()

}