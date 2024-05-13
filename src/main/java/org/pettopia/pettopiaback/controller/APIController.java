package org.pettopia.pettopiaback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.APIDTO;
import org.pettopia.pettopiaback.dto.ShotRecordsDTO;
import org.pettopia.pettopiaback.service.APIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class APIController {
    private final APIService apiService;

    @GetMapping("/map/hospital/")
    public ResponseEntity  <List<APIDTO.MapListResponse>> getShotRecordsList() throws Exception {

        List<APIDTO.MapListResponse> hospitalList = apiService.getHospitalMapList();
        return ResponseEntity.ok(hospitalList);
    }

}

