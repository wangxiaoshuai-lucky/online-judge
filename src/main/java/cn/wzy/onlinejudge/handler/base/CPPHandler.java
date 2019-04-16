package cn.wzy.onlinejudge.handler.base;

import cn.wzy.onlinejudge.util.FileUtils;
import cn.wzy.onlinejudge.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

public abstract class CPPHandler extends Handler {

	@Value("${judge.Crun}")
	private String runWord;

	@Override
	protected void createSrc(JudgeTask task, File path) throws IOException {
		File src = new File(path, "main.cpp");
		FileUtils.write(task.getSrc(), src);
	}

	@Override
	protected String getRunCommand(File path) {
		return runWord.replace("PATH", path.getPath());
	}
}
