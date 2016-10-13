package com.microsmadio.invoker.listener;

import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Penn
 * Project: ProcessInvoker
 * Date: 2016/10/13
 * Time: 10:33
 */
public interface IWriteProcessStreamListener {
    public void onWriteProcessStream(OutputStream stream);
}
