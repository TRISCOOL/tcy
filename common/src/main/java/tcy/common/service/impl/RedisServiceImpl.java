package tcy.common.service.impl;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import tcy.common.service.RedisService;

import javax.annotation.Resource;
import java.util.Map;


@Service
public class RedisServiceImpl implements RedisService {
    @Resource
    private JedisPool pool;

    public void setStr(String key, String val, final long timeout) {
        try(Jedis jedis = pool.getResource()) {

            jedis.set(key, val);
            if (timeout == 0l) {
                setStr(key, val);
            } else {
                jedis.expire(key, (int)timeout);
            }

        }

    }

    public Map<String,String> hgetAll(String key){
        try(Jedis jedis = pool.getResource()){
            return jedis.hgetAll(key);
        }
    }


    public void setStr(String key, String val) {
        try(Jedis jedis = pool.getResource()) {
            jedis.set(key, val);
        }
    }

    public String getStr(String key) {
        try(Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }

    public boolean exists(String key) {
        try(Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }

    @Override
    public void delStr(String key) {
        try (Jedis jedis = pool.getResource()){
            jedis.del(key);
        }
    }
}
