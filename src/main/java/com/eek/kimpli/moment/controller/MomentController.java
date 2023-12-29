package com.eek.kimpli.moment.controller;


import com.eek.kimpli.moment.model.Moment;
import com.eek.kimpli.moment.service.MomentService;
import com.eek.kimpli.moment.validator.MomentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/moment")
@RequiredArgsConstructor //생성자 자동으로 만들어준다.
public class MomentController {


    final MomentService momentService;
    private final MomentValidator momentValidator;

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid Moment moment,
                                  BindingResult bindingResult,
                                  @RequestParam(value = "momentImgs", required = false) List<MultipartFile> momentImgs) {
        // 유효성 검사 수행
        momentValidator.validate(moment, bindingResult);

        if (bindingResult.hasErrors()) {
            // 에러가 있으면 에러 메시지를 추출하여 JSON으로 반환
            Map<String, String> validationErrors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                validationErrors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
        }

        // 에러가 없으면 정상적으로 처리
        Moment savedMoment = momentService.saveOrUpdateMoment(moment);
        momentService.saveMoment(moment, momentImgs);

        // 처리 결과를 JSON으로 반환
        return ResponseEntity.ok(savedMoment);
    }

    @GetMapping("/result")
    public List<Moment> list(Model model) {
        List<Moment> moments = momentService.findAll();
        model.addAttribute("moments", moments);
        return moments;
    }


}
