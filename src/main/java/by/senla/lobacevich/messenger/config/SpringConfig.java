package by.senla.lobacevich.messenger.config;

import by.senla.lobacevich.messenger.mapper.UserMapper;
import by.senla.lobacevich.messenger.mapper.UserMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }

}
