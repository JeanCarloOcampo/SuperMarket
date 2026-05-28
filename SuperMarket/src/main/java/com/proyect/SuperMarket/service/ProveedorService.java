package com.proyect.SuperMarket.service;

import com.proyect.SuperMarket.dto.request.EntradaAlmacenRequestDTO;
import com.proyect.SuperMarket.dto.request.ProveedorRequestDTO;
import com.proyect.SuperMarket.dto.response.EntradaAlmacenResponseDTO;
import com.proyect.SuperMarket.dto.response.ProductoResponseDTO;
import com.proyect.SuperMarket.dto.response.ProveedorResponseDTO;
import com.proyect.SuperMarket.entity.Producto;
import com.proyect.SuperMarket.entity.Proveedor;
import com.proyect.SuperMarket.exception.BusinessException;
import com.proyect.SuperMarket.exception.ResourceNotFoundException;
import com.proyect.SuperMarket.repository.ProductoRepository;
import com.proyect.SuperMarket.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;

    public List<ProveedorResponseDTO> listarTodos() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProveedorResponseDTO obtenerPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        return toResponseDTO(proveedor);
    }

    public ProveedorResponseDTO crear(ProveedorRequestDTO dto) {
    
        if (proveedorRepository.existsByNit(dto.getNit())) {
            throw new BusinessException("Ya existe un proveedor con el NIT: " + dto.getNit());
        }

        Proveedor proveedor = Proveedor.builder()
                .nombre(dto.getNombre())
                .nit(dto.getNit())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .build();

        return toResponseDTO(proveedorRepository.save(proveedor));
    }

    public ProveedorResponseDTO actualizar(Long id, ProveedorRequestDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));

        if (proveedorRepository.existsByNitAndIdNot(dto.getNit(), id)) {
            throw new BusinessException("Ya existe un proveedor con el NIT: " + dto.getNit());
        }

        proveedor.setNombre(dto.getNombre());
        proveedor.setNit(dto.getNit());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());

        return toResponseDTO(proveedorRepository.save(proveedor));
    }

    public void eliminar(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proveedor no encontrado con id: " + id);
        }
        proveedorRepository.deleteById(id);
    }

    @Transactional
    public EntradaAlmacenResponseDTO entradaAlmacen(EntradaAlmacenRequestDTO dto) {
        Producto producto = productoRepository.findByIdAndActivoTrue(dto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + dto.getProductoId()));

        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + dto.getProveedorId()));

        if (!producto.getProveedores().contains(proveedor)) {
            producto.getProveedores().add(proveedor);
        }
        
        producto.setStock(producto.getStock() + dto.getCantidad());
        productoRepository.save(producto);

        return EntradaAlmacenResponseDTO.builder()
                .productoId(producto.getId())
                .productoNombre(producto.getNombre())
                .proveedorId(proveedor.getId())
                .proveedorNombre(proveedor.getNombre())
                .cantidadIngresada(dto.getCantidad())
                .stockActual(producto.getStock())
                .mensaje("Entrada de almacen registrada exitosamente")
                .build();
    }

    private ProveedorResponseDTO toResponseDTO(Proveedor proveedor) {
        List<ProductoResponseDTO> productos = proveedor.getProductos() == null ? List.of() :
                proveedor.getProductos().stream()
                        .filter(p -> p.getActivo())
                        .map(p -> ProductoResponseDTO.builder()
                                .id(p.getId())
                                .nombre(p.getNombre())
                                .codigoBarras(p.getCodigoBarras())
                                .precio(p.getPrecio())
                                .stock(p.getStock())
                                .activo(p.getActivo())
                                .categoriaId(p.getCategoria().getId())
                                .categoriaNombre(p.getCategoria().getNombre())
                                .build())
                        .collect(Collectors.toList());

        return ProveedorResponseDTO.builder()
                .id(proveedor.getId())
                .nombre(proveedor.getNombre())
                .nit(proveedor.getNit())
                .email(proveedor.getEmail())
                .telefono(proveedor.getTelefono())
                .productos(productos)
                .build();
    }
}
