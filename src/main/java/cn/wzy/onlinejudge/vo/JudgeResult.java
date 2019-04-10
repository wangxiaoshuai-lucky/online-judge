package cn.wzy.onlinejudge.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {
	private List<Integer> status;
	private Integer timeUsed;
	private Integer memoryUsed;
	private String errorMessage;
}
