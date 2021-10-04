package com.example.digitalwallet.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.lettuce.core.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import java.lang.reflect.Method;

@EnableCaching
@Configuration
public class RedisConfig {
    @Value("${spring.redis.pool.max-active}")
    private int redisPoolMaxActive;

    @Value("${spring.redis.pool.max-wait}")
    private int redisPoolMaxWait;

    @Value("${spring.redis.pool.max-idle}")
    private int redisPoolMaxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int redisPoolMinIdle;

    /**
     * Configuration Key Generation
     *
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(o.getClass().getName())
                        .append(method.getName());
                for (Object object : objects) {
                    stringBuilder.append(object.toString());
                }
                return stringBuilder.toString();
            }
        };
    }

    /**
     * Create redis connection factory
     *
     * @param dbIndex
     * @param host
     * @param port
     * @param password
     * @param timeout
     * @return
     */
//    public RedisConnectionFactory createJedisConnectionFactory(int dbIndex, String host, int port, String password, int timeout) {
//
//    }

}
