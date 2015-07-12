package am.ik.archetype.domain.model;

import am.ik.archetype.domain.model.vo.Password;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Credential implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long credentialsId;
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name = "password"), name = "value"),
            @AttributeOverride(column = @Column(name = "algorithm"), name = "algorithm")
    })
    private Password password;

    @Version
    private Long version;

}
