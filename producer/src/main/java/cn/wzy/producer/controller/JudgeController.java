package cn.wzy.producer.controller;

import cn.wzy.producer.vo.JudgeResult;
import cn.wzy.producer.vo.JudgeTask;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Log4j
public class JudgeController {

	@Resource
	KafkaTemplate<String, String> kafkaTemplate;

	@PostMapping("/judge.do")
	public Object judge(@RequestBody JudgeTask task) {
		kafkaTemplate.send("judge", JSON.toJSONString(task));
		return "OK";
	}

	@PutMapping("/result.do")
	public String result(String key, Long submitId, @RequestBody JudgeResult result) {
		log.info("*****************");
		log.info("\tkey: " + key + "\n" +
			"\tsubmitId: " + submitId + "\n" +
			"\tresult: " + result);
		log.info("*****************");
		return "OK";
	}

}
