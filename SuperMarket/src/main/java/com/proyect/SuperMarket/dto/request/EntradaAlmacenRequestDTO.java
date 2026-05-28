package com.proyect.SuperMarket.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntradaAlmacenRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "El ID del proveedor es obligatorio")
    private Long proveedorId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}
