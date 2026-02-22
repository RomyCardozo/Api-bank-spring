package com.apibank.service;

import com.apibank.dto.TransferenciaRequestDTO;
import com.apibank.dto.TransferenciaResponseDTO;
import com.apibank.entity.Cuenta;
import com.apibank.entity.Transferencia;
import com.apibank.entity.Transaccion;
import com.apibank.exception.EntityNotFoundException;
import com.apibank.exception.InsufficientBalanceException;
import com.apibank.repository.CuentaRepository;
import com.apibank.repository.TransferenciaRepository;
import com.apibank.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private static final Logger log = LoggerFactory.getLogger(TransferenciaService.class);

    private final CuentaRepository cuentaRepository;
    private final TransferenciaRepository transferenciaRepository;
    private final TransaccionRepository transaccionRepository;

    /**
     * Executes a fund transfer atomically.
     *
     * Strategy mirrors the Node.js service exactly:
     * 1. Acquire pessimistic write locks in DETERMINISTIC ORDER (lower ID first)
     * → prevents deadlocks when two concurrent transfers try to lock (A,B) and
     * (B,A).
     * 2. Validate origin balance.
     * 3. Debit origin, credit destination.
     * 4. Persist Transferencia record.
     * 5. Persist two Transaccion records (debito / credito) for audit.
     *
     * ISOLATION = READ_COMMITTED matches PostgreSQL default and aligns with the
     * Node.js Prisma transaction which also runs at READ_COMMITTED.
     * The SELECT FOR UPDATE locks provide the necessary serialization for these
     * rows.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TransferenciaResponseDTO crearTransferencia(TransferenciaRequestDTO request) {
        Long origenId = request.getCuenta_origen_id();
        Long destinoId = request.getCuenta_destino_id();
        BigDecimal monto = request.getMonto();

        // ── Deterministic lock order (lower ID first) ──────────────────────
        // This is the key anti-deadlock pattern from the Node.js service.
        Long firstId = Math.min(origenId, destinoId);
        Long secondId = Math.max(origenId, destinoId);

        // Acquire SELECT FOR UPDATE locks
        Cuenta first = cuentaRepository.findByIdWithLock(firstId)
                .orElseThrow(() -> new EntityNotFoundException(
                        firstId.equals(origenId)
                                ? "Cuenta origen no encontrada"
                                : "Cuenta destino no encontrada"));

        Cuenta second = cuentaRepository.findByIdWithLock(secondId)
                .orElseThrow(() -> new EntityNotFoundException(
                        secondId.equals(origenId)
                                ? "Cuenta origen no encontrada"
                                : "Cuenta destino no encontrada"));

        // Re-map to semantic names
        Cuenta cuentaOrigen = firstId.equals(origenId) ? first : second;
        Cuenta cuentaDestino = firstId.equals(destinoId) ? first : second;

        // ── Validate balance ────────────────────────────────────────────────
        if (cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new InsufficientBalanceException();
        }

        // ── Update balances ─────────────────────────────────────────────────
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));

        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);

        // ── Persist Transferencia ───────────────────────────────────────────
        Transferencia transferencia = new Transferencia();
        transferencia.setCuentaOrigenId(origenId);
        transferencia.setCuentaDestinoId(destinoId);
        transferencia.setMonto(monto);
        transferencia = transferenciaRepository.save(transferencia);

        // ── Persist audit Transacciones (debito + credito) ─────────────────
        // Uses saveAll for a single batch round-trip instead of two separate inserts
        Transaccion debito = new Transaccion();
        debito.setCuentaId(origenId);
        debito.setTipo("debito");
        debito.setMonto(monto);

        Transaccion credito = new Transaccion();
        credito.setCuentaId(destinoId);
        credito.setTipo("credito");
        credito.setMonto(monto);

        transaccionRepository.saveAll(List.of(debito, credito));

        log.info("Transfer completed: origen={} destino={} monto={} id={}",
                origenId, destinoId, monto, transferencia.getId());

        return toResponseDTO(transferencia);
    }

    @Transactional(readOnly = true)
    public TransferenciaResponseDTO obtenerTransferenciaPorId(Long id) {
        Transferencia transferencia = transferenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transferencia no encontrada"));
        return toResponseDTO(transferencia);
    }

    // ── Mapping helper ──────────────────────────────────────────────────────
    private TransferenciaResponseDTO toResponseDTO(Transferencia t) {
        return new TransferenciaResponseDTO(
                t.getId(),
                t.getCuentaOrigenId(),
                t.getCuentaDestinoId(),
                t.getMonto(),
                t.getFecha());
    }
}
