package com.proyect.SuperMarket.controller;

import com.proyect.SuperMarket.dto.request.EntradaAlmacenRequestDTO;
import com.proyect.SuperMarket.dto.request.ProveedorRequestDTO;
import com.proyect.SuperMarket.dto.response.EntradaAlmacenResponseDTO;
import com.proyect.SuperMarket.dto.response.ProveedorResponseDTO;
import com.proyect.SuperMarket.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping("/api/proveedores")
    public ResponseEntity<List<ProveedorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(proveedorService.listarTodos());
    }

    @GetMapping("/api/proveedores/{id}")
    public ResponseEntity<ProveedorResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.obtenerPorId(id));
    }

    @PostMapping("/api/proveedores")
    public ResponseEntity<ProveedorResponseDTO> crear(@Valid @RequestBody ProveedorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.crear(dto));
    }

    @PutMapping("/api/proveedores/{id}")
    public ResponseEntity<ProveedorResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorRequestDTO dto) {
        return ResponseEntity.ok(proveedorService.actualizar(id, dto));
    }

    @DeleteMapping("/api/proveedores/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/almacen/entrada")
    public ResponseEntity<EntradaAlmacenResponseDTO> entradaAlmacen(
            @Valid @RequestBody EntradaAlmacenRequestDTO dto) {
        return ResponseEntity.ok(proveedorService.entradaAlmacen(dto));
    }
}
