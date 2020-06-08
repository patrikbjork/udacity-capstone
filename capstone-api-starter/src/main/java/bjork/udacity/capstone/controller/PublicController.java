package bjork.udacity.capstone.controller;

import bjork.udacity.capstone.domain.MyEntity;
import bjork.udacity.capstone.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private TestRepository testRepository;

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
    public String get() {
        return myEnv;
//        return System.getenv(myEnv);
    }
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public List<MyEntity> test() {
        return testRepository.findAll();
    }
}

