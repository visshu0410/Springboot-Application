package com.vishalgound.Springboot.Application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private ApplicationContext applicationContext; // ✅ Check if Spring Boot context loads correctly

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should be loaded");
    }

}
