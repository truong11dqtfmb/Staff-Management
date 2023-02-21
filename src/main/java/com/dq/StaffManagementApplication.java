package com.dq;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootApplication
public class StaffManagementApplication {

//    @Bean
//    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename("classpath:messages/staff");
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    public static void main(String[] args) {
        SpringApplication.run(StaffManagementApplication.class, args);
    }

}
