package com.unsa.etf.Identity.Service.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name="Permission")
public class Permission {
    private Integer id;
    private String name;
}
