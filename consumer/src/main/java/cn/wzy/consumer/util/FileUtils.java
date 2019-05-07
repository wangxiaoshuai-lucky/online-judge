package cn.wzy.consumer.util;

import java.io.*;

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
}
