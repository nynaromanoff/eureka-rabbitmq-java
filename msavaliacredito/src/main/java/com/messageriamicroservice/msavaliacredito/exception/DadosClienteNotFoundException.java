package com.messageriamicroservice.msavaliacredito.exception;

public class DadosClienteNotFoundException extends Exception{
    public DadosClienteNotFoundException() {
        super("Dados do Cliente não encontrados para o CPF informado! ");
    }
}
