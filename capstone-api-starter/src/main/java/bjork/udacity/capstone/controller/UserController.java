package bjork.udacity.capstone.controller;

import bjork.udacity.capstone.domain.MyEntity;
import bjork.udacity.capstone.domain.UserInfo;
import bjork.udacity.capstone.repository.TestRepository;
import bjork.udacity.capstone.repository.UserInfoRepository;
import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import com.auth0.spring.security.api.authentication.PreAuthenticatedAuthenticationJsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/secure/users")
public class UserController {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Value("${PATH}")
    private String myEnv;

    @PostConstruct
    public void init() {
        List<MyEntity> all = testRepository.findAll();

        if (all.size() == 0) {
            testRepository.save(new MyEntity(UUID.randomUUID().toString()));
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserInfo> get() {
        return userInfoRepository.findAll();
//        return System.getenv(myEnv);
    }
    
    @RequestMapping(value = "/save-user", method = RequestMethod.POST)
    public ResponseEntity saveUser(HttpServletRequest request) {

        PreAuthenticatedAuthenticationJsonWebToken principal =
                (PreAuthenticatedAuthenticationJsonWebToken) request.getUserPrincipal();

        /*AuthenticationJsonWebToken principal =
                (AuthenticationJsonWebToken) request.getUserPrincipal();*/

        String token = principal.getToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.put("Authorization", List.of("Bearer " + token));

        RequestEntity requestEntity = RequestEntity
                .get(URI.create("https://dev-jyv2wd64.eu.auth0.com/userinfo"))
                .headers(headers)
                .build();
//        RequestEntity requestEntity = new RequestEntity<>(
//                headers,
//                HttpMethod.GET, URI.create("https://dev-jyv2wd64.eu.auth0.com/userinfo")
//        );

        ResponseEntity<UserInfo> userInfo = restTemplate.exchange(requestEntity, UserInfo.class);
        userInfoRepository.save(Objects.requireNonNull(userInfo.getBody()));

        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public List<MyEntity> test(HttpServletRequest request) {
        return testRepository.findAll();
    }
}

