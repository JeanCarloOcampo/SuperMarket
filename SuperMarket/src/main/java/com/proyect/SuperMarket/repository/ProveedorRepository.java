package com.proyect.SuperMarket.repository;

import com.proyect.SuperMarket.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    boolean existsByNit(String nit);

    boolean existsByNitAndIdNot(String nit, Long id);
}
