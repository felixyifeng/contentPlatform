package com.fyf.contentplatform.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author fyf
 * @Description Redis配置，value用带类型信息的JSON序列化，保证取出来能还原成原对象
 * @create 2026-11-24 17:37
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 1. 创建RedisTemplate对象，设置连接工厂（连接redis服务用的）
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 2. 创建Jackson核心转换器、时间模块
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // 3. 开启默认类型写入（自动加 @class）
        mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // 4. 把配置好的mapper传入序列化器
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(mapper);


        // 5. 设置4种序列化规则
        // key 序列化为字符串
        template.setKeySerializer(new StringRedisSerializer());
        // value 序列化为json
        template.setValueSerializer(serializer);
        // hashKey 序列化为字符串
        template.setHashKeySerializer(new StringRedisSerializer());
        // hashValue 序列化为json
        template.setHashValueSerializer(serializer);

        // 6. 返回配置好的RedisTemplate对象
        return template;
    }
}
