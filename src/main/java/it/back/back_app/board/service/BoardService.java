package it.back.back_app.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.back.back_app.board.dto.BoardDTO;
import it.back.back_app.board.entity.BoardEntity;
import it.back.back_app.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;


    @Transactional(readOnly = true)
    public Map<String, Object> getBoardList(Pageable pageable) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        Page<BoardEntity> boardList = boardRepository.findAll(pageable);

        List<BoardDTO.Response> list = 
                boardList.getContent().stream()
                .map(BoardDTO.Response::of).toList();
        
        resultMap.put("total", boardList.getTotalElements());
        resultMap.put("page", boardList.getNumber());
        resultMap.put("content", list);

        return resultMap;
    }

    @Transactional(readOnly = true)
    public BoardDTO.Detail getBoard(int brdId) throws Exception {
        BoardEntity entity = boardRepository.getBoard(brdId)
                    .orElseThrow(() -> new RuntimeException("찾는 게시글이 없음"));
        
        return  BoardDTO.Detail.of(entity);
    }


}
