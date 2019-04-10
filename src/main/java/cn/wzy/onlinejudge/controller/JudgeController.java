package cn.wzy.onlinejudge.controller;

import cn.wzy.onlinejudge.service.JudgeService;
import cn.wzy.onlinejudge.vo.JudgeResult;
import cn.wzy.onlinejudge.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JudgeController {

	@Autowired
	private JudgeService service;

	@RequestMapping(value = "/judge.do", method = RequestMethod.POST)
	public JudgeResult judge(@RequestBody JudgeTask task) {
		return service.judge(task);
	}

}
