package com.eek.kimpli.kakaoLogin.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.eek.kimpli.kakaoLogin.config.KakaoAuthentication;
import com.eek.kimpli.kakaoLogin.config.KakaoComponent;
import com.eek.kimpli.kakaoLogin.config.KakaoUserDetails;
import com.eek.kimpli.kakaoLogin.repository.KakaoLoginRepository;
//import com.eek.kimpli.member.repository.MemberRepository;
import com.eek.kimpli.user.model.User;
import com.eek.kimpli.user.service.ImageDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
@RequiredArgsConstructor
public class KakaoLoginServiceImpl implements KakaoLoginService {

    private final KakaoLoginRepository kakaoLoginRepository;
    private final ImageDownloader imageDownloader;
//    private final User user;
//        private final KakaoComponent kakaoComponent;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.response-type}")
    private String kakaoResponseType;

	//로그인 성공하면 authorize_code 받아옴
   public Map<String, String>  getAccessToken (String authorize_code) {
      String access_Token = "";
      String refresh_Token = "";
      String reqURL = "https://kauth.kakao.com/oauth/token";
      try {
    	  //java.net.URL 클래스
         URL url = new URL(reqURL);
         //주어진 URL에 대한 HttpURLConnection 객체를 생성하고 연결
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("POST");
         conn.setDoOutput(true);

         //POST 요청. 서버로 데이터를 전송 -> HttpURLConnection의 출력 스트림을 얻어와서-> 문자열로 조작
         //출력 스트림
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
         StringBuilder sb = new StringBuilder();
         sb.append("grant_type=authorization_code");
         sb.append("&client_id=").append(kakaoClientId);//본인이 발급받은 key
         sb.append("&redirect_uri=").append(kakaoRedirectUri);// 본인이 설정한 주소 -> 이쪽으로 간다


//              sb.append("&client_id=1e411be4c9538cd8fc4f1b4c817968b4"); //본인이 발급받은 key
//         sb.append("&redirect_uri=http://localhost:9090/kakaologin"); // 본인이 설정한 주소 -> 이쪽으로 간다

         sb.append("&code=" + authorize_code);
         bw.write(sb.toString());

         //버퍼의 내용을 출력 스트림으로 플러시하여 전송
         bw.flush();

         // 요청이 정상 도달-> 200
         int responseCode = conn.getResponseCode();
         System.out.println("responseCode : " + responseCode);
         // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         String line = "";
         String result = "";
         while ((line = br.readLine()) != null) {
            result += line;
         }
         System.out.println("response body'토큰.' : " + result);

       //JSON파싱 객체 생성
         // Gson 라이브러리의 JsonParser와 JsonElement 대신 Jackson 라이브러리의 ObjectMapper와 JsonNode를 사용
         ObjectMapper objectMapper = new ObjectMapper();
         JsonNode jsonNode = objectMapper.readTree(result);
         //access 토큰은 일정 기간 동안 유효
         access_Token = jsonNode.get("access_token").asText();
         //refresh 토큰은 access 토큰 만료후 자동으로 접근 토큰을 갱신
         refresh_Token = jsonNode.get("refresh_token").asText();
         System.out.println("access_token : " + access_Token);
         System.out.println("refresh_token : " + refresh_Token);

         //메모리 누수를 방지
         br.close();
         bw.close();
      } catch (IOException e) {
         e.printStackTrace();
      }

      Map<String, String> token = new HashMap<>();
      token.put("access_token", access_Token);
      token.put("refresh_token", refresh_Token);

      System.out.println("카카오서비스 맨 마지막 ");
      System.out.println("access_token : " + access_Token);
      System.out.println("refresh_token : " + refresh_Token);
      return token;
   }




   public User getUserInfo(String access_Token, String refresh_Token) {
       // 사용자 정보를 바탕으로 KakaoAuthentication 객체 생성

      HashMap<String, Object>userInfo = new HashMap<String, Object>();
      String reqURL = "https://kapi.kakao.com/v2/user/me";
      try {
         URL url = new URL(reqURL);
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         conn.setRequestProperty("Authorization", "Bearer " + access_Token);
         int responseCode = conn.getResponseCode();
         System.out.println("responseCode : " + responseCode);
         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         String line = "";
         String result = "";
         while ((line = br.readLine()) != null) {
            result += line;
         }
         // JSON 데이터를 파싱하고 필요한 정보를 추출. JSON데이터 조작하기 위해 jsonNode 객체화
         ObjectMapper objectMapper = new ObjectMapper();
         JsonNode jsonNode = objectMapper.readTree(result);
         JsonNode properties = jsonNode.get("properties");
         JsonNode kakao_account = jsonNode.get("kakao_account");
         String nickname = properties.get("nickname").asText();
         String email = kakao_account.get("email").asText();
         String thumbnailImage = properties.get("thumbnail_image").asText();
         String profileImage = properties.get("profile_image").asText();
//      String profileImage = properties.get("profile_image").asText().replace("http://", "");

         userInfo.put("nikname", nickname);
         System.out.println("@@@@@@닉네임@@@@@"+nickname);
         userInfo.put("email", email);

         userInfo.put("thumbnailImage", thumbnailImage);
         userInfo.put("profileImage", profileImage);
         userInfo.put("access_Token", access_Token);
         userInfo.put("refresh_Token", refresh_Token);
         userInfo.put("username", email.substring(0, email.indexOf('@')));
         System.out.println("여기1");
          String  fileName = imageDownloader.imgServerSave(profileImage);
        System.out.println("여기2"+fileName);
userInfo.put("stored_file_name", fileName);
      } catch (IOException e) {
         e.printStackTrace();
      }

      User result = kakaoLoginRepository.findKakao(userInfo);
String username = userInfo.get("username") != null ? userInfo.get("username").toString() : null;
KakaoAuthentication kakaoAuthentication = new KakaoAuthentication(new KakaoUserDetails(username));

        // SecurityContextHolder에 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(kakaoAuthentication);

      if(result==null) {
         //없으면 인서트
        kakaoLoginRepository.insertKakaoLogin(userInfo);
        return result;
   		}else {
         //다르면 업데이트
   			kakaoLoginRepository.updateKakaoLogin(userInfo);
   			return result;
   		}
      }
   }







