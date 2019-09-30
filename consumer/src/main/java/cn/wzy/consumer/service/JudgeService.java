package cn.wzy.consumer.service;

import cn.wzy.consumer.handler.*;
import cn.wzy.consumer.handler.base.Handler;
import cn.wzy.consumer.handler.cpphandler.GNUCPP11Handler;
import cn.wzy.consumer.handler.cpphandler.GNUCPP14Handler;
import cn.wzy.consumer.handler.cpphandler.GNUCPP17Handler;
import cn.wzy.consumer.handler.cpphandler.GNUCPP98Handler;
import cn.wzy.consumer.handler.gcchandler.GNUC11Handler;
import cn.wzy.consumer.handler.gcchandler.GNUC90Handler;
import cn.wzy.consumer.handler.gcchandler.GNUC99Handler;
import cn.wzy.consumer.vo.JudgeResult;
import cn.wzy.consumer.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Autowired
	private MonoHandler monoHandler;

	@Autowired
	private RubyHandler rubyHandler;

	@Autowired
	private GoHandler goHandler;

	public JudgeResult judge(JudgeTask task) {
		JudgeResult result;
		if (task.getJudgeId() == null || task.getJudgeId() < 1 || task.getJudgeId() > 14) {
			result = new JudgeResult("编译选项有误!", null);
		} else {
			Handler handler;
			switch (task.getJudgeId()) {
				case 2:
					handler = gnuc99Handler;
					break;
				case 3:
					handler = gnuc11Handler;
					break;
				case 4:
					handler = gnucpp98Handler;
					break;
				case 5:
					handler = gnucpp11Handler;
					break;
				case 6:
					handler = gnucpp14Handler;
					break;
				case 7:
					handler = gnucpp17Handler;
					break;
				case 8:
					handler = javaHandler;
					break;
				case 9:
					handler = py2Handler;
					break;
				case 10:
					handler = py3Handler;
					break;
				case 11:
					handler = jsHandler;
					break;
				case 12:
					handler = monoHandler;
					break;
				case 13:
					handler = rubyHandler;
					break;
				case 14:
					handler = goHandler;
					break;
				default:
					handler = gnuc90Handler;
			}
			result = handler.judge(task);
		}
		return result;
	}
}
