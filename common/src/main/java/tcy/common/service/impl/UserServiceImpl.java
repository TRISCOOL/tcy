package tcy.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcy.common.mapper.UserMapper;
import tcy.common.model.User;
import tcy.common.service.RedisService;
import tcy.common.service.UserService;
import tcy.common.utils.Utils;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public User loginAndRegister(User user) {

        User existUser = userMapper.selectUserByOpenId(user.getOpenId());

        String token = Utils.getUuid(true);

        boolean result = false;
        String cacheUser = null;

        if (existUser == null){
            user.setToken(token);
            result = insertUser(user);
            existUser = user;
            cacheUser = Utils.toJson(user);
        }else {
            redisService.delStr(existUser.getToken());

            existUser.setWxName(user.getWxName());
            existUser.setToken(token);
            result = updateUser(existUser);
            cacheUser = Utils.toJson(existUser);
        }

        if (result){
            redisService.setStr(token,cacheUser);
        }

        return existUser;
    }

    @Override
    public boolean insertUser(User user) {

        String uuId = Utils.getUuid(true);
        user.setUuid(uuId);
        user.setRegisterTime(new Date());

        int result = userMapper.insertSelective(user);
        if (result != 0){
            return true;
        }

        return false;
    }

    @Override
    public boolean updateUser(User user) {

        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result != 0){
            return true;
        }

        return false;
    }
}
