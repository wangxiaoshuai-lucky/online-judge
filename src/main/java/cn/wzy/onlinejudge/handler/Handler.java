package cn.wzy.onlinejudge.handler;

import cn.wzy.onlinejudge.util.ExecutorUtil;
import cn.wzy.onlinejudge.util.FileUtils;
import cn.wzy.onlinejudge.vo.JudgeResult;
import cn.wzy.onlinejudge.vo.JudgeTask;
import cn.wzy.onlinejudge.vo.ResultCase;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base Handler
 */
public abstract class Handler {
	/**
	 * 'Accepted',
	 * 'Presentation Error',
	 * 'Time Limit Exceeded',
	 * 'Memory Limit Exceeded',
	 * 'Wrong Answer',
	 * 'Runtime Error',
	 * 'Output Limit Exceeded',
	 * 'Compile Error',
	 * 'System Error'
	 */
	protected final int AC = 0;
	protected final int PE = 1;
	protected final int TLE = 2;
	protected final int MLE = 3;
	protected final int WE = 4;
	protected final int RE = 5;
	protected final int OLE = 6;
	protected final int CE = 7;
	protected final int SE = 8;

	@Value("${judge.judgePath}")
	private String judgePath;

	@Value("${judge.scriptPath}")
	private String script;
	/**
	 * 验证参数是否合法
	 *
	 * @param task   task.
	 * @param result result.
	 * @return bool.
	 */
	private boolean checkTask(JudgeTask task, JudgeResult result) {
		if (task.getInput() == null || task.getOutput() == null
			|| task.getInput().size() == 0 || task.getOutput().size() == 0) {
			result.setGlobalMsg("测试数据不能为空!");
			return false;
		}
		if (task.getInput().size() != task.getOutput().size()) {
			result.setGlobalMsg("测试数据组数不对应!");
			return false;
		}
		if (task.getSrc() == null || task.getSrc().trim().equals("")) {
			result.setGlobalMsg("测试代码不能为空!");
			return false;
		}
		if (task.getTimeLimit() == null || task.getMemoryLimit() == null) {
			result.setGlobalMsg("时间消耗、空间消耗不能为空!");
			return false;
		}
		if (task.getTimeLimit() < 0 || task.getTimeLimit() > 5000) {
			result.setGlobalMsg("时间消耗应在范围0-5s内!");
			return false;
		}
		if (task.getMemoryLimit() < 0 || task.getMemoryLimit() > 524288) {
			result.setGlobalMsg("空间消耗应在范围524288kb内!");
			return false;
		}
		return true;
	}

	/**
	 * （模板方法）创建对应的源程序
	 *
	 * @throws IOException
	 */
	protected abstract void createSrc(JudgeTask task, File path) throws IOException;

	/**
	 * 创建工作目录
	 *
	 * @param task
	 * @param result
	 * @param path
	 * @return
	 */
	private boolean createWorkspace(JudgeTask task, JudgeResult result, File path) {
		try {
			if (!path.exists())
				path.mkdirs();
			//create input and output
			for (int i = 0; i < task.getInput().size(); i++) {
				File inFile = new File(path, i + ".in");
				File outFile = new File(path, i + ".out");
				inFile.createNewFile();
				FileUtils.write(task.getInput().get(i), inFile);
				outFile.createNewFile();
				FileUtils.write(task.getOutput().get(i), outFile);
			}
			createSrc(task, path);
		} catch (IOException e) {
			result.setGlobalMsg("服务器工作目录出错");
			return false;
		}
		return true;
	}

	/**
	 * 编译程序
	 *
	 * @param result
	 * @param path
	 * @return
	 */
	private boolean compiler(JudgeResult result, File path) {
		String cmd = getCompilerCommand(path);
		ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(cmd, 5000);
		if (msg.getError() != null) {
			result.setGlobalMsg(msg.getError());
			return false;
		}
		return true;
	}

	/**
	 * 编译命令（模板方法）
	 *
	 * @param path
	 * @return
	 */
	protected abstract String getCompilerCommand(File path);

	/**
	 * 运行命令（模板方法）
	 *
	 * @param path
	 * @return
	 */
	protected abstract String getRunCommand(File path);

	private void runSrc(JudgeTask task, JudgeResult result, File path) {
		Map<String,Object> param = new HashMap<>();
		param.put("path",path.getPath());
		param.put("cmd",getRunCommand(path));
		param.put("time",task.getTimeLimit());
		param.put("memory",task.getMemoryLimit());
		String cmd = "python " + script + " \"" + JSON.toJSONString(param) + "\"";
		ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(cmd, 50000);
		List<ResultCase> cases = JSON.parseArray(msg.getStdout(), ResultCase.class);
		result.setResult(cases);
		result.setGlobalMsg(msg.getError());
		// TODO: 2019/4/11 返回运行结果
	}

	public JudgeResult judge(JudgeTask task) {
		JudgeResult result = new JudgeResult();
		//检验输入是否合法
		if (!checkTask(task, result)) {
			return result;
		}
		//创建工作目录
		File path = new File(judgePath + File.separator + System.currentTimeMillis());
		if (!createWorkspace(task, result, path)) {
			return result;
		}
		//编译
		if (!compiler(result, path)) {
			return result;
		}
		runSrc(task, result,path);
		return result;
	}


}
