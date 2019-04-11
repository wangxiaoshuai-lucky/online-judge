package cn.wzy.onlinejudge.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultCase {
	private Integer status;
	private Long time;
	private Long memory;
	private String errorMessage;
}
