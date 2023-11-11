package com.eek.kimpli.board.controller;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.board.repository.BoardRepository;
import com.eek.kimpli.board.validator.BoardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    //DI일어남. 여기에 인스턴스가 들어옴
    final BoardRepository boardRepository;
    final BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 3)  Pageable pageable,
                       @RequestParam(required = false,defaultValue = "") String searchText) {
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText,searchText,pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endpage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endpage);
        model.addAttribute("boards", boards);
        return "board/list";
    }



    //생성자 주입방식으로 의존성주입받게됨
   @GetMapping("/saveForm")//입력폼을 보여줌
    public String saveForm(Model model, @RequestParam(required = false) Long id) {
    if(id ==null) {
        model.addAttribute("board", new Board());
         System.out.println("아이디 값 없을 때:"+ model);
     } else {
    Board board = boardRepository.findById(id).orElse(null);
    model.addAttribute("board", board);
        System.out.println("아이디 값 있을 때:"+board);
    }

    return "board/save";
}

    //DB에 저장
    @PostMapping("/save")
    public String save(@Valid Board board, BindingResult bindingResult) {
        boardValidator.validate(board,bindingResult);
        if(bindingResult.hasErrors()){
            return "board/save";
        }
        boardRepository.save(board);
            return "redirect:/board/list";
}

//    @PostMapping("/save")
//    public String save(@ModelAttribute BoardDTO boardDTO){
//        System.out.println("boardDTO ="+ boardDTO);
//        boardService.save(boardDTO);
//        return "redirect:/";
//    }

    //HTTP GET 요청을 처리하며, "/list" 경로로 들어오는 요청을 이 핸들러 메소드로 매핑함
//    @GetMapping("/list")
//    public String findAll(Model model){
//        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다
//        List<BoardDTO> boardDTOList = boardService.findAll();
            //모델에 담긴 데이터들을 타임리프에서 사용할 수 있다.
//        model.addAttribute("boardList",boardDTOList);
//        return "board/list";
//    }
}
