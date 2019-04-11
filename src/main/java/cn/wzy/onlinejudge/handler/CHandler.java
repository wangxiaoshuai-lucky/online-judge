package cn.wzy.onlinejudge.handler;

import cn.wzy.onlinejudge.util.FileUtils;
import cn.wzy.onlinejudge.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

public class CHandler extends Handler {

	@Value("${Cword}")
	private String compilerWord;

	@Value("${Crun}")
	private String runWord;

	public CHandler(JudgeTask task) {
		super(task);
	}

	@Override
	protected void createSrc() throws IOException {
		File src = new File(path, "main.c");
		FileUtils.write(task.getSrc(), src);
	}

	@Override
	protected String getCompilerCommand() {
		return compilerWord;
	}

	@Override
	protected String getRunCommand() {
		return runWord;
	}
}
