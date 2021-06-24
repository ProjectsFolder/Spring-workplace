package com.example.demo.configuration;

import com.example.demo.task.AbstractTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    @Autowired
    private ApplicationContext context;

    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(4);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        var tasks = this.context.getBeansOfType(AbstractTask.class);
        for (var task : tasks.values()) {
            taskRegistrar.addTriggerTask(
                    task,
                    context -> {
                        Optional<Date> lastCompletionTime = Optional.ofNullable(context.lastCompletionTime());
                        Instant nextExecutionTime = lastCompletionTime
                                .orElseGet(Date::new)
                                .toInstant()
                                .plusMillis(task.getRate());

                        return Date.from(nextExecutionTime);
                    }
            );
        }
    }
}

//            Optional<Annotation> result = Arrays.stream(task.getClass().getAnnotations())
//                    .filter(a -> Task.class == a.annotationType())
//                    .findFirst();
//            if (result.isPresent()) {
//                Task annotation = (Task) result.get();
//                taskName = annotation.name();
//            }
//            if (taskName.isEmpty()) {
//                continue;
//            }
