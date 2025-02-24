package com.pablo.treading.service;

import com.pablo.treading.domain.VerificationType;
import com.pablo.treading.modal.User;
import com.pablo.treading.modal.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);


    void deleteVerificationCodeById(VerificationCode verificationCode);

}
