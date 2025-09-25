package it.back.back_app.security.entity;
import org.hibernate.annotations.ColumnDefault;

import it.back.back_app.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="tb_users")
public class UserEntity extends BaseEntity {
    
    @Id
    private String userId;

    private String passwd;

    private String userName;

    private String birth;

    private String gender;

    private String phone;

    private String email;

    private String addr;

    private String addrDetail;

    @Column( columnDefinition = "CHAR(1)")
    private String useYn;
    @Column( columnDefinition = "CHAR(1)")
    @ColumnDefault("N")
    private String delYn;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_role")
    private UserRoleEntity role;

}
