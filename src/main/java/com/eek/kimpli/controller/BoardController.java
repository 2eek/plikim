package com.eek.kimpli.controller;

import com.eek.kimpli.dto.BoardDTO;
import com.eek.kimpli.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/list")
    public String findAll(Model model){
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList",boardDTOList);
        return "board/list";

    }
}
