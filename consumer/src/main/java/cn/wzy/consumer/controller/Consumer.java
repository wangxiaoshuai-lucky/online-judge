package cn.wzy.consumer.controller;

import cn.wzy.consumer.service.JudgeService;
import cn.wzy.consumer.vo.JudgeResult;
import cn.wzy.consumer.vo.JudgeTask;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class Consumer {

    private final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private final RestTemplate template;

    private final JudgeService service;

    public Consumer(JudgeService service,
                    RestTemplate template) {
        this.service = service;
        this.template = template;
    }

    @KafkaListener(topics = {"judge"})
    public void listen(ConsumerRecord<String, String> record, Acknowledgment ack) {
        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            try {
                String message = kafkaMessage.get();
                JudgeTask task = JSON.parseObject(message, JudgeTask.class);
                JudgeResult result = service.judge(task);
                try {
                    template.put(task.getCallBack(), result);
                } catch (Exception e) {
                    LOGGER.error("CallBack is wrong : " + task.getCallBack());
                }
            } catch (Exception e) {
                LOGGER.error("判题出现错误：" + e.getCause(), e);
            } finally {
                ack.acknowledge();
            }
        }
    }
}
