package com.example.restaurants.Controller;

import com.example.restaurants.Dao.ReviewDao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value = "/app")
public class ReviewController {

    private final ReviewDao reviewDao;

    public ReviewController(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @CrossOrigin
    @RequestMapping("/insertComment")
    public JsonNode insertComment(@RequestBody String req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(req);
        System.out.println("-----------------------------" + json);
        return reviewDao.insertComment(json);
    }

}
