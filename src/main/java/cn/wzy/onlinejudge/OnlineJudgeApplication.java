package cn.wzy.onlinejudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//@EnableEurekaClient
@SpringBootApplication
public class OnlineJudgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineJudgeApplication.class, args);
	}

}
