package com.huce.edu_v2.service;

import com.huce.edu_v2.dto.request.AuthenticationRequest;
import com.huce.edu_v2.dto.response.AuthenticationResponse;
import com.huce.edu_v2.entity.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    /**
     * @param request -AuthenticationRequest Object
     * @return User Details based on a given email and password
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * @param refreshToken - refreshToken get from cookie
     */
    void logout(String refreshToken) throws ParseException, JOSEException;

    /**
     * @param refreshToken - refreshToken get from cookie
     * @return User Details based on a given refreshToken
     */
    AuthenticationResponse refreshToken(String refreshToken) throws ParseException, JOSEException;

    /**
     * @param user - User Object
     * @return Convert User Object to InfoAuthenticationDTO Object
     */
    AuthenticationResponse createAuthenticationResponse(User user);

    AuthenticationResponse outboundAuthenticate(String code);

}
