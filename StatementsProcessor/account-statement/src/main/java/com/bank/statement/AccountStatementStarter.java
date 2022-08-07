package com.bank.statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AccountStatementStarter {
    private static ApplicationContext appContext;

    public static void main(String[] args) {
        SpringApplication app=new SpringApplication(AccountStatementStarter.class);
        appContext=app.run(args);
    }
}
