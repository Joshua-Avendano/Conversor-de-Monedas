import com.Alurachallenges.conversordemonedas.clases.Moneda;
import com.Alurachallenges.conversordemonedas.comunicacion.MostrarMenus;
import com.Alurachallenges.conversordemonedas.comunicacion.GeneradorArchivo;
import com.Alurachallenges.conversordemonedas.comunicacion.Mensaje;
import com.Alurachallenges.conversordemonedas.conversiones.ConvertirMoneda;
import com.Alurachallenges.conversordemonedas.conversiones.DatosFormatosMonedas;

import java.io.IOException;
import java.util.*;

public class Principal {
    private static final int SALIDA = 11;
    private static final int VER_HISTORIAL = 12;

    public static void main(String[] args) {
        System.out.println("*******    Bienvenido/a al Conversor de monedas.  **********");
        try (Scanner entradaUsuario = new Scanner(System.in)) {
            ConvertirMoneda conversor = new ConvertirMoneda();
            MostrarMenus menuOpciones = new MostrarMenus();
            DatosFormatosMonedas datoMoneda = new DatosFormatosMonedas();
            List<String> conversiones = new ArrayList<>();
            conversiones.add("Fecha -----------------Conversión\n");

            while (true) {
                try {
                    menuOpciones.mostrarMenuDeOpcionesDeConversion();
                    int opcion = entradaUsuario.nextInt();

                    if (opcion == SALIDA) {
                        System.out.println("¡Salida Exitosa!");
                        break;
                    } else if (opcion == VER_HISTORIAL) {
                        mostrarHistorial(conversiones, entradaUsuario);
                    } else if (datoMoneda.getValoresPaisesDisponibles().containsKey(opcion)) {
                        procesarConversion(opcion, entradaUsuario, datoMoneda, conversor, conversiones);
                    } else {
                        System.out.println("Opción ingresada no válida, vuelva a intentar.");
                    }
                } catch (InputMismatchException e) {
                    mostrarMensajeAdvertencia();
                    entradaUsuario.nextLine(); // Limpiar el buffer de entrada
                }
            }
        }
    }

    private static void mostrarHistorial(List<String> conversiones, Scanner entradaUsuario) {
        if (conversiones.size() > 1) {
            conversiones.sort(Collections.reverseOrder());
            System.out.println(conversiones);
            System.out.println("Desea guardar el archivo. Ingrese 'Si' o 'No'");
            String guardar = entradaUsuario.next();
            if (guardar.equalsIgnoreCase("si")) {
                new GeneradorArchivo().guardarArchivo(conversiones);
                System.out.println("¡Archivo guardado con éxito!\n");
            }
        } else {
            System.out.println("*** No hay historial de conversiones para consultar. ***\n");
        }
    }

    private static void procesarConversion(int opcion, Scanner entradaUsuario, DatosFormatosMonedas datoMoneda, ConvertirMoneda conversor, List<String> conversiones) {
        System.out.println("*** Ingresa la cantidad que desea convertir. ***");
        double cantidad = entradaUsuario.nextDouble();
        String monedaBase = datoMoneda.obtenerFormatos(opcion, 0);
        String monedaDestino = datoMoneda.obtenerFormatos(opcion, 1);
        Moneda moneda = conversor.convertirMoneda(monedaBase, monedaDestino, cantidad);
        String conversion = new Mensaje(cantidad, moneda).toString();
        System.out.println(conversion);
        conversiones.add(conversion);
    }

    private static void mostrarMensajeAdvertencia() {
        System.out.println("""
                Advertencia:
                1. Solo se pueden ingresar números enteros para escoger
                   la opción inicial.
                2. Al ingresar el valor a convertir, puede ingresar números
                   con comas como por ejemplo: 5,98, 4980,00.
                3. No puede ingresar cadenas de caracteres como: 'salir'""");
    }
}