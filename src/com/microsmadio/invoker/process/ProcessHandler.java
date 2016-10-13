package com.microsmadio.invoker.process;

import com.microsmadio.invoker.listener.IReadProcessStreamListener;
import com.microsmadio.invoker.listener.IWriteProcessStreamListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: Penn
 * Project: Invoker
 * Date: 2016/9/29
 * Time: 18:34
 */
public class ProcessHandler {
    private Process process = null;
    private String command = null;
    private String argument[] = null;
    private int exitValue = -1;
    private IReadProcessStreamListener readProcessOutputListener;
    private IReadProcessStreamListener readProcessErrorListener;
    private IWriteProcessStreamListener writeProcessStreamListener;
    private Thread errorReadThread = null;
    private Thread outputReadThread = null;
    private static Runtime RUN_TIME = Runtime.getRuntime();

    public ProcessHandler(String command) {
        this.command = command;
    }

    private void createWriteProcessStream(
            final OutputStream input,
            final IWriteProcessStreamListener writeProcessStreamListener) {
        if (null != writeProcessStreamListener) {
            writeProcessStreamListener.onWriteProcessStream(input);
        }
    }

    private Thread createReadProcessStreamThread(
            final InputStream output,
            final IReadProcessStreamListener readProcessStreamListener) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != readProcessStreamListener) {
                    readProcessStreamListener.onReadProcessStream(output);
                }
            }
        });
        thread.start();
        return thread;
    }

    private void stopReadProcessStreamThread(Thread thread) {
        if (thread.isAlive()) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean exec() throws InterruptedException, IOException {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            if (argument != null) {
                List<String> cmds = new ArrayList<String>();
                cmds.add(command);
                for (String item : argument) {
                    cmds.add(item);
                }
                builder.command(cmds);
            }
            process = builder.start();//RUN_TIME.exec(command);
            createWriteProcessStream(process.getOutputStream(), writeProcessStreamListener);
            errorReadThread = createReadProcessStreamThread(process.getErrorStream(), readProcessErrorListener);
            outputReadThread = createReadProcessStreamThread(process.getInputStream(), readProcessOutputListener);
            exitValue = process.waitFor();
            if (exitValue == 0)
                return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getExitValue() {
        return exitValue;
    }

    public void destroy() {
        if (null != process) {
            process.destroy();
        }
        if (null != errorReadThread) {
            stopReadProcessStreamThread(errorReadThread);
        }
        if (null != outputReadThread) {
            stopReadProcessStreamThread(outputReadThread);
        }
    }

    public void setReadProcessOutputListener(IReadProcessStreamListener readProcessOutputListener) {
        this.readProcessOutputListener = readProcessOutputListener;
    }

    public void setReadProcessErrorListener(IReadProcessStreamListener readProcessErrorListener) {
        this.readProcessErrorListener = readProcessErrorListener;
    }

    public void setWriteProcessStreamListener(IWriteProcessStreamListener writeProcessStreamListener) {
        this.writeProcessStreamListener = writeProcessStreamListener;
    }

    public void setArgument(String[] argument) {
        this.argument = argument;
    }
}
