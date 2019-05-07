package cn.wzy.onlinejudge.controller;

import cn.wzy.onlinejudge.vo.JudgeTask;
import com.alibaba.fastjson.JSON;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class JudgeController {

	@Resource
	KafkaTemplate<String, String> kafkaTemplate;

	@RequestMapping(value = "/judge.do", method = RequestMethod.POST)
	public Object judge(@RequestBody JudgeTask task) {
		kafkaTemplate.send("judge", JSON.toJSONString(task));
		return "OK";
	}

}
