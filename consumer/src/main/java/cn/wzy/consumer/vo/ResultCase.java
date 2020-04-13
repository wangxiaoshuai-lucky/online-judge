package cn.wzy.consumer.vo;

public class ResultCase {

    private Integer status;

    private Integer timeUsed;

    private Integer memoryUsed;

    private String errorMessage;

	public ResultCase(Integer status, Integer timeUsed, Integer memoryUsed, String errorMessage) {
		this.status = status;
		this.timeUsed = timeUsed;
		this.memoryUsed = memoryUsed;
		this.errorMessage = errorMessage;
	}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(Integer timeUsed) {
        this.timeUsed = timeUsed;
    }

    public Integer getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Integer memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
