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
@RequestMapping("/member")//공통주소
public class MemberController {
    private  final BoardService boardService;
    //생성자 주입방식으로 의존성주입받게됨
    //단순한 호출 GetMapping을 이용한다. URL에 매핑된 핸들러 필요하다.
    @GetMapping("/memberlist")
    public String saveForm(){
        return "member/memberlist";}

    @GetMapping("/memberjoin")
    public String joinForm(){
        return "member/memberjoinform";
    }


//    @PostMapping("/save")
//    public String save(@ModelAttribute BoardDTO boardDTO){
//        System.out.println("boardDTO ="+ boardDTO);
//        boardService.save(boardDTO);
//        return "index";
//    }
    @GetMapping("/list")
    public String findAll(Model model){
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList",boardDTOList);
        return "board/list";
    }
}
