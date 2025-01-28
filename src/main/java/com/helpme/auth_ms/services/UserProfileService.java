package com.helpme.auth_ms.services;

import com.helpme.auth_ms.model.UserProfile;
import com.helpme.auth_ms.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    public void updateUserProfile() {

    }
}
