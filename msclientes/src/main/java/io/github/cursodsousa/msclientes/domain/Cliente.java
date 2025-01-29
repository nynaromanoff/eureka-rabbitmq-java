package io.github.cursodsousa.msclientes.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String cpf;
    @Column
    private String name;
    @Column
    private Integer idade;

    public Cliente(String cpf, String name, Integer idade) {
        this.cpf = cpf;
        this.name = name;
        this.idade = idade;
    }
}
