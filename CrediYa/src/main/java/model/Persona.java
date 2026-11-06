package model;

/**
 * Persona  (se completa en la Fase 4)
 *
 * Clase PADRE (abstracta). Datos comunes: id, nombre, documento, correo, clave.
 * Obliga a sus hijas a escribir tipoPersona() y resumen() (polimorfismo).
 *
 * Usa:      Encriptador (para verificar la clave)
 * La usan:  Empleado y Cliente (heredan de ella), Sesion (guarda la persona conectada),
 *            InformeController (directorio general)
 */
public abstract class Persona {

}
