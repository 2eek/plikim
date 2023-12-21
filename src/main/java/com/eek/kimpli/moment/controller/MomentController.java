package com.eek.kimpli.moment.controller;


import com.eek.kimpli.moment.model.Moment;
import com.eek.kimpli.moment.service.MomentService;
import com.eek.kimpli.moment.validator.MomentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/moment")
@RequiredArgsConstructor //생성자 자동으로 만들어준다.
public class MomentController {


     final MomentService momentService;
        private final MomentValidator momentValidator;

//    private final List<Moment> data = generateSampleData();

//           @PostMapping("/save")
//    public String save(@Valid Moment moment, BindingResult bindingResult) {
//        System.out.println("Received data: " + board.toString());
//        String resultView =  boardService.saveOrUpdateBoard(board, bindingResult);
//        //케이스에 따라 뷰 구분
//        return resultView;
//    }

@PostMapping("/save")
    public ResponseEntity<String> save(@Valid Moment moment,
                                       BindingResult bindingResult,
                                       @RequestParam(value = "profileFiles", required = false)List<MultipartFile> profileFiles) {

        // 유효성 검사 수행
        momentValidator.validate(moment, bindingResult);

        if (bindingResult.hasErrors()) {
            // 에러가 있으면 에러 메시지를 JSON으로 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Validation errors\"}");
        }

        // 에러가 없으면 정상적으로 처리
        String resultView = momentService.saveOrUpdateMoment(moment, bindingResult);

        // 처리 결과를 JSON으로 반환
        return ResponseEntity.ok(resultView);
    }

    // 다른 API 엔드포인트 및 메소드들...




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
