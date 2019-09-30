package cn.wzy.consumer.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils {

	public static void write(String str, File file) throws IOException {
		OutputStream outputStream = new FileOutputStream(file);
		outputStream.write(str.getBytes());
		outputStream.close();
	}

	public static void write(InputStream inputStream, OutputStream outputStream, boolean close) throws IOException {
		byte[] by = new byte[1024];
		int n;
		while ((n = inputStream.read(by)) != -1) {
			outputStream.write(by, 0, n);
		}
		if (close) {
			inputStream.close();
			outputStream.close();
		}
	}

	public static String read(File src) {
		byte[] bytes = new byte[1024];
		int len;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(src);
			StringBuilder builder = new StringBuilder();
			while ((len = inputStream.read(bytes)) != -1) {
				byte[] effective = new byte[len];
				System.arraycopy(bytes, 0, effective, 0, len);
				builder.append(new String(effective, StandardCharsets.UTF_8));
			}
			return builder.toString();
		} catch (IOException e) {
			return "";
		} finally {
			try {
				assert inputStream != null;
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
