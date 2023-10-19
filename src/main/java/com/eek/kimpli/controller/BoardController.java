package com.eek.kimpli.controller;

import com.eek.kimpli.dto.BoardDTO;
import com.eek.kimpli.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private  final BoardService boardService;
    //생성자 주입방식으로 의존성주입받게됨
    @GetMapping("/save")
    public String saveForm(){return "board/save";}

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO){
        System.out.println("boardDTO ="+ boardDTO);
        boardService.save(boardDTO);
        return "index";
    }
}
