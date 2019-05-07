package cn.wzy.onlinejudge;

import cn.wzy.onlinejudge.service.JudgeService;
import cn.wzy.onlinejudge.vo.JudgeResult;
import cn.wzy.onlinejudge.vo.JudgeTask;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OnlineJudgeApplication.class})
public class OnlineJudgeApplicationTests {

	@Autowired
	private JudgeService service;

	@Autowired
	KafkaTemplate kafkaTemplate;

	@Autowired
	private RestTemplate template;

	@Test
	public void restTest(){
		template.put("http://localhost:8080/result.do?key=111",new JudgeResult("adf",null));
	}

	@Test
	public void judgeTest() throws ExecutionException, InterruptedException {
		List<String> input = new ArrayList<>();
		input.add("111\n11\n");
		input.add("222\n22\n");
		input.add("333\n33\n");
		JudgeTask task = new JudgeTask(null, input, input, 1000l, 65535l, 1, "#include <stdio.h>\n" +
			"int main()\n" +
			"{\n" +
			"\tint a,b;\n" +
			"\tscanf(\"%d%d\",&a,&b);\n" +
			"\tint sum=0;\n" +
			"\tsum = a + b;\n" +
			"\tprintf(\"%d\",sum);\n" +
			"\treturn 0;\n" +
			"}","http://baidu.com");
		ListenableFuture send = kafkaTemplate.send("judge", "1", JSON.toJSONString(task));
		System.out.println(send.get());
	}



}
