package cn.wzy.onlinejudge;

import cn.wzy.onlinejudge.handler.CHandler;
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
		JudgeTask task = new JudgeTask(input,input,1000l,65535l,3,"public class Main{\n" +
			"\tpublic static void main(String[] args) {\n" +
			"\t\tSystem.out.println(\"hello world\");\n" +
			"\t}\n" +
			"}\n");
		System.out.println(service.judge(task));
	}

	@Test
	public void test() throws URISyntaxException {
		List<ResultCase> list = new ArrayList<>();
		list.add(new ResultCase(1,100l,500l,"sdf"));
		list.add(new ResultCase(1,100l,500l,"sdf"));
		list.add(new ResultCase(1,100l,500l,"sdf"));
	}
}
