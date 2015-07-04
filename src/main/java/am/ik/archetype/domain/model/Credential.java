package am.ik.archetype.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@ToString(exclude = "account")
@EqualsAndHashCode(exclude = "account")
public class Credential implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long credentialsId;
    @OneToOne(mappedBy = "credential", fetch = FetchType.LAZY)
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
