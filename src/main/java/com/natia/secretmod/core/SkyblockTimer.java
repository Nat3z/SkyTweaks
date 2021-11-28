package com.natia.secretmod.core;

import com.google.common.base.Stopwatch;

public class SkyblockTimer {

    public Stopwatch timerStopwatch;
    public String displayText;
    public int secondsUntil;
    public Runnable run;
    public boolean hiddenTimer;

    public SkyblockTimer(Stopwatch stopwatch, String definer, int secondsUntil, boolean hiddenTimer, Runnable run) {
        this.timerStopwatch = stopwatch;
        this.displayText = definer;
        this.run = run;
        this.hiddenTimer = hiddenTimer;
        this.secondsUntil = secondsUntil;
    }

}
