package it.back.back_app.security.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.back.back_app.security.entity.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity>{


    @EntityGraph(attributePaths = {"role"})
    Page<UserEntity> findAll(Pageable pageable);

    
    @EntityGraph(attributePaths = {"role"})
    Page<UserEntity> findAll(Specification<UserEntity> userSearchSpecification, Pageable pageable);


    
}
