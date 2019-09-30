package cn.wzy.consumer.controller;

import cn.wzy.consumer.service.JudgeService;
import cn.wzy.consumer.vo.JudgeResult;
import cn.wzy.consumer.vo.JudgeTask;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j
public class TestController {

	@Autowired
	private JudgeService service;

	@PostMapping("/judge.do")
	public JudgeResult judge(@RequestBody JudgeTask task) {
		return service.judge(task);
	}
}
