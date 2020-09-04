package com.sdider.crawler.impl.crawler;

/**
 * 一个ActiveObjectEngine会以单线程循环的模式持续运行其中包含的任务，
 * 并且可以在运行期间添加任务。ActiveObjectEngine同一时间内运行的任务是
 * 有限制的，{@link #getMaxSize()}方法返回此ActiveObjectEngine能够
 * 运行的最大任务数量。
 * @author yujiaxin
 */
interface ActiveObjectEngine {

    void add(Command command);

    /**
     * 开始运行引擎，这个方法会阻塞至{@link #getCurrentSize()}==0
     * 时返回。
     */
    void run();

    /**
     * 引擎内是否不包含任何Command
     * @return {@code true} if the engine dose not contains any command;
     * {@code false} for else
     */
    boolean isEmpty();

    /**
     * 返回此Engine同一时间能够运行的最大任务数量
     * @return 此Engine同一时间能够运行的最大Command数量
     */
    int getMaxSize();

    /**
     * 返回当前引擎中包含的command的数量
     * @return the number of commands which in the engine
     */
    int getCurrentSize();

    boolean isRunning();
}
