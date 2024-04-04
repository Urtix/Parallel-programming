import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.jupiter.api.Test
import stack.treiberStack.TreiberStack

class TestsTreiberStack {

    private val stack = TreiberStack<Int>()

    @Operation
    fun pop() = stack.pop()

    @Operation
    fun push(value: Int) = stack.push(value)

    @Operation
    fun top() = stack.top()

    @Test
    fun stressTest() = StressOptions()
        .threads(2)
        .actorsPerThread(2)
        .iterations(100)
        .invocationsPerIteration(1000)
        .check(this::class)

    @Test
    fun modelCheckingTest() = ModelCheckingOptions()
        .hangingDetectionThreshold(10000)
        .threads(2)
        .actorsPerThread(2)
        .iterations(100)
        .invocationsPerIteration(1000)
        .check(this::class)
}