package view;

public class Mensajes {

    public void exito(String mensaje) {
        System.out.println("""
                           ******************************
                           -------- Proceso Exitoso! --------
                           %s
                           ----------------------------------
                           """.formatted(mensaje));
    }

    public void informativo(String mensaje) {
        System.out.println("---------------------------");
        System.out.println(mensaje);
        System.out.println("---------------------------");
    }

    public void error(String mensaje) {
        System.out.println("""
                           ******************************
                           -------- Error! --------
                           %s
                           ------------------------
                           """.formatted(mensaje));
    }
}
