package com.proyect.SuperMarket.service;

import com.proyect.SuperMarket.dto.request.CategoriaRequestDTO;
import com.proyect.SuperMarket.dto.response.CategoriaResponseDTO;
import com.proyect.SuperMarket.dto.response.ProductoResponseDTO;
import com.proyect.SuperMarket.entity.Categoria;
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
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoriaResponseDTO obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
        return toResponseDTO(categoria);
    }

    public CategoriaResponseDTO crear(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNombre(dto.getNombre())) {
            throw new BusinessException("Ya existe una categoria con el nombre: " + dto.getNombre());
        }
        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
        return toResponseDTO(categoriaRepository.save(categoria));
    }

    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));

        if (categoriaRepository.existsByNombre(dto.getNombre()) &&
                !categoria.getNombre().equalsIgnoreCase(dto.getNombre())) {
            throw new BusinessException("Ya existe una categoria con el nombre: " + dto.getNombre());
        }

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return toResponseDTO(categoriaRepository.save(categoria));
    }

    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        List<ProductoResponseDTO> productosActivos = productoRepository
                .findByCategoriaIdAndActivoTrue(categoria.getId())
                .stream()
                .map(p -> ProductoResponseDTO.builder()
                        .id(p.getId())
                        .nombre(p.getNombre())
                        .codigoBarras(p.getCodigoBarras())
                        .precio(p.getPrecio())
                        .stock(p.getStock())
                        .activo(p.getActivo())
                        .categoriaId(categoria.getId())
                        .categoriaNombre(categoria.getNombre())
                        .build())
                .collect(Collectors.toList());

        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .productos(productosActivos)
                .build();
    }
}
