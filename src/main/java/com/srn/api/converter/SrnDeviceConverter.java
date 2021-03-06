/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srn.api.converter;

import com.srn.api.dto.SrnDeviceDTO;
import com.srn.api.model.SrnDevice;
import org.springframework.stereotype.Component;

/**
 *
 * @author long
 */

@Component
public final class SrnDeviceConverter {
    public SrnDevice DTOtoSrnDevice(SrnDeviceDTO sdDto) {
        SrnDevice sd = new SrnDevice();
        sd.setId(sdDto.getId());
        sd.setModel(sdDto.getModel());
        sd.setImei(sdDto.getImei());
        sd.setManufacture(sdDto.getManufacture());
        sd.setOsversion(sdDto.getOsversion());
        sd.setFcm_id(sdDto.getFcm_id());
        sd.setCreated(sdDto.getCreated());
        sd.setLast_updated(sdDto.getLast_updated());
        return sd;
    }

    public SrnDeviceDTO SrnDeviceToDTO(SrnDevice sd) {
        SrnDeviceDTO sdDto = new SrnDeviceDTO();
        sdDto.setId(sd.getId());
        sdDto.setModel(sd.getModel());
        sdDto.setImei(sd.getImei());
        sdDto.setManufacture(sd.getManufacture());
        sdDto.setOsversion(sd.getOsversion());
        sdDto.setFcm_id(sd.getFcm_id());
        sdDto.setCreated(sd.getCreated());
        sdDto.setLast_updated(sd.getLast_updated());
        return sdDto;
    }
}
