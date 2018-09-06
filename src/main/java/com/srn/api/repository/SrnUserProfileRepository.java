/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srn.api.repository;

import com.srn.api.model.SrnUserProfile;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author long
 */

public interface SrnUserProfileRepository extends JpaRepository<SrnUserProfile, Integer>{
    SrnUserProfile findByNickname(String nickname);
}
