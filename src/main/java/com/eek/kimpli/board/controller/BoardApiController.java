package com.eek.kimpli.board.controller;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
class BoardApiController {

  //인스턴스 변수로서 필드를 선언한 것이고, 아마도 이것을 생성자에서 초기화
  final BoardRepository repository;

  //전체 조회. 게시글 제목과 내용. 검색 방법 :  /api/boards?title=제목
  @GetMapping("/boards")
  List<Board> all(@RequestParam(required = false, defaultValue = "")String title,
                  @RequestParam(required = false, defaultValue = "")String content) {
    if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) {
      //없으면 전체 리스트 출력
      //http://localhost:9090/api/boards?아무값
      return repository.findAll();
    }else{
        //JPA Query Creation Or 조건. 첫 번째 필드, 두 번째 필드
      return repository.findByTitleOrContent(title, content);

    }
  }


  //게시글 저장. post요청으로 받는 파라미터가 requestBody이다.
  @PostMapping("/boards")
  Board newBoard(@RequestBody Board newBoard) {

    return repository.save(newBoard);
  }

  // 게시글 상세조회
  @GetMapping("/boards/{id}")
  Board one(@PathVariable Long id) {

    return repository.findById(id).orElse(null);
  }


//업데이트
  @PutMapping("/boards/{id}") //"/boards/{id}" 경로로 HTTP PUT 요청을 처리 "/boards/{id}" 경로로 HTTP PUT 요청을 처리
  Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {
  // 요소에 특정 함수를 적용하여 새로운 요소를 생성(.map함수를 이용)
    return repository.findById(id)
      .map(board -> {
        board.setTitle(newBoard.getTitle());
        board.setContent(newBoard.getContent());
        return repository.save(board);
      })
            //게시글이 존재하지 않는 경우 람다 표현식을 실행.게시글이 없다면 새로운 글 생성.
      .orElseGet(() -> {
        newBoard.setId(id);
        return repository.save(newBoard);
      });
  }
//삭제
  @DeleteMapping("/boards/{id}")
  void deleteBoard(@PathVariable Long id) {

    repository.deleteById(id);
  }
}