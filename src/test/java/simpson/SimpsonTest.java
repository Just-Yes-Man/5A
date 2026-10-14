package simpson;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class SimpsonTest {

    private static final double TOL = 0.00001;

   
    @Test void gammaEntero() { assertEquals(24.0, Gamma.of(5), 1e-9); }
    @Test void gammaSemiEntero() { assertEquals(11.63173, Gamma.of(4.5), 1e-5); }
    @Test void gammaMedio() { assertEquals(Math.sqrt(Math.PI), Gamma.of(0.5), 1e-9); }
    @Test void gammaInvalido() { assertThrows(IllegalArgumentException.class, () -> Gamma.of(0)); }

    
    @Test void constanteEjemploPdf() {
        assertEquals(0.388035, new TDistribution(9).constant(), 1e-6);
    }

   
    @Test void triangulo() {
        assertEquals(16.0, Simpson.integrate(x -> 2 * x, 4, 4), 1e-9);
    }
    @Test void xCuadrado() {
        assertEquals(1.0 / 3.0, Simpson.integrate(x -> x * x, 1, 4), 1e-9);
    }

  
    @Test void tabla1Caso1() { assertEquals(0.35006, Simpson.tIntegral(1.1, 9), TOL); }
    @Test void tabla1Caso2() { assertEquals(0.36757, Simpson.tIntegral(1.1812, 10), TOL); }
    @Test void tabla1Caso3() { assertEquals(0.49500, Simpson.tIntegral(2.750, 30), TOL); }

    @Test void ejemploPdf10Segmentos() {
        assertEquals(0.3500589, Simpson.integrate(new TDistribution(9), 1.1, 10), 1e-6);
    }
    @Test void ejemploPdf20Segmentos() {
        assertEquals(0.35005864, Simpson.integrate(new TDistribution(9), 1.1, 20), 1e-7);
    }

    @Test void intervaloPersonalizado() {
        assertEquals(
                Simpson.tIntegral(1.1, 9) - Simpson.tIntegral(0.5, 9),
                Simpson.tIntegral(0.5, 1.1, 9),
                TOL);
    }

  
    @Test void xCeroDaCero() { assertEquals(0.0, Simpson.tIntegral(0, 9), 0.0); }
    @Test void simetriaCasiMitad() {
        // x muy grande: el area de 0 a x tiende a 0.5
        assertEquals(0.5, Simpson.tIntegral(50, 30), 0.001);
    }
    @Test void numSegImpar() {
        assertThrows(IllegalArgumentException.class, () -> Simpson.integrate(x -> x, 1, 3));
    }
    @Test void xNegativo() {
        assertThrows(IllegalArgumentException.class, () -> Simpson.tIntegral(-1, 9));
    }
    @Test void dofInvalido() {
        Executable e = () -> new TDistribution(0);
        assertThrows(IllegalArgumentException.class, e);
    }
}
