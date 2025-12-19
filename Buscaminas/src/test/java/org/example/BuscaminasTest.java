package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;


class BuscaminasTest {

    @Test
    void pedirEntero() {

        String num = "42"; //Creamos una variable string con un número, el 42
        System.setIn(new ByteArrayInputStream(num.getBytes()));

        //Llamamos al método que queremos comprobar, asignandole a resultado el valor que devuelve
        int resultado = Buscaminas.pedirEntero("Introduce un número: ");

        //Comprobamos que resultado es igual a 42.
        assertEquals(42, resultado);
    }


    @Test
    void crearMatrizVisible() {

        //Asignamos el tablero con 2 filas y 3 columnas
        Buscaminas.numFilas = 2;
        Buscaminas.numColumnas = 3;

        //Creamos la matriz visible (2x3 y todas las celdas son "X")
        String[][] matriz = Buscaminas.crearMatrizVisible();

        //Utilizamos assertAll para comprobar todos los test aunque alguno falle
        assertAll(
                () -> assertEquals(2, matriz.length), //Comprobamos si la matriz tiene 2 filas
                () -> assertEquals(3, matriz[0].length), //Comprobamos si la matriz tiene 3 columnas
                //Comprobamos que en las siguientes celdas, el valor sea "X".
                () -> assertEquals("X", matriz[0][0]),
                () -> assertEquals("X", matriz[0][1]),
                () -> assertEquals("X", matriz[1][2])
        );
    }

    @Test
    void comprobarGameOver() {

        //Asignamos el tablero con 2 filas y 2 columnas

        Buscaminas.numFilas = 2;
        Buscaminas.numColumnas = 2;

        //Creamos un tablero con una bomba en la celda 1-1
        Buscaminas.matrizReal = new String[][]{
                {"-", "*"},
                {"-", "-"}
        };
        //Creamos 2 posiciones, la celda1, que contiene una bomba, y la celda 2, que no contiene una bomba
        String[] celda1 = {"1", "1"};
        String[] celda2 = {"1", "2"};
        assertAll(
                () -> assertTrue(Buscaminas.comprobarGameOver(celda1)), //Comprobamos que la primera celda contiene una bomba
                () -> assertFalse(Buscaminas.comprobarGameOver(celda2)) //Comprobamos que la segunda celda no es una bomba
        );
    }
}
