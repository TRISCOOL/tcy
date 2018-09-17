package tcy.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tcy.common.exception.ResponseCode;
import tcy.common.exception.TcyException;
import tcy.common.mapper.AdminUserMapper;
import tcy.common.mapper.UserMapper;
import tcy.common.model.AdminUser;
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

    @Autowired
    private AdminUserMapper adminUserMapper;

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

    @Override
    @Transactional
    public AdminUser loginAdmin(AdminUser adminUser) {
        if (adminUser == null)
            throw new TcyException(ResponseCode.PARAM_ILLEGAL);
        if (adminUser.getAccount() == null || adminUser.getPassword() == null)
            throw new TcyException(ResponseCode.PARAM_ILLEGAL);

        AdminUser existUser = adminUserMapper.selectByAccount(adminUser.getAccount());
        if (existUser == null)
            throw new TcyException(ResponseCode.NOT_FOUND_USER);

        if (!existUser.getPassword().equals(adminUser.getPassword()))
            throw  new TcyException(ResponseCode.PASSWORD_ERROR);

        //删除原来的token
        if (existUser != null && existUser.getToken() != null){
            redisService.delStr(existUser.getToken());
        }

        String token = Utils.getUuid(true);
        existUser.setToken(token);
        int result = adminUserMapper.updateByPrimaryKeySelective(existUser);
        if (result != 0){
            adminUser.setToken(token);
            adminUser.setId(existUser.getId());
            adminUser.setAvatar(existUser.getAvatar());
            adminUser.setName(existUser.getName());
            adminUser.setPassword(null);
            adminUser.setShopId(existUser.getShopId());
            adminUser.setShopName(existUser.getShopName());
            redisService.setStr(token,Utils.toJson(adminUser));
            return adminUser;
        }

        return null;
    }
}
