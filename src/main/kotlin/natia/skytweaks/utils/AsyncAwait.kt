package natia.skytweaks.utils

import com.google.common.base.Stopwatch

import java.util.concurrent.TimeUnit

object AsyncAwait {
    fun start(run: () -> Unit, millis: Int) {
        Thread {
            try {
                Thread.sleep(millis.toLong())
                run()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }

    fun until(run: () -> Unit, seconds: Int, stopwatch: Stopwatch) {
        if (!stopwatch.isRunning) stopwatch.start()

        if (stopwatch.elapsed(TimeUnit.SECONDS) >= seconds) {
            stopwatch.reset()
            run()
        }
    }
}
