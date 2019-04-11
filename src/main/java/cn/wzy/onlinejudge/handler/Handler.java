package cn.wzy.onlinejudge.handler;

import cn.wzy.onlinejudge.util.ExecutorUtil;
import cn.wzy.onlinejudge.util.FileUtils;
import cn.wzy.onlinejudge.vo.JudgeResult;
import cn.wzy.onlinejudge.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

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

	@Value("${judgePath}")
	private String judgePath;

	protected File path;

	protected JudgeTask task;


	public Handler(JudgeTask task) {
		this.task = task;
		this.path = new File(judgePath + "/" + System.currentTimeMillis());
	}

	/**
	 * 验证参数是否合法
	 *
	 * @param result result.
	 * @return bool.
	 */
	private boolean checkTask(JudgeResult result) {
		if (task.getJudgeId() == null || task.getJudgeId() < 0 || task.getJudgeId() > 5) {
			result.setGlobalMsg("编译选项有误!");
			return false;
		}
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
	protected abstract void createSrc() throws IOException;

	/**
	 * 创建工作目录
	 *
	 * @param result
	 * @return
	 */
	private boolean createWorkspace(JudgeResult result) {
		try {
			if (!path.exists())
				if (!path.mkdirs())
					//create input and output
					for (int i = 0; i < task.getInput().size(); i++) {
						File inFile = new File(path, i + ".in");
						File outFile = new File(path, i + ".out");
						inFile.createNewFile();
						FileUtils.write(task.getInput().get(i), inFile);
						outFile.createNewFile();
						FileUtils.write(task.getOutput().get(i), outFile);
					}
			createSrc();
		} catch (IOException e) {
			result.setGlobalMsg("服务器工作目录出错");
			return false;
		}
		return true;
	}

	private boolean compiler(JudgeResult result){
		ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(getCompilerCommand());
		if (msg.getError() != null){
			result.setGlobalMsg(msg.getError());
			return false;
		}
		return true;
	}

	protected abstract String getCompilerCommand();

	protected abstract String getRunCommand();

	private void runSrc(JudgeResult result) {
		ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(getRunCommand());
		// TODO: 2019/4/11 返回运行结果
	}

	/**
	 * judge主流程
	 *
	 * @return
	 */
	public JudgeResult judge() {
		JudgeResult result = new JudgeResult();
		//检验输入是否合法
		if (!checkTask(result)) {
			return result;
		}
		if (!createWorkspace(result)) {
			return result;
		}
		//编译
		if(!compiler(result)){
				return result;
		}
		return result;
	}


}
