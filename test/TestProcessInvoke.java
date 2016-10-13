import com.microsmadio.invoker.executor.TestCase;
import com.microsmadio.invoker.executor.TestCaseExecutor;
import com.microsmadio.invoker.listener.*;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Penn
 * Project: Invoker
 * Date: 2016/9/29
 * Time: 18:35
 */
public class TestProcessInvoke {
    public static void main(String[] args) {
        IWriteProcessStreamListener writeStreamHandler = new IWriteProcessStreamListener() {
            @Override
            public void onWriteProcessStream(OutputStream stream) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stream));
                try {
                    bw.write("hello\n");
                    bw.write("1");
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        IReadProcessStreamListener readStreamHandler = new IReadProcessStreamListener() {
            @Override
            public void onReadProcessStream(InputStream stream) {

                String line = "";
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(stream));
                    try {
                        while ((line = input.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Error e) {
                    System.out.println("ERROR");
                }
            }
        };

        TestCase t1 = new TestCase("res/sim_a.exe", 1000);
        t1.setTaskErrorCallback(readStreamHandler);
        t1.setTaskOutputCallback(readStreamHandler);

        TestCase t2 = new TestCase("res/sim_b.exe", 1000);
        t2.setTaskErrorCallback(readStreamHandler);
//        t2.setTaskOutputCallback(readStreamHandler);

        TestCase t3 = new TestCase("res/sim_c.exe", 2000);
        t3.setTaskErrorCallback(readStreamHandler);
        t3.setTaskOutputCallback(readStreamHandler);

        // t4 input:
        // string\n
        // int
        TestCase t4 = new TestCase("res/sim_d.exe", 1000);
        t4.setTaskInputCallback(writeStreamHandler);
        t4.setTaskErrorCallback(readStreamHandler);
        t4.setTaskOutputCallback(readStreamHandler);

        TestCaseExecutor pp = new TestCaseExecutor();
        pp.addTimeOutListener(new ITimeOutListener() {
            @Override
            public void onExecutionTimeOut(ExecutionEventArgs eventArgs) {
//                System.out.println("Time Out!");
                System.out.println(eventArgs);
            }
        });
        pp.addInterruptedListener(new IInterruptedListener() {
            @Override
            public void onExecutionInterrupted(ExecutionEventArgs eventArgs) {
                System.out.println("Interrupted!");
                System.out.println(eventArgs);
            }
        });
        pp.addExecutionFaultListener(new IExecutionFaultListener() {
            @Override
            public void onExecutionFault(ExecutionEventArgs eventArgs) {
                System.out.println("Execution Fault");
                System.out.println(eventArgs);
            }
        });
        pp.addExecutionSuccessListener(new IExecutionSuccessListener() {
            @Override
            public void onExecutionSuccess(ExecutionEventArgs eventArgs) {
                System.out.println("Execution Success");
                System.out.println(eventArgs);
            }
        });
        boolean r = false;
        r = pp.submit(t1);
        System.out.println(r);
        r = pp.submit(t2);
        System.out.println(r);
        r = pp.submit(t3);
        System.out.println(r);
        r = pp.submit(t4);
        System.out.println(r);
        pp.shutdown();
    }
}
