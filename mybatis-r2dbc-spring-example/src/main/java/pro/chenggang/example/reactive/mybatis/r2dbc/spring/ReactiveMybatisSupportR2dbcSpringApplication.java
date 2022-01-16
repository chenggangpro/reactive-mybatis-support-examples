package pro.chenggang.example.reactive.mybatis.r2dbc.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@Slf4j
@SpringBootApplication
public class ReactiveMybatisSupportR2dbcSpringApplication {

	public static void main(String[] args) {
		if(log.isDebugEnabled()){
			Hooks.onOperatorDebug();
		}
		SpringApplication.run(ReactiveMybatisSupportR2dbcSpringApplication.class, args);
	}

}
