package com.pablo.treading.service;

import com.pablo.treading.domain.VerificationType;
import com.pablo.treading.modal.ForgotPassswordToken;
import com.pablo.treading.modal.User;

public interface ForgotPasswordService {

    ForgotPassswordToken createToken(User user,
                                     String id,
                                     String otp,
                                     VerificationType verificationType,
                                     String sendTo);

    ForgotPassswordToken findById(String id);

    ForgotPassswordToken findByUser(Long userId);

    void deleteToken(ForgotPassswordToken token);
}
