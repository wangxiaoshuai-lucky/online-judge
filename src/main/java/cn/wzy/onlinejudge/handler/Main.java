package cn.wzy.onlinejudge.handler;

import cn.wzy.onlinejudge.util.ExecutorUtil;
import lombok.extern.log4j.Log4j;

import java.io.File;

@Log4j
public class Main {
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\test\\test");
		ExecutorUtil.exec("rm -rf " + file.getPath(),500);
	}
}
