package com.natia.secretmod.utils;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class AsyncAwait {
    public static void start(Runnable run, int millis) {
        new Thread(() -> {
            try {
                Thread.sleep(millis);
                run.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void until(Runnable run, int seconds, Stopwatch stopwatch) {
        if (!stopwatch.isRunning()) stopwatch.start();

        if (stopwatch.elapsed(TimeUnit.SECONDS) >= seconds) {
            stopwatch.reset();
            run.run();
        }
    }
}
