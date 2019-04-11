package cn.wzy.onlinejudge.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExecutorUtil {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ExecMessage {

		private String error;

		private String stdout;

		private String warn;
	}

	public static ExecMessage exec(String cmd) {
		Runtime runtime = Runtime.getRuntime();
		Process exec = null;
		try {
			exec = runtime.exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
			return new ExecMessage(e.getMessage(), null, null);
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
