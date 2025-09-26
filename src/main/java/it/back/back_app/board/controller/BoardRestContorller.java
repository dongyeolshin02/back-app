package it.back.back_app.board.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.back.back_app.board.dto.BoardDTO;
import it.back.back_app.board.service.BoardService;
import it.back.back_app.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardRestContorller {

    private final BoardService boardService;

    @GetMapping("/board")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBoardList(@PageableDefault(size= 10, page = 0, 
                                sort = "createDate", direction = Direction.DESC)Pageable pageable) throws Exception {

        log.info("-----  게시글 가져오기 -------");
        Map<String, Object> resultMap = boardService.getBoardList(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(resultMap));
    }

      @GetMapping("/board/{brdId}")
    public ResponseEntity<ApiResponse<BoardDTO.Detail>> getBoard(@PathVariable("brdId")int brdId) throws Exception {

        log.info(brdId +" 번 글 가져오기");
        BoardDTO.Detail  detail = boardService.getBoard(brdId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(detail));
    }


    @DeleteMapping("/board/{brdId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>>
        deleteBoard(@PathVariable("brdId")int brdId) throws Exception {

        Map<String, Object> resultMap = boardService.deleteBoard(brdId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(resultMap));
    }

    @PostMapping("/board")
    public ResponseEntity<ApiResponse<Map<String, Object>>> writeBoard(BoardDTO.Request request) throws Exception {
        Map<String, Object> resultMap = boardService.writeBoard(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(resultMap));
    }


    @PutMapping("/board")
    public ResponseEntity<ApiResponse<Map<String, Object>>>
        updateBoard(BoardDTO.Request request) throws Exception {

        Map<String, Object> resultMap = boardService.updateBoard(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(resultMap));
    }

    @DeleteMapping("/board/file/{bfId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>>
        deleteFile(@PathVariable("bfId")int bfId) throws Exception {

        Map<String, Object> resultMap = boardService.deleteFile(bfId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(resultMap));
    }
}
