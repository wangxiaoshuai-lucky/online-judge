package cn.wzy.consumer.handler.base;


import cn.wzy.consumer.service.Security;
import cn.wzy.consumer.util.ExecutorUtil;
import cn.wzy.consumer.util.FileUtils;
import cn.wzy.consumer.util.ZipUtils;
import cn.wzy.consumer.vo.JudgeResult;
import cn.wzy.consumer.vo.JudgeTask;
import cn.wzy.consumer.vo.ResultCase;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	protected final int WA = 4;
	protected final int RE = 5;
	protected final int OLE = 6;
	protected final int CE = 7;
	protected final int SE = 8;

	@Value("${judge.judgePath}")
	private String judgePath;

	@Value("${judge.scriptPath}")
	private String script;

	@Value("${judge.download}")
	private String download;

	@Autowired
	private Security security;

	/**
	 * 验证参数是否合法
	 *
	 * @param task   task.
	 * @param result result.
	 * @return bool.
	 */
	private boolean checkTask(JudgeTask task, JudgeResult result) {
		if (task.getProId() == null) {
			if (task.getInput() == null || task.getOutput() == null
				|| task.getInput().size() == 0 || task.getOutput().size() == 0) {
				result.setGlobalMsg("测试数据不能为空!");
				return false;
			}
			if (task.getInput().size() != task.getOutput().size()) {
				result.setGlobalMsg("测试数据组数不对应!");
				return false;
			}
		}
		if (task.getCallBack() == null) {
			result.setGlobalMsg("CallBack为空!");
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
	 * 编译（模板方法）
	 *
	 * @param path
	 * @return
	 */
	protected abstract ExecutorUtil.ExecMessage HandlerCompiler(File path);

	/**
	 * 运行命令（模板方法）
	 *
	 * @param path
	 * @return
	 */
	protected abstract String getRunCommand(File path);

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
			if (task.getProId() == null) {//create input and output
				for (int i = 0; i < task.getInput().size(); i++) {
					File inFile = new File(path, i + ".in");
					File outFile = new File(path, i + ".out");
					inFile.createNewFile();
					FileUtils.write(task.getInput().get(i), inFile);
					outFile.createNewFile();
					FileUtils.write(task.getOutput().get(i), outFile);
				}
			} else {//download the testData
				String param = download.replace("{ProId}", task.getProId().toString()).replace("PATH", path.getPath());
				ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(param, 5000);
				if (msg.getError() == null || !msg.getError().contains("0K")) {
					throw new IOException("文件目录出错！");
				}
				ZipUtils.unzip(path.getPath() + File.separator + "main.zip", path.getPath());
			}
			createSrc(task, path);
		} catch (IOException e) {
			result.setGlobalMsg("服务器工作目录出错:" + e);
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
		ExecutorUtil.ExecMessage msg = HandlerCompiler(path);
		if (msg.getError() != null) {
			result.setGlobalMsg(msg.getError());
			return false;
		}
		return true;
	}

	/**
	 * 测试源程序
	 *
	 * @param task
	 * @param result
	 * @param path
	 */
	private void runSrc(JudgeTask task, JudgeResult result, File path) {
		//cmd : command timeLimit memoryLimit inFile tmpFile
		String cmd = "script process timeLimit memoryLimit inputFile tmpFile";
		cmd = cmd.replace("script", script);
		cmd = cmd.replace("process", getRunCommand(path).replace(" ", "@"));
		cmd = cmd.replace("timeLimit", task.getTimeLimit().toString());
		cmd = cmd.replace("memoryLimit", task.getMemoryLimit().toString());
		cmd = cmd.replace("tmpFile", path.getPath() + File.separator + "tmp.out");
		List<ResultCase> cases = new ArrayList<>();
		for (int i = 0; ; i++) {
			File inFile = new File(path.getPath() + File.separator + i + ".in");
			File outFile = new File(path.getPath() + File.separator + i + ".out");
			if (!inFile.exists() || !outFile.exists()) {
				break;
			}
			System.out.println(cmd.replace("inputFile", inFile.getPath()));
			ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(cmd.replace("inputFile", inFile.getPath()), 50000);
			ResultCase caseOne = JSON.parseObject(msg.getStdout(), ResultCase.class);
			if (caseOne == null) {
				caseOne = new ResultCase(SE, -1, -1, null);
			}
			if (caseOne.getStatus() == AC) {
				diff(caseOne, new File(path.getPath() + File.separator + "tmp.out"), outFile);
			}
			//运行报错
			if (msg.getError() != null) {
				caseOne.setStatus(RE);
				caseOne.setMemoryUsed(-1);
				caseOne.setTimeUsed(-1);
				caseOne.setErrorMessage(msg.getError());
			}
			cases.add(caseOne);
			ExecutorUtil.exec("rm " + path.getPath() + File.separator + "tmp.out", 1000);
		}
		result.setResult(cases);
	}

	/**
	 * 比对结果
	 * @param caseOne
	 * @param tmpOut
	 * @param stdOut
	 */
	public void diff(ResultCase caseOne, File tmpOut, File stdOut) {
		String tem = FileUtils.read(tmpOut);
		String std = FileUtils.read(stdOut);
		if (tem.equals(std)) {
			caseOne.setStatus(AC);
			return;
		}
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		for (int i = 0; i < tem.length(); i++) {
			if (tem.charAt(i) != ' ' && tem.charAt(i) != '\n') {
				sb1.append(tem.charAt(i));
			}
		}
		for (int i = 0; i < std.length(); i++) {
			if (std.charAt(i) != ' ' && std.charAt(i) != '\n') {
				sb2.append(std.charAt(i));
			}
		}
		if (sb1.toString().equals(sb2.toString())) {
			caseOne.setStatus(PE);
		} else {
			caseOne.setStatus(WA);
		}
	}

	/**
	 * 判题主流程
	 *
	 * @param task
	 * @return
	 */
	public JudgeResult judge(JudgeTask task) {
		JudgeResult result = new JudgeResult();
		//检验输入是否合法
		if (!checkTask(task, result)) {
			return result;
		}
		if (!security.checkSecurity(task, result)) {
			return result;
		}
		//创建工作目录
		File path = new File(judgePath + File.separator + System.currentTimeMillis());
		if (!createWorkspace(task, result, path)) {
			ExecutorUtil.exec("rm -rf " + path.getPath(), 1000);
			return result;
		}
		//编译
		if (!compiler(result, path)) {
			ExecutorUtil.exec("rm -rf " + path.getPath(), 1000);
			return result;
		}
		runSrc(task, result, path);
		ExecutorUtil.exec("rm -rf " + path.getPath(), 1000);
		return result;
	}

}
