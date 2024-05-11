//package org.pettopia.pettopiaback.service;
//
//
//
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.json.JSONObject;
//import org.json.JSONArray;
//import org.pettopia.pettopiaback.dto.APIDTO;
//import org.pettopia.pettopiaback.dto.ShotRecordsDTO;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.List;
//
//@Slf4j
//@RequiredArgsConstructor
//@Transactional
//@Service
//public class APIService {
//
// @Value("${API.key}")
// String apiKey;
//
//    public List<Map<String, Object>> getHospitalList() throws Exception{
//
//     List<Map<String, Object >> resultMap = new ArrayList<>();
//     for(int i=0; i<3;i++){
//      String finalIndex ="";
//      String startIndex ="";
//      if(i==0){
//       finalIndex="1000";
//       startIndex ="1";
//      }
//      else if(i==1){
//       startIndex ="1001";
//       finalIndex="2000";
//
//      }
//      else{
//       startIndex="2001";
//       finalIndex="2120";
//      }
//      StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
//      urlBuilder.append("/" + URLEncoder.encode(apiKey, "UTF-8"));
//      urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
//      urlBuilder.append("/" + URLEncoder.encode("LOCALDATA_020301", "UTF-8"));
//      urlBuilder.append("/" + URLEncoder.encode(startIndex, "UTF-8"));
//      urlBuilder.append("/" + URLEncoder.encode(finalIndex, "UTF-8"));
//
//      URL url = new URL(urlBuilder.toString());
//      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//      conn.setRequestMethod("GET");
//      conn.setRequestProperty("Content-type", "application/json");
//
//      BufferedReader rd;
//      if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//       rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//      } else {
//       rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//      }
//
//      StringBuilder sb = new StringBuilder();
//      String line;
//      while ((line = rd.readLine()) != null) {
//       sb.append(line);
//      }
//      rd.close();
//      conn.disconnect();
//
//      // JSON 문자열을 Map으로 변환
//      JSONObject jsonResponse = new JSONObject(sb.toString());
//      JSONObject jsonData = jsonResponse.getJSONObject("LOCALDATA_020301"); // 서비스명
//      JSONArray rows = jsonData.getJSONArray("row");
//
//      // "row" 데이터에서 첫 번째 객체를 Map으로 변환
//      for(int j=0; j<rows.length();j++) {
//       JSONObject item = rows.getJSONObject(j);
//       Map<String, Object> itemMap = new HashMap<>();
//       for (String key : item.keySet()) { // 모든 키를 반복하여 Map에 매핑
//        itemMap.put(key, item.get(key));
//       }
//       String tradeState = (String) itemMap.get("TRDSTATENM");
//       String address = (String) itemMap.get("RDNWHLADDR");
//
//       if("영업/정상".equals(tradeState) && address.contains("영등포")){
//        resultMap.add(itemMap);
//       }
//
//      }
//     }
//
//     // Map 데이터 출력
//     System.out.println("Mapped data: " + resultMap);
//     return resultMap;
// }
//
// public List<APIDTO.MapListResponse> getHospitalMapList ()throws Exception{
//  List<Map<String,Object>>mapList=getHospitalList();
//     List<APIDTO.MapListResponse> apiDTOS = new ArrayList<>();
//     for(Map<String,Object> map : mapList){
//      APIDTO.MapListRequest request = APIDTO.MapListRequest.maps((String)map.get("BPLCNM"),(String)map.get("RDNWHLADDR"),(String)map.get("SITETEL"));
//      APIDTO.MapListResponse response = getMapResponse(request);
//      APIDTO.MapListResponse mapDTO =
//              APIDTO.MapListResponse.builder()
//                      .name(response.getName())
//                      .address(response.getAddress())
//                      .phoneNumber(response.getPhoneNumber())
//                      .build();
//      apiDTOS.add(mapDTO);
//     }
//     return apiDTOS;
// }
// public APIDTO.MapListResponse getMapResponse(APIDTO.MapListRequest request){
//     APIDTO.MapListResponse response =
//             APIDTO.MapListResponse.builder()
//                     .name(request.getName())
//                     .address(request.getAddress())
//                     .phoneNumber(request.getPhoneNumber())
//                     .build();
//     return response;
// }
//}
