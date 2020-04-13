package cn.wzy.producer.controller;

import cn.wzy.producer.vo.JudgeResult;
import cn.wzy.producer.vo.JudgeTask;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
public class JudgeController {


    private final Logger LOGGER = LoggerFactory.getLogger(JudgeController.class);

    @Resource
    KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/judge.do")
    public Object judge(@RequestBody JudgeTask task) {
        LOGGER.info("\n************" + "\n" +
                "\t收到任务,将回调到:" + task.getCallBack() + "\n" +
                "************");
        kafkaTemplate.send("judge", JSON.toJSONString(task));
        return "OK";
    }

    @PutMapping("/result.do")
    public String result(String key, Long submitId, @RequestBody JudgeResult result) {
        LOGGER.info("\n*****************" + "\n" +
                "\tkey: " + key + "\n" +
                "\tsubmitId: " + submitId + "\n" +
                "\tresult: " + result + "\n" +
                "*****************");
        return "OK";
    }

}
