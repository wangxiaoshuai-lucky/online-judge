package cn.wzy.onlinejudge.handler;

import cn.wzy.onlinejudge.handler.base.Handler;
import cn.wzy.onlinejudge.util.ExecutorUtil;
import cn.wzy.onlinejudge.util.FileUtils;
import cn.wzy.onlinejudge.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JSHandler extends Handler {

	@Value("${judge.JSrun}")
	private String runWord;

	@Override
	protected void createSrc(JudgeTask task, File path) throws IOException {
		File src = new File(path, "main.js");
		FileUtils.write(task.getSrc(), src);
	}

	@Override
	protected ExecutorUtil.ExecMessage HandlerCompiler(File path) {
		String cmd = runWord.replace("PATH",path.getPath());
		ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(cmd, 500);
		if (msg.getError() == null || msg.getError().equals("timeOut")){
			msg.setError(null);
		}
		return msg;
	}

	@Override
	protected String getRunCommand(File path) {
		return runWord.replace("PATH",path.getPath());
	}
}
