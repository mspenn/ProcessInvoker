package com.microsmadio.invoker.executor;

import com.microsmadio.invoker.listener.IReadProcessStreamListener;
import com.microsmadio.invoker.listener.IWriteProcessStreamListener;
import com.microsmadio.invoker.process.ProcessHandler;

import java.io.PrintStream;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: Penn
 * Project: Invoker
 * Date: 2016/9/29
 * Time: 19:06
 */
public class Task implements Callable<Long> {
    private ProcessHandler processHandler = null;
    private String[] arguments;

    public Task(String command) {
        this.processHandler = new ProcessHandler(command);
    }

    public void setArguments(final String[] args) {
        this.arguments = args;
        processHandler.setArgument(args);
    }

    @Override
    public Long call() throws Exception {
        long timeStart, timeEnd;
        timeStart = System.currentTimeMillis();
        processHandler.exec();
        timeEnd = System.currentTimeMillis();
        return timeEnd - timeStart;
    }

    public void stop() {
        if (processHandler != null) {
            processHandler.destroy();
        }
    }

    public void setReadExecutionOutputCallback(IReadProcessStreamListener readExecutionOutputCallback) {
        processHandler.setReadProcessOutputListener(readExecutionOutputCallback);
    }

    public void setReadExecutionErrorCallback(IReadProcessStreamListener readExecutionOutputCallback) {
        processHandler.setReadProcessErrorListener(readExecutionOutputCallback);
    }

    public void setWriteExecutionInputCallback(IWriteProcessStreamListener writeExecutionInputCallback) {
        processHandler.setWriteProcessStreamListener(writeExecutionInputCallback);
    }

    public int getExitValue() {
        return processHandler.getExitValue();
    }
}
