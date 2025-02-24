package com.pablo.treading.controller;


import com.pablo.treading.request.ForgotPasswordTokenRequest;
import com.pablo.treading.domain.VerificationType;
import com.pablo.treading.modal.ForgotPassswordToken;
import com.pablo.treading.modal.User;
import com.pablo.treading.modal.VerificationCode;
import com.pablo.treading.request.ResetPasswordRequest;
import com.pablo.treading.response.ApiResponse;
import com.pablo.treading.response.AuthResponse;
import com.pablo.treading.service.EmailService;
import com.pablo.treading.service.ForgotPasswordService;
import com.pablo.treading.service.UserService;
import com.pablo.treading.service.VerificationCodeService;
import com.pablo.treading.utils.OptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private EmailService emailService;
    private String jwt;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationType(@RequestHeader("Authorization") String jwt,
                                                     @PathVariable VerificationType verificationType) throws Exception{

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        if (verificationCode!= null) {
            verificationCode=verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if (verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerificationsOtpEmail(user.getEmail(), verificationCode.getOtp());
        }



        return new ResponseEntity<String>("verificação otp enviada com sucesso", HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @PathVariable String otp,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)?
                verificationCode.getEmail():verificationCode.getMobile();


        boolean isVerified= verificationCode.getOtp().equals(otp);

        if (isVerified){
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);

            verificationCodeService.deleteVerificationCodeById(verificationCode);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }

       throw new Exception("wrong otp");
    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
                                                        @RequestBody ForgotPasswordTokenRequest req) throws Exception{

        User user = userService.findUserByEmail(req.getSendTo());
        String otp = OptUtils.generateOTP();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPassswordToken token = forgotPasswordService.findByUser(user.getId());

        if (token == null) {
            token = forgotPasswordService.createToken(user, id,otp, req.getVerificationType(), req.getSendTo());
        }
        if(req.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationsOtpEmail(user.getEmail(), token.getOtp());
        }
        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("senha reseta com sucesso");


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPasswordOtp(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {


        ForgotPassswordToken forgotPassswordToken = forgotPasswordService.findById(id);

        boolean isVerified = forgotPassswordToken.getOtp().equals(req.getOtp());

        if (isVerified){
            userService.updatePassword(forgotPassswordToken.getUser(), req.getPassword());
            ApiResponse res = new ApiResponse();
            res.setMessage("senha atualizada com sucesso");
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        }
        throw new Exception();
    }
}
