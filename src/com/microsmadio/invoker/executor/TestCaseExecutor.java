package com.microsmadio.invoker.executor;

import com.microsmadio.invoker.listener.*;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: Penn
 * Project: Invoker
 * Date: 2016/9/29
 * Time: 19:42
 */
public class TestCaseExecutor {
    private ExecutorService executorService = null;
    private Vector<ITimeOutListener> timeOutListeners = new Vector<ITimeOutListener>();
    private Vector<IInterruptedListener> interruptedListeners = new Vector<IInterruptedListener>();
    private Vector<IExecutionFaultListener> executionFaultListeners = new Vector<IExecutionFaultListener>();
    private Vector<IExecutionSuccessListener> executionSuccessListeners = new Vector<IExecutionSuccessListener>();
    //private HashMap<Class<? extends Exception>, Vector> eventMap;

    public TestCaseExecutor() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public TestCaseExecutor(int poolSize) {
        executorService = Executors.newFixedThreadPool(poolSize);
        // eventMap = new HashMap<Class<? extends Exception>, Vector>();
    }

    public boolean submit(TestCase testCase) {
        Task task = testCase.getTask();
        Future<Long> future = executorService.submit(task);
        long timeElapsed = -1;
        try {
            timeElapsed = future.get(testCase.getTimeLimit(), testCase.getTimeUnit());
            int exitValue = task.getExitValue();
            onExecutionSuccess(makeExecutionResult(testCase, task, null, timeElapsed, exitValue));
            onExecutionExceptionFinished(future, task, null);
            return exitValue == 0;
        } catch (InterruptedException e) {
            onExecutionInterrupted(makeExecutionResult(testCase, task, e, timeElapsed, task.getExitValue()));
            onExecutionExceptionFinished(future, task, e);
        } catch (ExecutionException e) {
            onExecutionFault(makeExecutionResult(testCase, task, e, timeElapsed, task.getExitValue()));
            onExecutionExceptionFinished(future, task, e);
        } catch (TimeoutException e) {
            onExecutionTimeOut(makeExecutionResult(testCase, task, e, timeElapsed, task.getExitValue()));
            onExecutionExceptionFinished(future, task, e);
        }
        return false;
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private <T> void addListener(Vector<T> container, T listener, Class<? extends Exception> clazz) {
//        if (container == null) {
//            container = new Vector<T>();
//            // eventMap.put(clazz, container);
//        }
        if (listener != null) {
            container.add(listener);
        }
    }

    private <T> void removeListener(Vector<T> container, T listener) {
        if (listener != null) {
            container.remove(listener);
        }
    }

    public void addTimeOutListener(ITimeOutListener timeOutListener) {
        addListener(timeOutListeners, timeOutListener, TimeoutException.class);
    }

    public void addInterruptedListener(IInterruptedListener interruptedListener) {
        addListener(interruptedListeners, interruptedListener, InterruptedException.class);
    }

    public void addExecutionFaultListener(IExecutionFaultListener executionFaultListener) {
        addListener(executionFaultListeners, executionFaultListener, ExecutionException.class);
    }

    public void addExecutionSuccessListener(IExecutionSuccessListener executionSuccessListener) {
        addListener(executionSuccessListeners, executionSuccessListener, null);
    }

    public void removeTimeOutListener(ITimeOutListener timeOutListener) {
        removeListener(timeOutListeners, timeOutListener);
    }

    public void removeInterruptedListener(IInterruptedListener interruptedListener) {
        removeListener(interruptedListeners, interruptedListener);
    }

    public void removeExecutionFaultListener(IExecutionFaultListener executionFaultListener) {
        removeListener(executionFaultListeners, executionFaultListener);
    }

    public void removeExecutionSuccessListener(IExecutionSuccessListener executionSuccessListener) {
        removeListener(executionSuccessListeners, executionSuccessListener);
    }

    public void onExecutionTimeOut(ExecutionEventArgs eventArgs) {
        if (timeOutListeners == null || timeOutListeners.isEmpty()) return;
        for (ITimeOutListener h : timeOutListeners) {
            h.onExecutionTimeOut(eventArgs);
        }
    }

    public void onExecutionInterrupted(ExecutionEventArgs eventArgs) {
        if (interruptedListeners == null || interruptedListeners.isEmpty()) return;
        for (IInterruptedListener h : interruptedListeners) {
            h.onExecutionInterrupted(eventArgs);
        }
    }

    public void onExecutionFault(ExecutionEventArgs eventArgs) {
        if (executionFaultListeners == null || executionFaultListeners.isEmpty()) return;
        for (IExecutionFaultListener h : executionFaultListeners) {
            h.onExecutionFault(eventArgs);
        }
    }

    public void onExecutionSuccess(ExecutionEventArgs eventArgs) {
        if (executionSuccessListeners == null || executionSuccessListeners.isEmpty()) return;
        for (IExecutionSuccessListener h : executionSuccessListeners) {
            h.onExecutionSuccess(eventArgs);
        }
    }

    private void onExecutionExceptionFinished(Future<Long> future, Task task, Exception e) {
        task.stop();
        future.cancel(true);
    }

    private final ExecutionEventArgs makeExecutionResult(
            TestCase testCase,
            Task task,
            Exception e,
            long timeElapsed,
            int exitValue) {
        ExecutionEventArgs executeResult = new ExecutionEventArgs();
        executeResult.setTestCase(testCase);
        executeResult.setTask(task);
        if (null != e) {
            executeResult.setException(e);
        }
        executeResult.setTimeElapsed(timeElapsed);
        executeResult.setExitValue(exitValue);
        return executeResult;
    }
}
