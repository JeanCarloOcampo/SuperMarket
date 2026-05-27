package com.proyect.SuperMarket.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private String codigoBarras;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
    private Long categoriaId;
    private String categoriaNombre;
}
