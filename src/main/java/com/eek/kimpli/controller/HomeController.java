package com.eek.kimpli.controller;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.board.repository.BoardRepository;
import com.eek.kimpli.board.validator.BoardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
//    @GetMapping("/")
//    public String index(){
//        return "index";
//    }

    //DI일어남. 여기에 인스턴스가 들어옴
    final BoardRepository boardRepository;


    final BoardValidator boardValidator;

    @GetMapping("/1")
    public String list(Model model, @PageableDefault(size = 3) Pageable pageable,
                       @RequestParam(required = false,defaultValue = "") String searchText) {
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText,searchText,pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endpage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endpage);
        model.addAttribute("boards", boards);
        return "index1";
    }
}
