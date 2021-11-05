package com.natia.secretmod.utils;

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
}
