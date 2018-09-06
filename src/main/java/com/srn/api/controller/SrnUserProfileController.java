/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srn.api.controller;

import com.srn.api.converter.SrnUserProfileConverter;
import com.srn.api.dto.SrnUserProfileDTO;
import com.srn.api.model.SrnUserProfile;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.srn.api.service.SrnUserProfileService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author long
 */

@RestController
@RequestMapping("/apiuserprofile")
public class SrnUserProfileController {
    private final Logger LOGGER = LoggerFactory.getLogger(SrnUserProfileController.class);
    private final SrnUserProfileService userProfileService;
    private final SrnUserProfileConverter userProfileConverter;

    @Autowired
    public SrnUserProfileController(SrnUserProfileService userProfileService, SrnUserProfileConverter userProfileConverter) {
        this.userProfileService = userProfileService;
        this.userProfileConverter = userProfileConverter;
    }

    @RequestMapping
    public ResponseEntity<List<SrnUserProfile>> loadAll() {
        LOGGER.info("start loadAll profile users");
        try {
            List<SrnUserProfile> LUPS = userProfileService.findAll();
            LOGGER.info("Found {} users", LUPS.size());
            return new ResponseEntity<>(LUPS, HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping("/{id}")
    public ResponseEntity<SrnUserProfileDTO> loadOne(@PathVariable int id) {
        LOGGER.info("start loadOne user by id: ", id);
        try {
            SrnUserProfile user = userProfileService.find(id);
            LOGGER.info("Found: {}", user);
            return new ResponseEntity<>(userProfileConverter.SrnUserProfileToDTO(user), HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SrnUserProfileDTO> create(@RequestBody SrnUserProfileDTO SUPDTO) {
        LOGGER.info("start creating user: ", SUPDTO);
        try {
            SrnUserProfile user = userProfileService.create(userProfileConverter.DTOtoSrnUserProfile(SUPDTO));
            return new ResponseEntity<>(userProfileConverter.SrnUserProfileToDTO(user), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
