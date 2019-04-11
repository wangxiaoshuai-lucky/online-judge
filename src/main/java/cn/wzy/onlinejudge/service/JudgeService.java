package cn.wzy.onlinejudge.service;

import cn.wzy.onlinejudge.handler.Handler;
import cn.wzy.onlinejudge.vo.JudgeResult;
import cn.wzy.onlinejudge.vo.JudgeTask;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class JudgeService {

	public JudgeResult judge(JudgeTask task) {
		long start = System.currentTimeMillis();
		log.info("=========开始判题：" + task + "=========");
		Handler judgeHandler = null;
		JudgeResult result = judgeHandler.judge();
		log.info("=========结束判题：" + result + "=========");
		log.info("=========判题耗时：" + (System.currentTimeMillis() - start) / 1000 + " secends !=========");
		return result;
	}

	public static void main(String[] args) {
		List<String> input = new ArrayList<>();
		input.add("111\n11\n");
		input.add("222\n22\n");
		input.add("333\n33\n");
		JudgeTask task = new JudgeTask(input,input,65535l,1000l,1,"hello world");
	}
}
