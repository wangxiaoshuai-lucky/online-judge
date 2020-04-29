package cn.wzy.consumer.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


public class ExecutorUtil {

    public static void main(String[] args) {
        System.out.println(exec(args[0], 10000));
    }

    public static class ExecMessage {

        private String error;

        private String stdout;

        public ExecMessage() {
        }

        public ExecMessage(String error, String stdout) {
            this.error = error;
            this.stdout = stdout;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getStdout() {
            return stdout;
        }

        public void setStdout(String stdout) {
            this.stdout = stdout;
        }
    }

    public static ExecMessage exec(String cmd, long milliseconds) {
        Runtime runtime = Runtime.getRuntime();
        final Process exec;
        try {
            exec = runtime.exec(cmd);
            if (!exec.waitFor(milliseconds, TimeUnit.MILLISECONDS)) {
                if (exec.isAlive()) {
                    exec.destroy();
                }
                throw new InterruptedException();
            }
        } catch (IOException e) {
            return new ExecMessage(e.getMessage(), null);
        } catch (InterruptedException e) {
            return new ExecMessage("timeOut", null);
        }
        ExecMessage res = new ExecMessage();
        res.setError(message(exec.getErrorStream()));
        res.setStdout(message(exec.getInputStream()));
        return res;
    }

    private static String message(InputStream inputStream) {
        ByteArrayOutputStream buffer = null;
        try {
            buffer = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                buffer.write(bytes, 0, len);
            }
            String result = buffer.toString("UTF-8").trim();
            if (result.equals("")) {
                return null;
            }
            return result;
        } catch (IOException e) {
            return e.getMessage();
        } finally {
            try {
                inputStream.close();
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
