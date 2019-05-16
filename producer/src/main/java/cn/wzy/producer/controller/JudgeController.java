package cn.wzy.producer.controller;

import cn.wzy.producer.vo.JudgeResult;
import cn.wzy.producer.vo.JudgeTask;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Log4j
public class JudgeController {

	@Resource
	KafkaTemplate<String, String> kafkaTemplate;

	@PostMapping("/judge.do")
	public Object judge(@RequestBody JudgeTask task) {
		log.info("\n************" + "\n" +
			"\t收到任务,将回调到:" + task.getCallBack() + "\n" +
			"************");
		kafkaTemplate.send("judge", JSON.toJSONString(task));
		return "OK";
	}

	@PutMapping("/result.do")
	public String result(String key, Long submitId, @RequestBody JudgeResult result) {
		log.info("\n*****************" + "\n" +
			"\tkey: " + key + "\n" +
			"\tsubmitId: " + submitId + "\n" +
			"\tresult: " + result + "\n" +
			"*****************");
		return "OK";
	}

}
