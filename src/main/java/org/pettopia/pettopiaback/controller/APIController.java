package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.APIDTO;
import org.pettopia.pettopiaback.service.APIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @Operation(summary = "날씨 api", description = """
    [강수형태]0:없음, 1:비, 2:비/눈, 3:눈, 4:소나기
    """)
    @GetMapping("/map/weather")
    public ResponseEntity<Map<String, String>> getWeather(@RequestParam("lat") String lat, @RequestParam("lon") String lon) throws Exception {
        Map<String,String> map = apiService.getWeather(lat, lon);
        return ResponseEntity.ok(map);
    }
    @Operation(summary = "반려동물등록증", description = """
    동물등록번호는 15자리
    생년월일 020415
    <br>
    """)
    @GetMapping("/map/pet")
    public ResponseEntity<Map<String,Object>> getPet(
            @RequestParam("dogRegNo") String dogRegNo,  @RequestParam("ownernm") String ownernm
    ) throws Exception
    {
        Map<String,Object> map = apiService.getPet(dogRegNo,  ownernm);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/search/beauty/csv/{address}")
    public ResponseEntity <List<APIDTO.MapListResponse>> searchBeautyShopList(@PathVariable String address) throws Exception {
        List<APIDTO.MapListResponse> beautyShopList = apiService.getBeautyShopMapListAll(address);
        return ResponseEntity.ok(beautyShopList);
    }


}

