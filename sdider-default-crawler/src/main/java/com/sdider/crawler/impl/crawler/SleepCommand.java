package com.sdider.crawler.impl.crawler;

import java.util.concurrent.TimeUnit;

/**
 * SleepCommand接收一个Command并在一段时间后唤醒它
 * @author yujiaxin
 */
public class SleepCommand implements Command{
    private final Command command;
    private final ActiveObjectEngine engine;
    private Long wakeUpTime;
    private final long sleepMillis;

    public SleepCommand(ActiveObjectEngine engine, Command command, long time, TimeUnit timeUnit) {
        this.engine = engine;
        this.command = command;
        sleepMillis = timeUnit.toMillis(time);
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        if (wakeUpTime == null) {
            wakeUpTime = now + sleepMillis;
        }
        if (wakeUpTime <= now) {
            engine.add(command);
            wakeUpTime = null;
        } else {
            engine.add(this);
        }
    }

    @Override
    public String toString() {
        return "SleepCommand{" + "command=" + command +
                ", wakeUpTime=" + wakeUpTime +
                ", sleepMillis=" + sleepMillis +
                '}';
    }
}
