package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CommonController {

    @Autowired
    private CommonBusinessService commonBusinessService;

    // Method to get the User details
    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<UserDetailsResponse> userdetail(@RequestHeader("authorization") final String authorization, @PathVariable("userId") final String userId) throws AuthorizationFailedException, UserNotFoundException {

       String accessToken=authorization.split("Bearer" )[1].trim();
       accessToken=accessToken.substring(1,accessToken.length()-1);
        commonBusinessService.validateUser(authorization);
        UserAuthEntity authEntity = commonBusinessService.getCurrentUserDeatils(userId, authorization);
        final UserEntity userEntity = commonBusinessService.getUser(userId);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().userName(userEntity.getUsername()).firstName(userEntity.getFirstname())
                .lastName(userEntity.getLastname()).emailAddress(userEntity.getEmail()).contactNumber(userEntity.getContactnumber()).aboutMe(userEntity.getAboutme())
                .country(userEntity.getCountry()).dob(userEntity.getDob());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
