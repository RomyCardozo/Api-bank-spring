package com.apibank.repository;

import com.apibank.entity.Transaccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    /**
     * Flexible query supporting optional date range filter and optional cuentaId
     * filter.
     * Equivalent to the dynamic 'where' object built in the Node.js transacciones
     * service.
     * Returns a Page<Transaccion> to support real pagination via Pageable.
     */
    @Query("""
            SELECT t FROM Transaccion t
            WHERE (:desde IS NULL OR t.fecha >= :desde)
              AND (:hasta IS NULL OR t.fecha <= :hasta)
              AND (:cuentaId IS NULL OR t.cuentaId = :cuentaId)
            ORDER BY t.fecha DESC
            """)
    Page<Transaccion> findWithFilters(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta,
            @Param("cuentaId") Long cuentaId,
            Pageable pageable);
}
