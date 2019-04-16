package cn.wzy.onlinejudge.handler.gcchandler;

import cn.wzy.onlinejudge.handler.base.CHandler;
import cn.wzy.onlinejudge.util.ExecutorUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GNUC99Handler extends CHandler {

	@Value("${judge.GNUC99}")
	private String compilerWord;

	@Override
	protected ExecutorUtil.ExecMessage HandlerCompiler(File path) {
		String cmd = compilerWord.replace("PATH",path.getPath());
		ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(cmd, 5000);
		return msg;
	}
}