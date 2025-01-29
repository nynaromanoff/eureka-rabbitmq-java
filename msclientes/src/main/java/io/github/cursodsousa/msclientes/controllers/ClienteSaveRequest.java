package io.github.cursodsousa.msclientes.controllers;

import io.github.cursodsousa.msclientes.domain.Cliente;
import lombok.Data;

@Data
public class ClienteSaveRequest {
    private String cpf;
    private String nome;
    private Integer idade;

    public Cliente modelClient(){
        return new Cliente(cpf, nome, idade);
    }
}
