package com.pablo.treading.repository;

import com.pablo.treading.modal.ForgotPassswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassswordToken, String> {
    ForgotPassswordToken findByUserId(Long userId);
}
