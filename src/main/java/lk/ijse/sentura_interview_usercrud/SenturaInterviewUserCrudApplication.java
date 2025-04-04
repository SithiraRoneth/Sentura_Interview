package lk.ijse.sentura_interview_usercrud;

import okhttp3.OkHttpClient;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SenturaInterviewUserCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenturaInterviewUserCrudApplication.class, args);
    }
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
