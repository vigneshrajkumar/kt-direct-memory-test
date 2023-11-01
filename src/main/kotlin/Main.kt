import reactor.core.publisher.Flux
import java.time.Duration

const val ONE_MB_IN_BYTES = 1024 * 1024

fun main(args: Array<String>) {
    println("direct mem exp")

    val memoryBlockSize = System.getProperty("BLOCK_SIZE", "1000").toInt()
    val waitTime = System.getProperty("STEP_DURATION", "1").toLong()
    var totalAllocation = 0L

    println("start..")
    println("Allocating blocks of $memoryBlockSize in $waitTime sec intervals")

    val infiniteEventStream = Flux.generate<String> { sink ->
        sink.next("Event at ${java.time.LocalTime.now()}")
    }.delayElements(Duration.ofSeconds(waitTime))

    // pipeline 1
    infiniteEventStream
    .doOnNext {
        val buffer = java.nio.ByteBuffer.allocateDirect(memoryBlockSize * ONE_MB_IN_BYTES)
        totalAllocation += buffer.limit()
        println("pipeline 1: Total Allocation:" + totalAllocation / ONE_MB_IN_BYTES + "mb")
    }.subscribe()

    // pipeline 2
    infiniteEventStream
    .subscribe{
        println("pipeline 2: processing event $it")
    }

    Thread.sleep(Long.MAX_VALUE)
}