package com.srn.api.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.srn.api.model.dto.SrnProfileDto;
import com.srn.api.model.entity.SrnEmail;
import com.srn.api.model.entity.SrnProfile;
import com.srn.api.repo.ISrnProfileRepo;
import com.srn.api.repo.ISrnUserEmailRepo;
import com.srn.api.service.ISrnUserService;
import com.srn.api.utils.FormatterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Transactional
@Service("srnUserService")
public class SrnUserServiceImpl implements ISrnUserService {

    private static final String GOOGLE_CLIENT_ID = "734320557909-q0k91popdikfe65fcao0ng313gfsg4ne.apps.googleusercontent.com";

    @Autowired
    ISrnProfileRepo srnProfileRepo;

    @Autowired
    ISrnUserEmailRepo srnUserEmailRepo;

    @Override
    public SrnProfileDto userLogin(String token, LoginType type) {
        switch (type) {
            case GOOGLE:
                return userGoogleLogin(token);
            case FACEBOOK:
                break;
        }
        return null;
    }

    @Override
    public void userLogout(String type) {

    }

    public SrnProfileDto userGoogleLogin(String token) {
        GoogleIdToken.Payload googlePayload = null;
        try {
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID)).build();
            GoogleIdToken googleIdToken = verifier.verify(token);
            googlePayload = googleIdToken.getPayload();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generateProfile(googlePayload);
    }

    private SrnProfileDto generateProfile(GoogleIdToken.Payload payload) {
        SrnProfileDto dto = null;
        if (payload != null) {
            SrnEmail userEmail = srnUserEmailRepo.findByEmail(payload.getEmail());
            if (userEmail == null) {
                userEmail = new SrnEmail();
                userEmail.setEmail(payload.getEmail());
                userEmail.setLoginType(LoginType.GOOGLE.toString());
                userEmail.setCreated(FormatterUtils.getCurrentTimestamp());
                userEmail.setLastUpdated(FormatterUtils.getCurrentTimestamp());
                userEmail = srnUserEmailRepo.save(userEmail);
            }
            long emailId = userEmail.getId();
            SrnProfile profile = srnProfileRepo.findProfileByUserId(emailId);
            if (profile == null) {
                profile = new SrnProfile();
                profile.setUserId(userEmail.getId());
                profile.setAlternateEmail(userEmail.getEmail());
                srnProfileRepo.save(profile);
            }
            dto = profile.toDto();
            dto.setFullName(payload.get("name").toString());
            dto.setUrl(payload.get("picture").toString());
            dto.setEmail(userEmail.getEmail());
            return dto;
        }
        return null;
    }
}