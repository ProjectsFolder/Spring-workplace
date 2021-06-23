package com.example.demo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class OtherTestTask extends AbstractTask {

    private static final Logger log = LoggerFactory.getLogger(OtherTestTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void execute() {
        log.info("OTHER The time is now {}", dateFormat.format(new Date()));
    }
}
