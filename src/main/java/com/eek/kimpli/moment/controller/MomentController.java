package com.eek.kimpli.moment.controller;


import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.moment.dto.Moment;
import com.eek.kimpli.moment.service.MomentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/moment")
@RequiredArgsConstructor //생성자 자동으로 만들어준다.
public class MomentController {

    @Autowired
     final MomentService momentService;

//    private final List<Moment> data = generateSampleData();

//           @PostMapping("/save")
//    public String save(@Valid Moment moment, BindingResult bindingResult) {
//        System.out.println("Received data: " + board.toString());
//        String resultView =  boardService.saveOrUpdateBoard(board, bindingResult);
//        //케이스에 따라 뷰 구분
//        return resultView;
//    }

  public String save(@Valid Moment moment, BindingResult bindingResult) {
        System.out.println("Received data: " + moment.toString());
        String resultView =  momentService.saveOrUpdateMoment(moment, bindingResult);
        //케이스에 따라 뷰 구분
        return resultView;
    }




//게시글이 있으면 불러와야됨
//    @GetMapping("/data")
//    public List<Moment> getData() {
//        return data; // JSON 데이터를 반환하는 메소드
//    }

//private List<Moment> generateSampleData() {
//    List<Moment> sampleData = new ArrayList<>();
//
//    for (int i = 1; i <= 10; i++) {
//        sampleData.add(new Moment(i, "Moment " + i));
//    }
//
//    return sampleData;
//}
//
//         @PostMapping("/save")
//    public String save(@Valid Moment moment, BindingResult bindingResult) {
//        System.out.println("Received data: " + moment.toString());
//        String resultView =  momentService.saveOrUpdateBoard(moment, bindingResult);
//        //케이스에 따라 뷰 구분
//        return resultView;
//    }


}
