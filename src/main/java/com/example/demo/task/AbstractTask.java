package com.example.demo.task;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractTask implements Runnable {

    @Autowired
    private TaskRepository taskRepository;

    public abstract void execute();

    @Override
    public final void run() {
        if (0 != this.getConfig().getActive()) {
            execute();
        }
    }

    public final Long getRate()
    {
        return this.getConfig().getRate();
    }

    private Task getConfig()
    {
        String taskName = this.getClass().getName();
        Task taskConfig = this.taskRepository.findByName(taskName);
        if (null == taskConfig) {
            taskConfig = new Task();
            taskConfig.setName(taskName);
            taskConfig.setRate(10000L);
            taskRepository.save(taskConfig);
        }

        return taskConfig;
    }
}
