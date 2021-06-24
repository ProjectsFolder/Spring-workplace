package com.example.demo.task;

import com.example.demo.service.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TestTask extends AbstractTask {

    private static final Logger log = LoggerFactory.getLogger(TestTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private BillingService billingService;

    @Override
    public void execute() {
        String title = null;
//        @SuppressWarnings("OptionalGetWithoutIsPresent")
//        var map = billingService.getContract(102127).stream().findFirst().get();
//        title = (String) map.get("title");

        log.info("The time is now {}, {}", dateFormat.format(new Date()), title);
    }
}
