package com.proyect.SuperMarket.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorResponseDTO {

    private Long id;
    private String nombre;
    private String nit;
    private String email;
    private String telefono;
    private List<ProductoResponseDTO> productos;
}
