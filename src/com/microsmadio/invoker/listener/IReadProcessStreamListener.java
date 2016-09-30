package com.microsmadio.invoker.listener;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Penn
 * Project: Invoker
 * Date: 2016/9/30
 * Time: 10:46
 */
public interface IReadProcessStreamListener {
    public void onReadProcessStream(InputStream stream);
}
