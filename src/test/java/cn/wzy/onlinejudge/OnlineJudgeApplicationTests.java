package cn.wzy.onlinejudge;

import cn.wzy.onlinejudge.service.JudgeService;
import cn.wzy.onlinejudge.vo.JudgeTask;
import cn.wzy.onlinejudge.vo.ResultCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OnlineJudgeApplicationTests {

	@Autowired
	private JudgeService service;

	@Test
	public void judgeTest() {
		List<String> input = new ArrayList<>();
		input.add("111\n11\n");
		input.add("222\n22\n");
		input.add("333\n33\n");
		JudgeTask task = new JudgeTask(input,input,1000l,65535l,1,"#include <stdio.h>\n" +
			"int main()\n" +
			"{\n" +
			"\tint a,b;\n" +
			"\tscanf(\"%d%d\",&a,&b);\n" +
			"\tint sum=0;\n" +
			"\tsum = a + b;\n" +
			"\tprintf(\"%d\",sum);\n" +
			"\treturn 0;\n" +
			"}");
		System.out.println(service.judge(task));
		task.setJudgeId(2);
		System.out.println(service.judge(task));
		task.setJudgeId(3);
		System.out.println(service.judge(task));
		task.setJudgeId(4);
		System.out.println(service.judge(task));
		task.setJudgeId(5);
		System.out.println(service.judge(task));
		task.setJudgeId(6);
		System.out.println(service.judge(task));
		task.setJudgeId(7);
		System.out.println(service.judge(task));
	}
}
