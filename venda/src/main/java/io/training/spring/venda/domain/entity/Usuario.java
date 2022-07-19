package io.training.spring.venda.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "{campo.login.obrigatorio}")
    @Column
    private String login;

    @NotEmpty(message = "{campo.senha.obrigatorio}")
    @Column
    private String senha;

    @Column
    private boolean admin;
}
