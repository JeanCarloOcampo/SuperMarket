package com.proyect.SuperMarket.repository;

import com.proyect.SuperMarket.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByCodigoBarras(String codigoBarras);

    boolean existsByCodigoBarrasAndIdNot(String codigoBarras, Long id);

    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    Optional<Producto> findByIdAndActivoTrue(Long id);

    List<Producto> findAllByActivoTrue();
}
