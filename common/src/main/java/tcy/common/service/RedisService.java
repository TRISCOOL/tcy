package tcy.common.service;

import java.util.Map;

public interface RedisService {
    void setStr(String key, String val, final long timeout);

    Map<String,String> hgetAll(String key);

    void setStr(String key, String val);

    String getStr(String key);

    boolean exists(String key);

    void delStr(String key);

    /**
     *
     * @param key 自增的key
     * @param add 自增的步长
     * @return
     */
    Long incrby(String key,Integer add);

    void setExpire(String key,Long timeout);
}
