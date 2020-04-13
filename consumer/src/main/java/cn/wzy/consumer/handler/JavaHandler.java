package cn.wzy.consumer.handler;

import cn.wzy.consumer.handler.base.Handler;
import cn.wzy.consumer.util.ExecutorUtil;
import cn.wzy.consumer.util.FileUtils;
import cn.wzy.consumer.vo.JudgeTask;
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
	protected ExecutorUtil.ExecMessage HandlerCompiler(File path) {
		String cmd = compilerWord.replace("PATH",path.getPath());
		return ExecutorUtil.exec(cmd, 2000);
	}

	@Override
	protected String getRunCommand(File path) {
		return runWord.replace("PATH",path.getPath());
	}
}
