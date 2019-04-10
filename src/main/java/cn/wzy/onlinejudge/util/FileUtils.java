package cn.wzy.onlinejudge.util;

import java.io.*;

public class FileUtils {

	public static void write(String str, File file) throws IOException {
		OutputStream outputStream = new FileOutputStream(file);
		outputStream.write(str.getBytes());
		outputStream.close();
	}
}
