package com.microsmadio.invoker.executor;

import com.microsmadio.invoker.listener.IReadProcessStreamListener;
import com.microsmadio.invoker.listener.IWriteProcessStreamListener;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Penn
 * Project: Invoker
 * Date: 2016/9/29
 * Time: 19:34
 */
public class TestCase {
    private static final int TIME_LIMIT = 1000;
    private static final float WEIGHT = 1.0f;

    private String name = null;
    private String description = null;

    private String command = null;
    private String arguments[] = null;
    private long timeLimit = TIME_LIMIT;
    private float weight = WEIGHT;
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    private IWriteProcessStreamListener writeStreamInputCallback;
    private IReadProcessStreamListener readStreamOutputCallback;
    private IReadProcessStreamListener readStreamErrorCallback;

    public TestCase(final String command) {
        this.command = command;
    }

    public TestCase(final String command, final String... arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public TestCase(final String command, long timeLimit) {
        this.command = command;
        this.timeLimit = timeLimit;
    }

    public TestCase(final String command, long timeLimit, final String... arguments) {
        this.command = command;
        this.timeLimit = timeLimit;
        this.arguments = arguments;
    }

    public TestCase(final String name,
                    final String description,
                    final String command) {
        this.name = name;
        this.description = description;
        this.command = command;
    }

    public Task getTask() {
        Task task = new Task(command);
        task.setArguments(arguments);
        if (null != writeStreamInputCallback) {
            task.setWriteExecutionInputCallback(writeStreamInputCallback);
        }
        if (null != readStreamOutputCallback) {
            task.setReadExecutionOutputCallback(readStreamOutputCallback);
        }
        if (null != readStreamErrorCallback) {
            task.setReadExecutionErrorCallback(readStreamErrorCallback);
        }
        return task;
    }

    public void setTaskInputCallback(IWriteProcessStreamListener writeStreamInputCallback) {
        this.writeStreamInputCallback = writeStreamInputCallback;
    }

    public void setTaskOutputCallback(IReadProcessStreamListener readProcessStreamListener) {
        readStreamOutputCallback = readProcessStreamListener;
    }

    public void setTaskErrorCallback(IReadProcessStreamListener readProcessStreamListener) {
        readStreamErrorCallback = readProcessStreamListener;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
}
