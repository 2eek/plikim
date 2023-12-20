package com.eek.kimpli.moment.controller;


import com.eek.kimpli.moment.dto.Moment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/moment")
public class MomentController {

    private final List<Moment> data = generateSampleData();

    @GetMapping("/list")
    public String list() {
        return "/moment/list"; // HTML View를 반환하는 메소드
    }

    @ResponseBody
    @GetMapping("/data")
    public List<Moment> getData() {
        return data; // JSON 데이터를 반환하는 메소드
    }
  @ResponseBody
private List<Moment> generateSampleData() {
    List<Moment> sampleData = new ArrayList<>();

    for (int i = 1; i <= 10; i++) {
        sampleData.add(new Moment(i, "Moment " + i));
    }

    return sampleData;
}
}
