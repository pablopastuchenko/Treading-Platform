package com.pablo.treading.service;

import com.pablo.treading.domain.VerificationType;
import com.pablo.treading.modal.User;

public interface UserService {

    public User findUserByEmail(String email) throws Exception;
    public User findUserByJwt(String jwt) throws Exception;
    public User findUserById(Long userId) throws Exception;

    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user);

    User updatePassword(User user, String newPassword);

    User findUserProfileByJwt(String jwt);
}
