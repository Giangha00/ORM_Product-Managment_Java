package org.example.mvcdemo.service;

import org.example.mvcdemo.entity.User;
import org.example.mvcdemo.exception.BusinessException;
import org.example.mvcdemo.repository.UserRepository;
import org.example.mvcdemo.util.PasswordUtil;

public class LoginService {

    private final UserRepository userRepository = new UserRepository();

    public User login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new BusinessException("Sai ten dang nhap hoac mat khau!");
        }
        User user = userRepository.findByUsername(username.trim());
        if (user == null || !PasswordUtil.matches(password, user.getPassword())) {
            throw new BusinessException("Sai ten dang nhap hoac mat khau!");
        }
        return user;
    }
}
