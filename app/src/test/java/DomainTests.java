
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.lksnext.parkingbercibengoa.domain.HorarioPlaza;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DomainTests {

    @Test
    public void testReservar() {
        HorarioPlaza horarioPlaza = new HorarioPlaza(null, LocalDate.of(2025,05,13));
        assertTrue(horarioPlaza.reservar(LocalDateTime.of(2025,05,13,23,0), Duration.ofMinutes(30)));
        horarioPlaza.printHorarioPlaza();
    }
    @Test
    public void testReservaRepetida() {
        HorarioPlaza horarioPlaza = new HorarioPlaza(null, LocalDate.of(2025,05,13));
        assertTrue(horarioPlaza.reservar(LocalDateTime.of(2025,05,13,23,0), Duration.ofMinutes(30)));
        System.out.println(horarioPlaza.reservar(LocalDateTime.of(2025,05,13,23,0), Duration.ofMinutes(30)));
        assertFalse(horarioPlaza.reservar(LocalDateTime.of(2025,05,13,23,0), Duration.ofMinutes(30)));
        horarioPlaza.printHorarioPlaza();
    }
}