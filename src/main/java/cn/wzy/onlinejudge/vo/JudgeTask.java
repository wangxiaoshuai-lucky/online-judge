package cn.wzy.onlinejudge.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeTask {
	private Integer proId;
	private List<String> input;
	private List<String> output;
	private Long timeLimit;
	private Long memoryLimit;
	private Integer judgeId;
	private String src;
	private String callBack;
}
