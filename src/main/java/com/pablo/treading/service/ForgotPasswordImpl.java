package com.pablo.treading.service;

import com.pablo.treading.domain.VerificationType;
import com.pablo.treading.modal.ForgotPassswordToken;
import com.pablo.treading.modal.User;
import com.pablo.treading.repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordImpl implements ForgotPasswordService{
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Override
    public ForgotPassswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo) {
        ForgotPassswordToken token = new ForgotPassswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setVerificationType(verificationType);
        token.setOtp(otp);
        token.setId(id);
        return forgotPasswordRepository.save(token);
    }

    @Override
    public ForgotPassswordToken findById(String id) {
        Optional<ForgotPassswordToken> token = forgotPasswordRepository.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgotPassswordToken findByUser(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPassswordToken token) {
        forgotPasswordRepository.delete(token);
    }
}
