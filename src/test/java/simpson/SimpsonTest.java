package simpson;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class SimpsonTest {

    private static final double TOL = 0.00001;

   
    @Test void gammaEntero() { assertEquals(24.0, Gamma.of(5), 1e-9); }
    @Test void gammaSemiEntero() { assertEquals(11.63173, Gamma.of(4.5), 1e-5); }
    @Test void gammaMedio() { assertEquals(Math.sqrt(Math.PI), Gamma.of(0.5), 1e-9); }
    @Test void gammaUno() { assertEquals(1.0, Gamma.of(1.0), 1e-9); }
    @Test void gammaInvalido() { assertThrows(IllegalArgumentException.class, () -> Gamma.of(0)); }
    @Test void gammaNoEnteroNiSemiEntero() {
        assertThrows(IllegalArgumentException.class, () -> Gamma.of(0.75));
    }

    
    @Test void constanteEjemploPdf() {
        assertEquals(0.389, new TDistribution(9).constant(), 1e-6);
    }
    @Test void distribucionEvaluaDensidad() {
        TDistribution distribution = new TDistribution(9);
        assertEquals(distribution.constant(), distribution.applyAsDouble(0), 1e-9);
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
    @Test void numSegNoPositivo() {
        assertThrows(IllegalArgumentException.class, () -> Simpson.integrate(x -> x, 1, 0));
    }
    @Test void errorInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Simpson.integrate(x -> x, 0, 1, 2, 0));
    }
    @Test void integralRefinadaDesdeCero() {
        assertEquals(1.0 / 3.0, Simpson.integrate(x -> x * x, 1, 4, TOL), TOL);
    }
    @Test void integralRefinadaConXNegativo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Simpson.integrate(x -> x, -1, 2, TOL));
    }
    @Test void intervaloVacio() {
        assertEquals(0.0, Simpson.integrate(x -> x, 2, 2, 2, TOL), 0.0);
    }
    @Test void numSegIntervaloNoPositivo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Simpson.integrate(x -> x, 0.0, 1.0, 0));
    }
    @Test void numSegIntervaloImpar() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Simpson.integrate(x -> x, 0.0, 1.0, 3));
    }
    @Test void xNegativo() {
        assertThrows(IllegalArgumentException.class, () -> Simpson.tIntegral(-1, 9));
    }
    @Test void dofInvalido() {
        Executable e = () -> new TDistribution(0);
        assertThrows(IllegalArgumentException.class, e);
    }

    @Test void mainConDosArgumentos() {
        new Main();
        assertTrue(runMain("1.1 9").contains("p = 0.35006"));
    }

    @Test void mainConTresArgumentos() {
        assertTrue(runMain("1.1 9 0.5").contains("p = 0.16459"));
    }

    @Test void mainSinArgumentosYSalidaInmediata() {
        assertTrue(runMain("", "0").contains("esperado"));
    }

    @Test void mainSinEntradaNumerica() {
        assertTrue(runMainWithArguments(new String[0], new String[0]).contains("esperado"));
    }

    @Test void mainModoInteractivo() {
        String output = runMain("", "1.1", "9", "0.5", "0");
        assertTrue(output.contains("p = 0.16459"));
    }

    private static String runMain(String... input) {
        return runMainWithArguments(new String[0], input);
    }

    private static String runMain(String arguments) {
        return runMainWithArguments(arguments.split(" "), new String[]{"0"});
    }

    private static String runMainWithArguments(String[] arguments, String[] input) {
        PrintStream originalOut = System.out;
        java.io.InputStream originalIn = System.in;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            System.setIn(new ByteArrayInputStream(
                    String.join(System.lineSeparator(), input).getBytes(StandardCharsets.UTF_8)));
            Main.main(arguments);
            return output.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }
}
