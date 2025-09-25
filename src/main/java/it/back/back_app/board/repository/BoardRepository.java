package it.back.back_app.board.repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.back.back_app.board.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, 
                                            JpaSpecificationExecutor<BoardEntity> {


    //EntityGraph 는  기본적으로 JPARepository 가 제공하는 함수를  overrride 만 가능 
    @EntityGraph(attributePaths = {"fileList"})
    Page<BoardEntity> findAll(Pageable pageable);

    @Query(value="""
           select b from BoardEntity b left join fetch b.fileList
           where b.brdId = :brdId 
           """)
    Optional<BoardEntity> getBoard(@Param("brdId")int brdId);
    
}
