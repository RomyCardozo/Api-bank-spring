package com.apibank.service;

import com.apibank.entity.Cliente;
import com.apibank.entity.Cuenta;
import com.apibank.entity.TipoCuenta;
import com.apibank.repository.ClienteRepository;
import com.apibank.repository.CuentaRepository;
import com.apibank.repository.TipoCuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SetupService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final TipoCuentaRepository tipoCuentaRepository;

    /**
     * Creates a test dataset: account types + N clients each with 1-3 accounts.
     * Equivalent to the Node.js crearDataset() in setup.service.js.
     */
    @Transactional
    public Map<String, Object> crearDataset(int cantidad) {
        // Ensure base account types exist
        List<Map<String, String>> tipos = List.of(
                Map.of("nombre", "Ahorros", "descripcion", "Cuenta de ahorros"),
                Map.of("nombre", "Corriente", "descripcion", "Cuenta corriente"));

        for (Map<String, String> t : tipos) {
            if (tipoCuentaRepository.findByNombre(t.get("nombre")).isEmpty()) {
                TipoCuenta tc = new TipoCuenta();
                tc.setNombre(t.get("nombre"));
                tc.setDescripcion(t.get("descripcion"));
                tipoCuentaRepository.save(tc);
            }
        }

        List<TipoCuenta> tiposAll = tipoCuentaRepository.findAll();
        BigDecimal saldoInicial = new BigDecimal("1000.00");

        List<Cliente> clientes = new ArrayList<>();
        List<Cuenta> cuentas = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            long ts = System.currentTimeMillis();

            Cliente cliente = new Cliente();
            cliente.setNombre("Cliente " + ts + "_" + i);
            cliente.setEmail("cliente" + ts + "_" + i + "@test.local");
            cliente = clienteRepository.save(cliente);
            clientes.add(cliente);

            // 1-3 accounts per client
            int cuentasPorCliente = (int) (Math.random() * 3) + 1;
            for (int j = 0; j < cuentasPorCliente; j++) {
                TipoCuenta tipo = tiposAll.get((i + j) % tiposAll.size());
                String numero = (ts + "" + i + j)
                        .substring(0, Math.min(20, (ts + "" + i + j).length()));

                Cuenta cuenta = new Cuenta();
                cuenta.setClienteId(cliente.getId());
                cuenta.setTipoCuentaId(tipo.getId());
                cuenta.setNumeroCuenta(numero);
                cuenta.setSaldo(saldoInicial);
                cuenta = cuentaRepository.save(cuenta);
                cuentas.add(cuenta);
            }
        }

        return Map.of(
                "clientes", clientes.size(),
                "cuentas", cuentas.size());
    }
}
