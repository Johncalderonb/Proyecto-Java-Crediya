package view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Validaciones {

    private static final Scanner sc = new Scanner(System.in);

    public String validarTexto(String mensaje) {
        String dato;
        boolean validacion;
        do {
            validacion = true;
            System.out.println(mensaje);
            dato = sc.nextLine().strip();
            if (dato.isBlank()) {
                System.out.println("Error, no se aceptan espacios en blanco");
                validacion = false;
                continue;
            }
            for (int i = 0; i < dato.length(); i++) {
                if (!Character.isLetter(dato.charAt(i)) && dato.charAt(i) != ' ') {
                    System.out.println("Error, solo se aceptan letras o espacios");
                    validacion = false;
                    break;
                }
            }
        } while (!validacion);
        return dato;
    }

    public String validarNumerico(String mensaje, int minimo, int maximo) {
        String dato;
        boolean validacion;
        do {
            validacion = true;
            System.out.println(mensaje);
            dato = sc.nextLine().strip();
            if (dato.length() < minimo || dato.length() > maximo) {
                System.out.println("Error, debe tener entre " + minimo + " y " + maximo + " digitos");
                validacion = false;
                continue;
            }
            for (int i = 0; i < dato.length(); i++) {
                if (!Character.isDigit(dato.charAt(i))) {
                    System.out.println("Error, solo se aceptan numeros");
                    validacion = false;
                    break;
                }
            }
        } while (!validacion);
        return dato;
    }

    public String validarClave(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            String dato = sc.nextLine().strip();
            if (dato.length() >= 4 && dato.length() <= 20 && !dato.contains(" ")) {
                return dato;
            }
            System.out.println("Error, la clave debe tener entre 4 y 20 caracteres y sin espacios");
        }
    }

    public String validarCorreo(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            String dato = sc.nextLine().strip().toLowerCase();
            int arroba = dato.indexOf('@');
            boolean unaArroba = arroba > 0 && arroba == dato.lastIndexOf('@');
            boolean puntoDespues = dato.lastIndexOf('.') > arroba + 1 && !dato.endsWith(".");
            if (unaArroba && puntoDespues && !dato.contains(" ")) {
                return dato;
            }
            System.out.println("Error, correo invalido. Ejemplo: usuario@correo.com");
        }
    }

    public int validarEntero(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            String dato = sc.nextLine().strip();
            try {
                return Integer.parseInt(dato);
            } catch (NumberFormatException e) {
                System.out.println("Error, se espera un valor entero");
            }
        }
    }

    public long validarEnteroGrande(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            String dato = sc.nextLine().strip();
            try {
                return Long.parseLong(dato);
            } catch (NumberFormatException e) {
                System.out.println("Error, se espera un valor entero");
            }
        }
    }

    public int validarRango(String mensaje, int minimo, int maximo) {
        int dato = validarEntero(mensaje);
        while (dato < minimo || dato > maximo) {
            System.out.println("Error, el valor debe estar entre " + minimo + " y " + maximo);
            dato = validarEntero(mensaje);
        }
        return dato;
    }

    public double validarDecimal(String mensaje) {
        while (true) {
            System.out.println(mensaje);
            String dato = sc.nextLine().strip().replace(",", ".");
            try {
                return Double.parseDouble(dato);
            } catch (NumberFormatException e) {
                System.out.println("Error, se espera un valor numerico");
            }
        }
    }

    public double validarDecimalRango(String mensaje, double minimo, double maximo) {
        double dato = validarDecimal(mensaje);
        while (dato < minimo || dato > maximo) {
            System.out.println("Error, el valor debe estar entre " + String.format("%,.2f", minimo)
                    + " y " + String.format("%,.2f", maximo));
            dato = validarDecimal(mensaje);
        }
        return dato;
    }

    public LocalDate validarFecha(String mensaje) {
        while (true) {
            System.out.println(mensaje + " (formato AAAA-MM-DD, Enter = hoy)");
            String dato = sc.nextLine().strip();
            if (dato.isEmpty()) {
                return LocalDate.now();
            }
            try {
                LocalDate fecha = LocalDate.parse(dato);
                if (fecha.isAfter(LocalDate.now())) {
                    System.out.println("Error, la fecha no puede ser futura");
                    continue;
                }
                return fecha;
            } catch (DateTimeParseException e) {
                System.out.println("Error, fecha invalida. Ejemplo: 2026-03-15");
            }
        }
    }

    public boolean confirmar(String mensaje) {
        return validarRango(mensaje + "\n1- Si\n2- No", 1, 2) == 1;
    }
}
