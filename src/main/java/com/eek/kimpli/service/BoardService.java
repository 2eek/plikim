package com.eek.kimpli.service;

import com.eek.kimpli.dto.BoardDTO;
import com.eek.kimpli.entity.BoardEntity;
import com.eek.kimpli.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

//entity를 뷰단에 노출시키는 것 안좋다 entity는 DB와 연관있으므로
//DTO -> Entity(변환 in Entity Class)
//Entity -> DTO

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void save (BoardDTO boardDTO){
      BoardEntity boardEntity  = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }
}