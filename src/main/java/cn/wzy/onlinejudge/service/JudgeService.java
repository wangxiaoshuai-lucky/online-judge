package cn.wzy.onlinejudge.service;

import cn.wzy.onlinejudge.util.FileUtils;
import cn.wzy.onlinejudge.vo.JudgeResult;
import cn.wzy.onlinejudge.vo.JudgeTask;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Log4j
@Service
public class JudgeService {

	@Value("${judgePath}")
	private String judgePath;
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
	private final int AC = 0;
	private final int PE = 1;
	private final int TLE = 2;
	private final int MLE = 3;
	private final int WE = 4;
	private final int RE = 5;
	private final int OLE = 6;
	private final int CE = 7;
	private final int SE = 8;

	private void endLog(long start, JudgeResult result) {
		log.info("=========结束判题：" + result + "=========");
		log.info("=========判题耗时：" + (System.currentTimeMillis() - start) / 1000 + " secends !=========");
	}

	public JudgeResult judge(JudgeTask task) {
		long start = System.currentTimeMillis();
		log.info("=========开始判题：" + task + "=========");

		JudgeResult result = new JudgeResult();
		//检验输入是否合法
		if (!checkTask(task, result)) {
			endLog(start, result);
			return result;
		}
		//mkdir
		File path = new File(judgePath + "/" + System.currentTimeMillis());

		return result;
	}


	private boolean createWorkspace(JudgeTask task, JudgeResult result, File path) {
		if (!path.exists()) {
			if (!path.mkdirs()) {
				result.setErrorMessage("服务器创建文件失败");
				result.setStatus(Arrays.asList(SE));
				return false;
			}
		}
		try {
			//create input and output
			for (int i = 0; i < task.getInput().size(); i++) {
				File inFile = new File(path, i + ".in");
				File outFile = new File(path, i + ".out");
				inFile.createNewFile();
				FileUtils.write(task.getInput().get(i), inFile);
				outFile.createNewFile();
				FileUtils.write(task.getOutput().get(i), outFile);
			}
			//create src
			File src = null;
			switch (task.getJudgeId()) {
				case 1:
				case 2:
				case 3:{
					src = new File(path,"main.c");
					break;
				}
			}
		} catch (IOException e) {
			result.setErrorMessage("服务器创建文件失败");
			result.setStatus(Arrays.asList(SE));
			return false;
		}
		return true;
	}

	/**
	 * 验证参数是否合法
	 *
	 * @param task   task.
	 * @param result result.
	 * @return bool.
	 */
	private boolean checkTask(JudgeTask task, JudgeResult result) {
		if (task.getJudgeId() == null || task.getJudgeId() < 0 || task.getJudgeId() > 5) {
			result.setErrorMessage("编译选项有误!");
			return false;
		}
		if (task.getInput() == null || task.getOutput() == null
			|| task.getInput().size() == 0 || task.getOutput().size() == 0) {
			result.setErrorMessage("测试数据不能为空!");
			return false;
		}
		if (task.getInput().size() != task.getOutput().size()) {
			result.setErrorMessage("测试数据组数不对应!");
			return false;
		}
		if (task.getSrc() == null || task.getSrc().trim().equals("")) {
			result.setErrorMessage("测试代码不能为空!");
			return false;
		}
		if (task.getTimeLimit() == null || task.getMemoryLimit() == null) {
			result.setErrorMessage("时间消耗、空间消耗不能为空!");
			return false;
		}
		if (task.getTimeLimit() < 0 || task.getTimeLimit() > 5000) {
			result.setErrorMessage("时间消耗应在范围0-5s内!");
			return false;
		}
		if (task.getMemoryLimit() < 0 || task.getMemoryLimit() > 524288) {
			result.setErrorMessage("空间消耗应在范围524288kb内!");
			return false;
		}
		return true;
	}
}
