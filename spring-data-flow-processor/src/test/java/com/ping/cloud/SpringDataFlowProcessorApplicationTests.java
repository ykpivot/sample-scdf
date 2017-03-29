package com.ping.cloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataFlowProcessorApplicationTests {

    @Autowired
    private SpringDataFlowProcessorApplication application;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testTimestampConversion() throws Exception {
        LocalDateTime time = LocalDateTime.of(2017, Month.JANUARY, 1, 12, 30, 0, 0);
        Object formatted = application.transform(time.toInstant(ZoneOffset.of("-05:00")).toEpochMilli());

        assertEquals("2017/01/01 12:30:00", formatted);
    }
}
