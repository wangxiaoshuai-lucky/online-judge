package cn.wzy.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConsumerApplication.class)
public class ConsumerApplicationTests {

	@Resource
	KafkaTemplate<String, String> kafkaTemplate;

	@Test
	public void contextLoads() throws InterruptedException, ExecutionException {
		Thread.sleep(5000);
		for (int i = 0; i < 10; i++) {
			ListenableFuture<SendResult<String, String>> judge = kafkaTemplate.send("judge", "newdata" + i);
			System.out.println("发送消息：" + judge.get());
		}
		while (true) {

		}
	}

}
