package natia.skytweaks.core

import com.google.common.base.Stopwatch

class SkyblockTimer(var timerStopwatch: Stopwatch, var displayText: String, var secondsUntil: Int, var hiddenTimer: Boolean, var run: () -> Unit)
