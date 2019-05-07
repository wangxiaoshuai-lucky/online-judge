package cn.wzy.onlinejudge.controller;

import cn.wzy.onlinejudge.service.JudgeService;
import cn.wzy.onlinejudge.vo.JudgeResult;
import cn.wzy.onlinejudge.vo.JudgeTask;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Log4j
public class Consumer {

	@Autowired
	private RestTemplate template;

	@Autowired
	private JudgeService service;

	@KafkaListener(topics = {"judge"})
	public void listen(ConsumerRecord<String, String> record) {
		Optional<String> kafkaMessage = Optional.ofNullable(record.value());
		if (kafkaMessage.isPresent()) {
			String message = kafkaMessage.get();
			JudgeTask task = JSON.parseObject(message, JudgeTask.class);
			long start = System.currentTimeMillis();
			log.info("====开始判题====");
			JudgeResult result = service.judge(task);
			try {
				template.put(task.getCallBack(), result);
			} catch (Exception e) {
				log.error("CallBack is wrong : " + task.getCallBack());
			}
			log.info("====结束判题====");
			log.info("*\t" + result);
			log.info("====判题耗时：" + (System.currentTimeMillis() - start) / 1000 + " secends !=========");
		}
	}
}
