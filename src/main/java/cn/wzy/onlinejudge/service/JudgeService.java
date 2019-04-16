package cn.wzy.onlinejudge.service;

import cn.wzy.onlinejudge.handler.*;
import cn.wzy.onlinejudge.handler.base.Handler;
import cn.wzy.onlinejudge.handler.cpphandler.*;
import cn.wzy.onlinejudge.handler.gcchandler.*;
import cn.wzy.onlinejudge.vo.JudgeResult;
import cn.wzy.onlinejudge.vo.JudgeTask;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j
@Service
public class JudgeService {

	@Autowired
	private GNUC90Handler gnuc90Handler;

	@Autowired
	private GNUC99Handler gnuc99Handler;

	@Autowired
	private GNUC11Handler gnuc11Handler;

	@Autowired
	private GNUCPP98Handler gnucpp98Handler;

	@Autowired
	private GNUCPP11Handler gnucpp11Handler;

	@Autowired
	private GNUCPP14Handler gnucpp14Handler;

	@Autowired
	private GNUCPP17Handler gnucpp17Handler;

	@Autowired
	private Py2Handler py2Handler;

	@Autowired
	private Py3Handler py3Handler;

	@Autowired
	private JavaHandler javaHandler;

	@Autowired
	private JSHandler jsHandler;


	public JudgeResult judge(JudgeTask task) {
		long start = System.currentTimeMillis();
		log.info("=========开始判题=========");
		JudgeResult result;
		if (task.getJudgeId() == null || task.getJudgeId() < 1 || task.getJudgeId() > 12) {
			result = new JudgeResult("编译选项有误!",null);
		} else {
			Handler handler;
			switch (task.getJudgeId()){
				case 1:handler = gnuc90Handler;break;
				case 2:handler = gnuc99Handler;break;
				case 3:handler = gnuc11Handler;break;
				case 4:handler = gnucpp98Handler;break;
				case 5:handler = gnucpp11Handler;break;
				case 6:handler = gnucpp14Handler;break;
				case 7:handler = gnucpp17Handler;break;
				case 8:handler = javaHandler;break;
				case 9:handler = py2Handler;break;
				case 10:handler = py3Handler;break;
				case 11:handler = jsHandler;break;
				case 12:handler = javaHandler;break;
				default:
					handler = gnuc90Handler;
			}
			result = handler.judge(task);
		}
		log.info("=========结束判题：" + result + "=========");
		log.info("=========判题耗时：" + (System.currentTimeMillis() - start) / 1000 + " secends !=========");
		return result;
	}
}
