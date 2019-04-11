package cn.wzy.onlinejudge.service;

import cn.wzy.onlinejudge.handler.*;
import cn.wzy.onlinejudge.vo.JudgeResult;
import cn.wzy.onlinejudge.vo.JudgeTask;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j
@Service
public class JudgeService {

	@Autowired
	private CHandler cHandler;

	@Autowired
	private CPPHandler cppHandler;

	@Autowired
	private Py2Handler py2Handler;

	@Autowired
	private Py3Handler py3Handler;

	@Autowired
	private JavaHandler javaHandler;

	public JudgeResult judge(JudgeTask task) {
		long start = System.currentTimeMillis();
		log.info("=========开始判题=========");
		JudgeResult result;
		if (task.getJudgeId() == null || task.getJudgeId() < 1 || task.getJudgeId() > 5) {
			result = new JudgeResult("编译选项有误!",null);
		} else {
			Handler handler = null;
			switch (task.getJudgeId()){
				case 1:handler = cHandler;break;
				case 2:handler = cppHandler;break;
				case 3:handler = javaHandler;break;
				case 4:handler = py2Handler;break;
				case 5:handler = py3Handler;break;
				default:
					handler = cHandler;
			}
			result = handler.judge(task);
		}
		log.info("=========结束判题：" + result + "=========");
		log.info("=========判题耗时：" + (System.currentTimeMillis() - start) / 1000 + " secends !=========");
		return result;
	}
}
