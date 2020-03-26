package cn.wepact.dfm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(basePackages = "cn.wepact.dfm")
@EnableFeignClients(basePackages = {"cn.wepact", "cn.wepact"})
public class KnowledgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowledgeApplication.class, args);
	}

}
