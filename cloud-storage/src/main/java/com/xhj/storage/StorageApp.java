package com.xhj.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class StorageApp {

    public static void main(String[] args) {
        SpringApplication.run(StorageApp.class, args);
    }

}
