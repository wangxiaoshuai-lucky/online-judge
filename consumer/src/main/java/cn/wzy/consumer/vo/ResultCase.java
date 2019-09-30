package cn.wzy.consumer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultCase {
	private Integer status;
	private Long timeUsed;
	private Long memoryUsed;
	private String errorMessage;
}
