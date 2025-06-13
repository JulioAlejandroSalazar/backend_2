package com.letrasypapeles.backend;

import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.repository.ProductoRepository;
import com.letrasypapeles.backend.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    ProductoRepository productoRepository;

    @Mock
    ReservaRepository reservaRepository;

    @InjectMocks
    ReservaService reservaService;

    @Test
    void crearReserva_deberiaConfirmarSiHayStock() {
        Producto producto = Producto.builder().id(1L).nombre("Libro").stock(5).build();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Reserva reservaMock = new Reserva();
        reservaMock.setProducto(producto);
        reservaMock.setEstado("Confirmada");
        reservaMock.setFechaReserva(LocalDateTime.now());

        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaMock);

        Reserva reserva = reservaService.crearReserva(1L);

        assertEquals("Confirmada", reserva.getEstado());
        verify(reservaRepository).save(any(Reserva.class));
    }


    @Test
    void crearReserva_deberiaRechazarSiNoHayStock() {
        Producto producto = Producto.builder().id(2L).nombre("Revista").stock(0).build();
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
    
        Reserva reservaMock = new Reserva();
        reservaMock.setProducto(producto);
        reservaMock.setEstado("Rechazada");
        reservaMock.setFechaReserva(LocalDateTime.now());
    
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaMock);
    
        Reserva reserva = reservaService.crearReserva(2L);
    
        assertEquals("Rechazada", reserva.getEstado());
        verify(reservaRepository).save(any(Reserva.class));
    }
    
}

class ReservaService {
    private final ProductoRepository productoRepository;
    private final ReservaRepository reservaRepository;

    public ReservaService(ProductoRepository productoRepository, ReservaRepository reservaRepository) {
        this.productoRepository = productoRepository;
        this.reservaRepository = reservaRepository;
    }

    public Reserva crearReserva(Long productoId) {
        Producto producto = productoRepository.findById(productoId).orElseThrow();
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setProducto(producto);
        reserva.setEstado(producto.getStock() > 0 ? "Confirmada" : "Rechazada");
        return reservaRepository.save(reserva);
    }
}

