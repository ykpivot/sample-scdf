package com.ping.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@EnableBinding(Processor.class)
@SpringBootApplication
public class SpringDataFlowProcessorApplication {
    private static final Logger logger = LoggerFactory.getLogger(SpringDataFlowProcessorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringDataFlowProcessorApplication.class, args);
    }

    @Transformer(
            inputChannel = Processor.INPUT,
            outputChannel = Processor.OUTPUT
    )
    public Object transform(Long timestamp) {
        logger.info("In PROCESSOR");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String date = dateFormat.format(timestamp);
        return date;
    }
}
