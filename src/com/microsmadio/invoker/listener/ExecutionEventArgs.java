package com.microsmadio.invoker.listener;

import com.microsmadio.invoker.executor.Task;
import com.microsmadio.invoker.executor.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: Penn
 * Project: Invoker
 * Date: 2016/9/30
 * Time: 10:11
 */
enum ExecutionStatus {
    TIME_OUT,
    EXECUTE_FAULT,
    INTERRUPTED,
    SUCCESS,
}

public class ExecutionEventArgs {
    private static final Map<Class<? extends Exception>, ExecutionStatus> EXECUTION_STATUS_MAP
            = new HashMap<Class<? extends Exception>, ExecutionStatus>();

    static {
        EXECUTION_STATUS_MAP.put(InterruptedException.class, ExecutionStatus.INTERRUPTED);
        EXECUTION_STATUS_MAP.put(ExecutionException.class, ExecutionStatus.EXECUTE_FAULT);
        EXECUTION_STATUS_MAP.put(TimeoutException.class, ExecutionStatus.TIME_OUT);
    }

    private TestCase testCase = null;
    private Task task = null;

    private long timeElapsed = -1;
    private ExecutionStatus executionStatus = ExecutionStatus.SUCCESS;
    private int exitValue = -1;
    private Exception exception = null;

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public ExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public int getExitValue() {
        return exitValue;
    }

    public void setExitValue(int exitValue) {
        this.exitValue = exitValue;
        if (exitValue != 0 && exitValue != -1) {
            this.executionStatus = ExecutionStatus.EXECUTE_FAULT;
        }
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
        if (EXECUTION_STATUS_MAP.containsKey(exception.getClass())) {
            this.executionStatus = EXECUTION_STATUS_MAP.get(exception.getClass());
        }
    }

    public boolean isExecutionSuccess() {
        return this.exitValue == 0 || this.executionStatus == ExecutionStatus.SUCCESS;
    }

    public String toString() {
        return String.format("COMMAND:%s, TIME_ELAPSED:%d, EXECUTE_STATUS:(%d, %s)",
                testCase.getCommand(), timeElapsed, exitValue, executionStatus.toString());
    }
}
