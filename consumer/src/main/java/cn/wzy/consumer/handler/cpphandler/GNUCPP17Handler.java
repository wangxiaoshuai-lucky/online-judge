package cn.wzy.consumer.handler.cpphandler;

import cn.wzy.consumer.handler.base.CHandler;
import cn.wzy.consumer.util.ExecutorUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GNUCPP17Handler extends CHandler {

	@Value("${judge.GNUCPP17}")
	private String compilerWord;

	@Override
	protected ExecutorUtil.ExecMessage HandlerCompiler(File path) {
		String cmd = compilerWord.replace("PATH",path.getPath());
		return ExecutorUtil.exec(cmd, 5000);
	}
}
