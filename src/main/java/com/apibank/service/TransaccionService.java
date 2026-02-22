package com.apibank.service;

import com.apibank.dto.TransaccionBatchItemDTO;
import com.apibank.entity.Transaccion;
import com.apibank.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private static final int MAX_LIMIT = 1000;
    private static final int MAX_OFFSET = 100_000;

    private final TransaccionRepository transaccionRepository;

    /**
     * List transacciones with optional date range and optional cuentaId filter.
     * Implements real Pageable pagination — equivalent to skip/take in Node.js
     * Prisma.
     */
    @Transactional(readOnly = true)
    public Page<Transaccion> obtenerTransacciones(
            LocalDateTime desde,
            LocalDateTime hasta,
            Long cuentaId,
            int limit,
            int offset) {
        // Sanitize inputs (same caps as Node.js controller)
        if (limit <= 0 || limit > MAX_LIMIT)
            limit = Math.min(Math.max(limit, 1), MAX_LIMIT);
        if (offset < 0)
            offset = 0;

        // offset-based pagination: Pageable uses page index, so pageNumber = offset /
        // limit
        // We compute page number from offset, rounding down
        int pageNumber = (limit > 0) ? offset / limit : 0;

        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by(Sort.Direction.DESC, "fecha"));
        return transaccionRepository.findWithFilters(desde, hasta, cuentaId, pageable);
    }

    /**
     * Bulk insert using saveAll() inside a single @Transactional boundary.
     * Hibernate will batch the inserts according to hibernate.jdbc.batch_size=50.
     * Equivalent to prisma.transacciones.createMany({ data }) in Node.js.
     */
    @Transactional
    public List<Transaccion> crearBatchTransacciones(List<TransaccionBatchItemDTO> items) {
        List<Transaccion> entities = items.stream().map(dto -> {
            Transaccion t = new Transaccion();
            t.setCuentaId(dto.getCuenta_id());
            t.setTipo(dto.getTipo());
            t.setMonto(dto.getMonto());
            return t;
        }).collect(Collectors.toList());

        return transaccionRepository.saveAll(entities);
    }
}
