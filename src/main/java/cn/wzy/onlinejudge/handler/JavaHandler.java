package cn.wzy.onlinejudge.handler;

import cn.wzy.onlinejudge.util.FileUtils;
import cn.wzy.onlinejudge.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JavaHandler extends Handler {

	@Value("${judge.Javaword}")
	private String compilerWord;

	@Value("${judge.Javarun}")
	private String runWord;

	@Override
	protected void createSrc(JudgeTask task, File path) throws IOException {
		File src = new File(path, "Main.java");
		FileUtils.write(task.getSrc(), src);
	}

	@Override
	protected String getCompilerCommand(File path) {
		return compilerWord.replace("PATH",path.getPath());
	}

	@Override
	protected String getRunCommand(File path) {
		return runWord.replace("PATH",path.getPath());
	}
}
