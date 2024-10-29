package by.senla.lobacevich.messenger.config;

import by.senla.lobacevich.messenger.mapper.ChatMapper;
import by.senla.lobacevich.messenger.mapper.ChatMapperImpl;
import by.senla.lobacevich.messenger.mapper.CommentMapper;
import by.senla.lobacevich.messenger.mapper.CommentMapperImpl;
import by.senla.lobacevich.messenger.mapper.GroupMapper;
import by.senla.lobacevich.messenger.mapper.GroupMapperImpl;
import by.senla.lobacevich.messenger.mapper.MessageMapper;
import by.senla.lobacevich.messenger.mapper.MessageMapperImpl;
import by.senla.lobacevich.messenger.mapper.PostMapper;
import by.senla.lobacevich.messenger.mapper.PostMapperImpl;
import by.senla.lobacevich.messenger.mapper.ProfileMapper;
import by.senla.lobacevich.messenger.mapper.ProfileMapperImpl;
import by.senla.lobacevich.messenger.mapper.RequestFriendshipMapper;
import by.senla.lobacevich.messenger.mapper.RequestFriendshipMapperImpl;
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

    @Bean
    public ProfileMapper profileMapper() {
        return new ProfileMapperImpl();
    }

    @Bean
    public MessageMapper messageMapper() {
        return new MessageMapperImpl();
    }

    @Bean
    public ChatMapper chatMapper() {
        return new ChatMapperImpl();
    }

    @Bean
    public GroupMapper groupMapper() {
        return new GroupMapperImpl();
    }

    @Bean
    public PostMapper postMapper() {
        return new PostMapperImpl();
    }

    @Bean
    public CommentMapper commentMapper() {
        return new CommentMapperImpl();
    }

    @Bean
    public RequestFriendshipMapper requestFriendshipMapper() {
        return new RequestFriendshipMapperImpl();
    }
}
