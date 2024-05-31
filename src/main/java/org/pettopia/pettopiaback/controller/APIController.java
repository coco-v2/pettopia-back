package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.APIDTO;
import org.pettopia.pettopiaback.dto.ShotRecordsDTO;
import org.pettopia.pettopiaback.service.APIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "API 컨트롤러", description = "API 관련 컨트롤러")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class APIController {

    private final APIService apiService;

    @GetMapping("/map/hospital/{address}")
    public ResponseEntity  <List<APIDTO.MapListResponse>> getShotRecordsList(@PathVariable String address) throws Exception {

        List<APIDTO.MapListResponse> hospitalList = apiService.getHospitalMapList(address);
        return ResponseEntity.ok(hospitalList);
    }

    @GetMapping("/map/beauty/{address}")
    public ResponseEntity  <List<APIDTO.MapListResponse>> getBeautyShopList(@PathVariable String address) throws Exception {

        List<APIDTO.MapListResponse> beautyShopList = apiService.getBeautyShopMapList(address);
        return ResponseEntity.ok(beautyShopList);
    }

//    @GetMapping("/search/beauty/csv")
//    public List<String[]> searchBeautyShopList(@RequestParam String name) {
//        return apiService.findAddressesContainingName(name);
//    }


}

