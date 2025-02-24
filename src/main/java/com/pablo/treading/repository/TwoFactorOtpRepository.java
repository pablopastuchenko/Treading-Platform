package com.pablo.treading.repository;

import com.pablo.treading.modal.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String > {
    TwoFactorOTP findByUserId(Long userId);
}
