package com.eek.kimpli.board.Controller;

import com.eek.kimpli.board.dto.BoardDTO;
import com.eek.kimpli.board.Service.BoardService;
import com.eek.kimpli.model.Board1;
import com.eek.kimpli.repository.Board1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    @Autowired //DI일어남. 여기에 인스턴스가 들어옴
    private Board1Repository board1Repository;

    @GetMapping("/list1")
    public String list(Model model) {
        List<Board1> board1s = board1Repository.findAll();
        model.addAttribute("board1s", board1s);
        return "board/list";
    }


    private final BoardService boardService;

    //생성자 주입방식으로 의존성주입받게됨
    @GetMapping("/saveForm")
    public String saveForm(Model model) {
        model.addAttribute("board1", new Board1());
        return "board/save";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute Board1 board1) {
        board1Repository.save(board1);
            return "redirect:/board/list1";
}

//    @PostMapping("/save")
//    public String save(@ModelAttribute BoardDTO boardDTO){
//        System.out.println("boardDTO ="+ boardDTO);
//        boardService.save(boardDTO);
//        return "redirect:/";
//    }
    @GetMapping("/list")
    public String findAll(Model model){
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList",boardDTOList);
        return "board/list";
    }
}
