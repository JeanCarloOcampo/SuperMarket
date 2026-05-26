package com.proyect.SuperMarket.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;

    private List<ProductoResponseDTO> productos;
}
