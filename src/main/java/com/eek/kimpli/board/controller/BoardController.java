package com.eek.kimpli.board.controller;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.board.repository.BoardRepository;
import com.eek.kimpli.board.validator.BoardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    //페이징+검색
    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 5)  Pageable pageable,
                       @RequestParam(required = false,defaultValue = "") String searchText) {
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText,searchText,pageable);
        int currentPage = boards.getPageable().getPageNumber() + 1; // 현재 페이지 번호 (0부터 시작)
        int startPage = Math.max(1, currentPage - 2); // 현재 페이지 주변에 2 페이지씩 보여주기
        int endPage = Math.min(boards.getTotalPages(), startPage + 4);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("boards", boards);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 사용자의 세션 정보
        model.addAttribute("userSession", authentication.getPrincipal());
        return "board/list";
    }

//게시글 상세보기 -> 수정 가능
@GetMapping("/saveForm")
public String saveForm(Model model, @RequestParam(required = false) Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();



//게시물 작성
    if (id == null) {
        model.addAttribute("board", new Board());
        model.addAttribute("username", username);
        System.out.println("게시물의 아이디 값 없을 때:" + model);
    } else {
         Board board = boardRepository.findById(id).orElse(null);
         // 조회수 증가
        board.setViews(board.getViews() + 1);

        // DB에 업데이트
        boardRepository.save(board);
       //게시물 상세보기
        model.addAttribute("board", board);
        model.addAttribute("username", username);
        System.out.println("아이디 값 있을 때:" + board);
    }

    return "board/save";
}



    //DB에 저장
    @PostMapping("/save")
    public String save(@Valid Board board, BindingResult bindingResult) {
        //해당 객체의 필드에 대한 유효성을 검증하고, 검증 결과는 BindingResult 객체에 저장됌
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        board.setAuthor(username);
//        boardValidator.validate(board,bindingResult);
//        if(bindingResult.hasErrors()){
//            System.out.println("A");
//            return "board/save";
//        }
        //
        boardRepository.save(board);
          System.out.println('B');
            return "redirect:/";
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
