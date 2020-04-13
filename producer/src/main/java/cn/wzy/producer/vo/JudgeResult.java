package cn.wzy.producer.vo;

import java.util.List;

public class JudgeResult {

    private String globalMsg;

    private List<ResultCase> result;

	public JudgeResult() {
	}

	public JudgeResult(String globalMsg, List<ResultCase> result) {
		this.globalMsg = globalMsg;
		this.result = result;
	}

	public String getGlobalMsg() {
        return globalMsg;
    }

    public void setGlobalMsg(String globalMsg) {
        this.globalMsg = globalMsg;
    }

    public List<ResultCase> getResult() {
        return result;
    }

    public void setResult(List<ResultCase> result) {
        this.result = result;
    }
}
