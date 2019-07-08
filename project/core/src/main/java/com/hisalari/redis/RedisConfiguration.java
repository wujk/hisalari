package com.hisalari.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collection;
import java.util.HashSet;

/**
 * redis操作工具包
 * @author CI11951
 *
 */
@Configuration
public class RedisConfiguration {
	
	// 普通操作类
	private RedisTemplate<String, Object> redisTemplate;
	private Jedis jedis;
	private JedisConnectionFactory jedisConnectionFactory;
	private RedisStandaloneConfiguration redisStandaloneConfiguration;
	
	private JedisClientConfiguration jedisClientConfiguration;   // 构建线程池
	private JedisPoolConfig jedisPoolConfig;   // 线程池信息
	
	private int maxIdle = 10;
	private long maxWaitMillis = 10000000;
	private boolean testOnBorrow = true;
	private String hostName = "192.168.140.159";
	private int port = 6379;
	private int index = 0;
	private int maxTotal = 5;
	private String password = "pass";

	@Bean("redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(@Autowired JedisConnectionFactory jedisConnectionFactory) {
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        StringRedisSerializer rs=new StringRedisSerializer();
        redisTemplate.setKeySerializer(rs);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(rs);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean("jedis")
	public Jedis jedis(@Autowired JedisConnectionFactory jedisConnectionFactory) {
		return jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection();
	}

	@Bean("jedisConnectionFactory")
	public JedisConnectionFactory jedisConnectionFactory(@Autowired RedisStandaloneConfiguration redisStandaloneConfiguration, @Autowired JedisClientConfiguration jedisClientConfiguration) {
		return jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
	}

	@Bean("redisStandaloneConfiguration")
	public RedisStandaloneConfiguration redisStandaloneConfiguration() {
		redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName, port);
		redisStandaloneConfiguration.setDatabase(index);
		redisStandaloneConfiguration.setPassword(password);
		return redisStandaloneConfiguration;
	}

	@Bean("jedisClientConfiguration")
	public JedisClientConfiguration jedisClientConfiguration(@Autowired JedisPoolConfig jedisPoolConfig) {
		return jedisClientConfiguration = ((JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder()).poolConfig(jedisPoolConfig).build();
	}

	@Bean("jedisPoolConfig")
	public JedisPoolConfig jedisPoolConfig() {
		jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		jedisPoolConfig.setMaxTotal(maxTotal);
		return jedisPoolConfig;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

}
