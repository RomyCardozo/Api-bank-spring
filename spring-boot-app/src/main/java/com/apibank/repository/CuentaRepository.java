package com.apibank.repository;

import com.apibank.entity.Cuenta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    List<Cuenta> findByClienteId(Integer clienteId);

    /**
     * Acquires a pessimistic write lock (SELECT FOR UPDATE) on a single account.
     * Used in TransferenciaService to prevent lost-update concurrency issues,
     * equivalent to the raw SQL SELECT FOR UPDATE in the Node.js service.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cuenta c WHERE c.id = :id")
    Optional<Cuenta> findByIdWithLock(@Param("id") Long id);
}
