package cn.wzy.producer.vo;


import java.util.List;

public class JudgeTask {

    private Integer proId;

    private List<String> input;

    private List<String> output;

    private Integer timeLimit;

    private Integer memoryLimit;

    private Integer judgeId;

    private String src;

    private String callBack;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public List<String> getInput() {
        return input;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }

    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public Integer getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(Integer judgeId) {
        this.judgeId = judgeId;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCallBack() {
        return callBack;
    }

    public void setCallBack(String callBack) {
        this.callBack = callBack;
    }
}
