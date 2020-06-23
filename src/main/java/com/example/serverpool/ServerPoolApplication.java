package com.example.serverpool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableAsync
public class ServerPoolApplication implements WebMvcConfigurer {
	public static void main(String[] args) {
		SpringApplication.run(ServerPoolApplication.class, args);
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(mvcTaskExecutor());
	}

	@Bean
	public ThreadPoolTaskExecutor mvcTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(10);
		taskExecutor.setMaxPoolSize(100);
		taskExecutor.setQueueCapacity(50);
		taskExecutor.setAllowCoreThreadTimeOut(true);
		taskExecutor.setKeepAliveSeconds(120);
		taskExecutor.initialize();
		taskExecutor.setThreadNamePrefix("mvc-task-");

		return taskExecutor;
	}
}
