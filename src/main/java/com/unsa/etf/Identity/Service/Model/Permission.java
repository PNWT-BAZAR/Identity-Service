package com.unsa.etf.Identity.Service.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Permission {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    private String id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Role> roles;

    public Permission(String name) {
        this.name = name;
    }
}
