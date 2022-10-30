package com.example.mailbackend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

//    private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);
//
//    private final TaskExecutionProperties taskExecutionProperties;
//
//    public AsyncConfiguration(TaskExecutionProperties taskExecutionProperties) {
//        this.taskExecutionProperties = taskExecutionProperties;
//    }
//
//    @Override
//    @Bean(name = "asyncTaskExecutor")
//    public Executor getAsyncExecutor() {
//        log.debug("Creating Async Task Executor");
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(2);
//        executor.setMaxPoolSize(200);
//        executor.setQueueCapacity(10000);
//        executor.setThreadNamePrefix("mail-receiver-");
//        return executor;
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return new SimpleAsyncUncaughtExceptionHandler();
//    }
}
