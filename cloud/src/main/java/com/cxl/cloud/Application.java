package com.cxl.cloud;

<<<<<<< HEAD
import org.mybatis.spring.annotation.MapperScan;
=======
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
<<<<<<< HEAD
@MapperScan("com.cxl.cloud.dao")
=======
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
public class Application  {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(Application.class);
//    }
}
