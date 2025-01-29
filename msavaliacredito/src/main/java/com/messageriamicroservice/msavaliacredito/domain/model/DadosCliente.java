package com.messageriamicroservice.msavaliacredito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DadosCliente {
    private Long id;
    private String name;
    private Integer idade;

}
