package com.proyect.SuperMarket.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntradaAlmacenResponseDTO {

    private Long productoId;
    private String productoNombre;
    private Long proveedorId;
    private String proveedorNombre;
    private Integer cantidadIngresada;
    private Integer stockActual;
    private String mensaje;
}
