
import com.lksnext.parkingbercibengoa.domain.HorarioPlaza;

import org.junit.Test;

public class DomainTests {

    @Test
    public void testHorarioPlaza() {
        HorarioPlaza horarioPlaza = new HorarioPlaza(null, null);
        horarioPlaza.printHorarioPlaza();
    }
}