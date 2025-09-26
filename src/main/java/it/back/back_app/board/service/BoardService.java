package it.back.back_app.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.back.back_app.board.dto.BoardFileDTO;
import it.back.back_app.board.entity.BoardFileEntity;
import it.back.back_app.board.repository.BoardFileRepository;
import it.back.back_app.common.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
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
    private final BoardFileRepository boardFileRepository;
    private final FileUtils fileUtils;

    @Value("${server.file.upload.path}")
    String filePath;


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


    @Transactional
    public Map<String, Object> writeBoard(BoardDTO.Request request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        BoardEntity entity = new BoardEntity();
        //게시글 등록
        entity.setTitle(request.getTitle());
        entity.setContents(request.getContents());
        entity.setWriter("admin");

        if(request.getFile()!=null &&  !request.getFile().isEmpty()) {
            Map<String, Object> fileMap = fileUtils.uploadFiles(request.getFile(), filePath);
            if(fileMap != null) {
                BoardFileEntity fileEntity = new BoardFileEntity();
                fileEntity.setFileName(fileMap.get("fileName").toString());
                fileEntity.setStoredName(fileMap.get("storedFileName").toString());
                fileEntity.setFilePath(fileMap.get("filePath").toString());
                fileEntity.setFileSize(request.getFile().getSize());
                entity.addFiles(fileEntity);
            }else {
                throw new RuntimeException("파일 업로드 오류");
            }
        }
        boardRepository.save(entity);

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "OK");
        return resultMap;
    }


    @Transactional
    public Map<String, Object> updateBoard(BoardDTO.Request request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        BoardEntity entity = boardRepository.getBoard(request.getBrdId())
                .orElseThrow(() -> new RuntimeException("찾는 게시글이 없음"));
        //dto 로 변경
        BoardDTO.Detail detail = BoardDTO.Detail.of(entity);

        if(request.getFile()!=null &&
                !request.getFile().isEmpty()) {
            Map<String, Object> fileMap = fileUtils.uploadFiles(request.getFile(), filePath);

            if(fileMap != null) {
                BoardFileEntity fileEntity = new BoardFileEntity();
                fileEntity.setFileName(fileMap.get("fileName").toString());
                fileEntity.setStoredName(fileMap.get("storedFileName").toString());
                fileEntity.setFilePath(fileMap.get("filePath").toString());
                fileEntity.setFileSize(request.getFile().getSize());

                entity.getFileList().clear();
                entity.addFiles(fileEntity);
            }else {
                throw new RuntimeException("파일 업로드 오류");
            }
        }

        //게시글 업데이트
        entity.setTitle(request.getTitle());
        entity.setContents(request.getContents());

        boardRepository.save(entity);
        // 기존파일 삭제
        if(request.getFile()!=null &&
                !request.getFile().isEmpty()) {

            if(detail.getFileList() != null && !detail.getFileList().isEmpty()) {

                for(BoardFileDTO file : detail.getFileList()) {
                    String oldFilePath = filePath + file.getStoredName();
                    fileUtils.deleteFile(oldFilePath);
                }
            }
        }

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "OK");

        return resultMap;
    }


    @Transactional
    public Map<String, Object> deleteBoard(int brdId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        BoardEntity entity = boardRepository.getBoard(brdId)
                .orElseThrow(() -> new RuntimeException("찾는 게시글이 없음"));

        //dto 로 변경
        BoardDTO.Detail detail = BoardDTO.Detail.of(entity);
        //게시글 삭제 > 고아파일 정책 실행해서 file 디비도 같이 지워짐
        boardRepository.delete(entity);

        // 기존파일 삭제
        if(detail.getFileList() != null && !detail.getFileList().isEmpty()) {

            for(BoardFileDTO file : detail.getFileList()) {
                String oldFilePath = filePath + file.getStoredName();
                fileUtils.deleteFile(oldFilePath);
            }
        }

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "OK");

        return resultMap;
    }


    @Transactional
    public Map<String, Object> deleteFile(int bfId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        BoardFileEntity fileEntity =
                boardFileRepository.findById(bfId).orElseThrow(() -> new RuntimeException(("파일없음")));


        String oldFilePath = fileEntity.getFilePath() + fileEntity.getStoredName();
        fileUtils.deleteFile(oldFilePath);

        boardFileRepository.delete(fileEntity);

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "OK");
        return resultMap;
    }


}
