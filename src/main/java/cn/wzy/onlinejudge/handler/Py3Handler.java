package cn.wzy.onlinejudge.handler;

import cn.wzy.onlinejudge.util.FileUtils;
import cn.wzy.onlinejudge.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class Py3Handler extends Handler {
	@Value("${judge.Python3word}")
	private String compilerWord;

	@Value("${judge.Python3run}")
	private String runWord;

	@Override
	protected void createSrc(JudgeTask task, File path) throws IOException {
		File src = new File(path, "main.py");
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
