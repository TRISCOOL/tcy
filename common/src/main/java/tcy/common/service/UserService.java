package tcy.common.service;

import tcy.common.model.User;

public interface UserService {

    User loginAndRegister(User user);

    boolean insertUser(User user);

    boolean updateUser(User user);
}
