package org.pettopia.pettopiaback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.JSONArray;
import org.pettopia.pettopiaback.dto.APIDTO;
import org.pettopia.pettopiaback.dto.ShotRecordsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class APIService {


    @Value("${API.key}")
    String apiKey;

    @Value("${API.beauty-key}")
    String beautyKey;

    @Value("${API.weather-key}")
    String weatherKey;

    @Value("${API.pet-key}")
    String petKey;

    public List<Map<String, Object>> getHospitalList(String country) throws Exception {
     int totalSize = getApiSize();

     List<Map<String, Object>> resultMap = new ArrayList<>();
     int startIndex = 1;
     int finalIndex =1;
     if(1000< totalSize){
      finalIndex=1000;
     }
     else{
      finalIndex = totalSize;
     }
     System.out.println(totalSize);
     System.out.println(startIndex);
     System.out.println(finalIndex);
     while (startIndex < totalSize) {
      StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
      urlBuilder.append("/" + URLEncoder.encode(apiKey, "UTF-8"));
      urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
      urlBuilder.append("/" + URLEncoder.encode("LOCALDATA_020301", "UTF-8"));
      urlBuilder.append("/" + URLEncoder.encode(String.valueOf(startIndex), "UTF-8"));
      urlBuilder.append("/" + URLEncoder.encode(String.valueOf(finalIndex), "UTF-8"));

      URL url = new URL(urlBuilder.toString());
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-type", "application/json");

      BufferedReader rd;
      if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
       rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      } else {
       rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
      }

      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = rd.readLine()) != null) {
       sb.append(line);
      }
      rd.close();
      conn.disconnect();

      // JSON 문자열을 Map으로 변환
      JSONObject jsonResponse = new JSONObject(sb.toString());
      JSONObject jsonData = jsonResponse.getJSONObject("LOCALDATA_020301"); // 서비스명
      System.out.println(jsonData.getInt("list_total_count"));

      JSONArray rows = jsonData.getJSONArray("row");

      // "row" 데이터에서 첫 번째 객체를 Map으로 변환
      for (int j = 0; j < rows.length(); j++) {
       JSONObject item = rows.getJSONObject(j);
       Map<String, Object> itemMap = new HashMap<>();
       for (String key : item.keySet()) { // 모든 키를 반복하여 Map에 매핑
        itemMap.put(key, item.get(key));
       }
       String tradeState = (String) itemMap.get("TRDSTATENM");
       String address = (String) itemMap.get("RDNWHLADDR");

       Map<String, Object> itemValue = new HashMap<>();
       if (address.contains(",")) {
        address = address.split(",")[0].trim();
       }
       if (address.contains("(")) {
        address = address.split("\\(")[0].trim();
       }
       Map<String, Object> ItemValue = new HashMap<>();
       itemValue.put("address", address);
       itemValue.put("name", (String) itemMap.get("BPLCNM"));
       itemValue.put("phoneNumber", itemMap.get("SITETEL"));


       if ("영업/정상".equals(tradeState) && address.contains(country)) {
        resultMap.add(itemValue);
       }
       startIndex = finalIndex+1;
       if(finalIndex + 1000 <totalSize){
        finalIndex = finalIndex+1000;
       }
       else{
        finalIndex = totalSize;
       }

      }



     }
     // Map 데이터 출력
     System.out.println("Mapped data: " + resultMap);
     return resultMap;
    }
    public int getApiSize() throws Exception {
        System.out.println("api 검색 시작");
        System.out.println("api key:" + apiKey);


        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
        urlBuilder.append("/" + URLEncoder.encode(apiKey, "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("LOCALDATA_020301", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("2", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        // JSON 문자열을 Map으로 변환
        JSONObject jsonResponse = new JSONObject(sb.toString());
        JSONObject jsonData = jsonResponse.getJSONObject("LOCALDATA_020301"); // 서비스명
        System.out.println("api 검색 끝");
        System.out.println(jsonData.getInt("list_total_count"));
        return jsonData.getInt("list_total_count");
    }

    public List<APIDTO.MapListResponse> getHospitalMapList(String address) throws Exception {
        List<Map<String, Object>> mapList = getHospitalList(address);
        List<APIDTO.MapListResponse> apiDTOS = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            APIDTO.MapListRequest request = APIDTO.MapListRequest.maps((String) map.get("name"), (String) map.get("address"), (String) map.get("phoneNumber"));
            APIDTO.MapListResponse response = getMapResponse(request);
            APIDTO.MapListResponse mapDTO =
                    APIDTO.MapListResponse.builder()
                            .name(response.getName())
                            .address(response.getAddress())
                            .phoneNumber(response.getPhoneNumber())
                            .build();
            apiDTOS.add(mapDTO);
        }
        return apiDTOS;
    }

    public APIDTO.MapListResponse getMapResponse(APIDTO.MapListRequest request) {
        APIDTO.MapListResponse response =
                APIDTO.MapListResponse.builder()
                        .name(request.getName())
                        .address(request.getAddress())
                        .phoneNumber(request.getPhoneNumber())
                        .build();
        return response;
    }

    public List<APIDTO.MapListResponse> getBeautyShopMapList(String address) throws Exception {
        List<Map<String, Object>> mapList = getBeautyShopList(address);
        List<APIDTO.MapListResponse> apiDTOS = new ArrayList<>();

        for (Map<String, Object> map : mapList) {
            APIDTO.MapListRequest request = APIDTO.MapListRequest.maps((String) map.get("name"), (String) map.get("address"), (String) map.get("phoneNumber"));
            APIDTO.MapListResponse response = getMapResponse(request);
            APIDTO.MapListResponse mapDTO =
                    APIDTO.MapListResponse.builder()
                            .name(response.getName())
                            .address(response.getAddress())
                            .phoneNumber(response.getPhoneNumber())
                            .build();
            apiDTOS.add(mapDTO);
        }
        return apiDTOS;
    }

    public Map<String,String> getWeather(String lat, String lon) throws Exception{
        String urlStr = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s", lat, lon, weatherKey);

        URL url = new URL(urlStr.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        JSONObject jsonObject = new JSONObject(sb.toString());

        // weather 배열에서 첫 번째 객체 가져오기
        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weather = weatherArray.getJSONObject(0);

        // main과 icon 값 가져오기
        String main = weather.getString("main");
        String icon = weather.getString("icon");

        // 결과 출력
        System.out.println("Main: " + main);
        System.out.println("Icon: " + icon);
        Map<String,String> map = new HashMap<>();
        map.put("main", main);
        map.put("icon",icon);
        return map;
    }

    public Map<String,Object> getPet (String dogRegNumber, String ownerNm )throws Exception{
        String key = "="+petKey;
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/animalInfoSrvc/animalInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + key); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("dog_reg_no","UTF-8") + "=" + URLEncoder.encode(dogRegNumber, "UTF-8")); /*동물등록번호 또는 RFID코드 필수*/
        urlBuilder.append("&" + URLEncoder.encode("owner_nm","UTF-8") + "=" + URLEncoder.encode(ownerNm, "UTF-8")); /*소유자 성명 또는 생년월일 필수*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값) 또는 json*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        JSONObject jsonResponse = new JSONObject(sb.toString());
        JSONObject response = jsonResponse.getJSONObject("response");

        Map<String, Object> map = new HashMap<>();

        if (response.has("body") && !response.isNull("body")) {
            JSONObject body = response.getJSONObject("body");

            if (body.has("item") && !body.isNull("item")) {
                JSONObject item = body.getJSONObject("item");

                // Extract the required fields and put them in the map
                map.put("dogRegNo", item.getString("dogRegNo"));
                map.put("rfidCd", item.getString("rfidCd"));
                map.put("dogNm", item.getString("dogNm"));
                map.put("sexNm", item.getString("sexNm")=="암컷"?false:true);
                map.put("kindNm", item.getString("kindNm"));
                map.put("neuterYn", item.getString("neuterYn")=="미중성"?false:true);
            }
        }
        return map;

    }


//    public List<Map<String, Object>> getBeautyShopList(String region) throws Exception {
//
//        int totalSize = getBeautyApiSize();
//
//        List<Map<String, Object>> resultMap = new ArrayList<>();
//        int pageSize = 500;
//
//        log.info("총 사이즈: {}", totalSize);
//
//        for (int startIndex = 1; startIndex <= totalSize; startIndex += pageSize) {
//            StringBuilder urlBuilder = new StringBuilder("http://www.localdata.go.kr/platform/rest/TO0/openDataApi");
//            urlBuilder.append("?authKey=" + URLEncoder.encode(beautyKey, "UTF-8"));
//            urlBuilder.append("&opnSvcId=" + URLEncoder.encode("02_03_11_P", "UTF-8"));
//            urlBuilder.append("&pageIndex=" + URLEncoder.encode(String.valueOf(startIndex), "UTF-8"));
//            urlBuilder.append("&pageSize=" + URLEncoder.encode(String.valueOf(pageSize), "UTF-8"));
//
//            URL url = new URL(urlBuilder.toString());
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/xml");
//
//            BufferedReader rd;
//            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//
//            StringBuilder sb = new StringBuilder();
//            String line;
//
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//
//            rd.close();
//            conn.disconnect();
//
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));
//
//            NodeList rows = doc.getElementsByTagName("row");
//            log.info("row:{}", rows);
//
//            for (int i = 0; i < rows.getLength(); i++) {
//                Map<String, Object> itemMap = new HashMap<>();
//                NodeList children = rows.item(i).getChildNodes();
//                for (int j = 0; j < children.getLength(); j++) {
//                    String nodeName = children.item(j).getNodeName();
//                    String nodeValue = children.item(j).getTextContent();
//                    itemMap.put(nodeName, nodeValue);
//                }
//                String tradeState = (String) itemMap.get("trdStateGbn");
//                log.info("tradeState: {}", tradeState);
//
//                String address = (String) itemMap.get("rdnWhlAddr");
//                log.info("address: {}", address);
//
//                // 주소 정리
//                if (address.contains(",")) {
//                    address = address.split(",")[0].trim();
//                }
//                if (address.contains("(")) {
//                    address = address.split("\\(")[0].trim();
//                }
//
//                if (tradeState.equals("01") && address.contains(region)) {
//                    // 검사 결과에 따라 resultMap에 추가
//                    itemMap.put("address", address);
//                    itemMap.put("name", itemMap.get("bplcNm"));
//                    itemMap.put("phoneNumber", itemMap.get("siteTel"));
//
//                    resultMap.add(itemMap);
//                }
//
//            }
//        }
//        return resultMap;
//    }

    public List<Map<String, Object>> getBeautyShopList(String region) throws Exception {
        int totalSize = getBeautyApiSize();

        List<Map<String, Object>> resultMap= new ArrayList<>();
        int startIndex = 1;
        int finalIndex =1;
        if(500 < totalSize){
            finalIndex = 500;
        }
        else{
            finalIndex = totalSize;
        }
        System.out.println("totalSize:" + totalSize);
        System.out.println("startIndex:" + startIndex);
        System.out.println("finalIndex" + finalIndex);

        while (startIndex < totalSize) {
            StringBuilder urlBuilder = new StringBuilder("http://www.localdata.go.kr/platform/rest/TO0/openDataApi");
            urlBuilder.append("?authKey=" + URLEncoder.encode(beautyKey, "UTF-8"));
            urlBuilder.append("&opnSvcId=" + URLEncoder.encode("02_03_11_P", "UTF-8"));
            urlBuilder.append("&pageIndex=" + URLEncoder.encode(String.valueOf(startIndex), "UTF-8"));
            urlBuilder.append("&pageSize=" + URLEncoder.encode(String.valueOf(finalIndex), "UTF-8"));

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            rd.close();
            conn.disconnect();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));

            NodeList rows = doc.getElementsByTagName("row");
            log.info("row:{}", rows);

            for (int i = 0; i < rows.getLength(); i++) {
                NodeList children = rows.item(i).getChildNodes();
                Map<String, Object> itemMap = new HashMap<>();
                for (int j = 0; j < children.getLength(); j++) {
                    String nodeName = children.item(j).getNodeName();
                    String nodeValue = children.item(j).getTextContent();
                    itemMap.put(nodeName, nodeValue);
                }
                String tradeState = (String) itemMap.get("trdStateGbn");
                log.info("tradeState: {}", tradeState);

                String address = (String) itemMap.get("rdnWhlAddr");
                log.info("address: {}", address);

                Map<String, Object> itemValue = new HashMap<>();

                // 주소 정리
                if (address.contains(",")) {
                    address = address.split(",")[0].trim();
                }
                if (address.contains("(")) {
                    address = address.split("\\(")[0].trim();
                }

                Map<String, Object> ItemValue = new HashMap<>();
                itemValue.put("address", address);
                itemValue.put("name", (String) itemMap.get("bplcNm"));
                itemValue.put("phoneNumber", itemMap.get("siteTel"));

                if ("01".equals(tradeState) && address.contains(region)) {
                    resultMap.add(itemValue);
                }
                startIndex = finalIndex + 1;
                if(finalIndex + 1000 < totalSize){
                    finalIndex = finalIndex + 1000;
                }
                else{
                    finalIndex = totalSize;
                }

            }

        }
        // Map 데이터 출력
        System.out.println("Mapped data: " + resultMap);
        return resultMap;
    }


    public int getBeautyApiSize() throws Exception {
        log.info("beauty api 검색 시작");

        StringBuilder urlBuilder = new StringBuilder("http://www.localdata.go.kr/platform/rest/TO0/openDataApi");
        urlBuilder.append("?authKey=" + URLEncoder.encode(beautyKey, "UTF-8"));
        urlBuilder.append("&opnSvcId=" + URLEncoder.encode("02_03_11_P", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));

        Element headerElement = (Element) doc.getElementsByTagName("header").item(0);
        Element pagingElement = (Element) headerElement.getElementsByTagName("paging").item(0);
        NodeList listTotalCountNode = pagingElement.getElementsByTagName("totalCount");
        log.info("listTotal1 : {}", listTotalCountNode.item(0).getTextContent());
        int totalSize = Integer.parseInt(listTotalCountNode.item(0).getTextContent());
        log.info("totalSize2 : {}", totalSize);
        return totalSize;
    }



}

