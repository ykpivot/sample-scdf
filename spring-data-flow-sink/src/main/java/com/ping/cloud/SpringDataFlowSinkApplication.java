package com.ping.cloud;

import com.ping.cloud.models.Log;
import com.ping.cloud.repositories.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
@SpringBootApplication
public class SpringDataFlowSinkApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringDataFlowSinkApplication.class);
    @Autowired
    private LogRepository logRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataFlowSinkApplication.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void loggerSink(String date) {
        logger.info("Received: " + date);
        Log log = new Log();
        log.setMessage(date);
        logRepository.save(log);
    }
}
