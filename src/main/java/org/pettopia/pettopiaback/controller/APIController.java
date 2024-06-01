package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.APIDTO;
import org.pettopia.pettopiaback.service.APIService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

//    @GetMapping("/map/beauty/{address}")
//    public ResponseEntity <List<APIDTO.MapListResponse>> getBeautyShopList(@PathVariable String address) throws Exception {
//
//        List<APIDTO.MapListResponse> beautyShopList = apiService.getBeautyShopMapList(address);
//        return ResponseEntity.ok(beautyShopList);
//    }
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

    //--
//    @GetMapping("/search/beauty/{address}")
//    public ResponseEntity<List<APIDTO.MapListResponse>> searchBeautyShopList(@PathVariable String address) {
//        List<APIDTO.MapListResponse> beautyShopList = new ArrayList<>();
//        try {
//            beautyShopList = apiService.getBeautyShopMapList(address);
//        } catch (Exception e) {
//            // Handle exception if needed
//            e.printStackTrace();
//        }
//
//        List<APIDTO.MapListResponse> beautyShopListFromCSV = new ArrayList<>();
//        try {
//            beautyShopListFromCSV = apiService.getBeautyShopMapListByCSV(address);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // csv의 결과가 존재하고 api 내용과 중복되지 않는 경우 않는 경우에 추가
//        for (APIDTO.MapListResponse response : beautyShopListFromCSV) {
//            if (!containsResponse(beautyShopList, response)) {
//                beautyShopList.add(response);
//            }
//        }
//
//        return ResponseEntity.ok(beautyShopList);
//    }
//
//    // api에 이미 존재하는지 확인
//    private boolean containsResponse(List<APIDTO.MapListResponse> list, APIDTO.MapListResponse response) {
//        for (APIDTO.MapListResponse item : list) {
//            // 여기에서는 name을 기준으로 중복을 확인합니다. 필요에 따라 다른 속성도 포함하여 중복을 확인할 수 있습니다.
//            if (item.getName().equals(response.getName())) {
//                return true;
//            }
//        }
//        return false;
//    }


}

