package com.proyect.SuperMarket.service;

import com.proyect.SuperMarket.dto.request.ProductoRequestDTO;
import com.proyect.SuperMarket.dto.response.ProductoResponseDTO;
import com.proyect.SuperMarket.entity.Categoria;
import com.proyect.SuperMarket.entity.Producto;
import com.proyect.SuperMarket.exception.BusinessException;
import com.proyect.SuperMarket.exception.ResourceNotFoundException;
import com.proyect.SuperMarket.repository.CategoriaRepository;
import com.proyect.SuperMarket.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAllByActivoTrue()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return toResponseDTO(producto);
    }

    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        if (productoRepository.existsByCodigoBarras(dto.getCodigoBarras())) {
            throw new BusinessException("Ya existe un producto con el codigo de barras: " + dto.getCodigoBarras());
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + dto.getCategoriaId()));

        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .codigoBarras(dto.getCodigoBarras())
                .precio(dto.getPrecio())
                .stock(dto.getStock())
                .activo(true)
                .categoria(categoria)
                .build();

        return toResponseDTO(productoRepository.save(producto));
    }

    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        if (productoRepository.existsByCodigoBarrasAndIdNot(dto.getCodigoBarras(), id)) {
            throw new BusinessException("Ya existe un producto con el codigo de barras: " + dto.getCodigoBarras());
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + dto.getCategoriaId()));

        producto.setNombre(dto.getNombre());
        producto.setCodigoBarras(dto.getCodigoBarras());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(categoria);

        return toResponseDTO(productoRepository.save(producto));
    }

    public void eliminar(Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public ProductoResponseDTO toResponseDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .codigoBarras(producto.getCodigoBarras())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .activo(producto.getActivo())
                .categoriaId(producto.getCategoria().getId())
                .categoriaNombre(producto.getCategoria().getNombre())
                .build();
    }
}
