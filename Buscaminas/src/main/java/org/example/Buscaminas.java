package org.example;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * Esta clase contiene los métodos utilizados para poder realizar el ejercicio 315 de Acepta el reto
 * @author david
 * @version 1.8
 */
public class Buscaminas {

    public static String[][] matrizReal;


    static int numFilas = 0;
    static int numColumnas = 0;
    static int celdasPorDescubrir = 0;
    static String[][] matrizVisible;
    static Scanner read = new Scanner(System.in);
    static Random aleatorio = new Random();


    static void main(String[] args) {

        numFilas = pedirEntero("Introduce el número de filas: ");
        numColumnas = pedirEntero("Introduce el número de columnas: ");

        if (numFilas > 50 || numColumnas > 50) {
            System.out.println("No puede haber más de 50 filas ni 50 columnas");
            return;
        }

        matrizReal = new String[numFilas][numColumnas];
        rellenarMatriz();

        imprimirMatriz(matrizReal);

        matrizVisible = crearMatrizVisible();

        celdasPorDescubrir = pedirEntero("Introduce el número de celdas a descubrir: ");
        read.nextLine();
        pedirCeldas(celdasPorDescubrir);

    }
    /**
     * Pide al usuario un número entero por consola y comprueba que el número
     * introducido sea válido.
     * @param texto El mensaje que se muestra al usuario para pedir el número.
     * @return El número entero introducido por el usuario.
     */
    public static int pedirEntero(String texto) {
        Scanner read = new Scanner(System.in);

        int num = 0;
        boolean seguir = true;


        do {
            System.out.print(texto);
            try {
                num = read.nextInt();
                seguir = false;
            } catch (InputMismatchException e) {
                System.out.println("Error. Debes introducir un número entero");
                read.nextLine();
            }
        } while (seguir);

        return num;
    }


    /**
     * Este método sirve para rellenar la matriz del buscaminas (todas las celdas)
     * y comprobar que lo que introduce el usuario es correcto
     */
    public static void rellenarMatriz () {

        int filaActual = 0;
        externo:
        for (int i = 0; i < matrizReal.length; i++) {
            System.out.println("Introduce los " + numColumnas + " carácteres de la fila " + filaActual);
            String valoresFilaActual[] = read.next().split("");

            if (valoresFilaActual.length != numColumnas) {
                i--;
                System.out.println("La fila debe de tener " + numColumnas + " carácteres.");
                continue;
            }
            for (int j = 0; j < matrizReal[0].length; j++) {
                if (!valoresFilaActual[j].equals("*") && !valoresFilaActual[j].equals("-")) {
                    System.out.println("La fila debe de tener solo '-' o '*'. Cerrando el programa.");
                    i--;
                    continue externo;
                }
                matrizReal[i][j] = valoresFilaActual[j];
            }
            filaActual++;
        }


    }

    /**
     *
     * Este método sirve para mostrar en consola la matriz que introduzca el usuario
     * @param matriz La matriz que el usuario desea que se muestre por consola
     *
     */
    public static void imprimirMatriz (String[][] matriz) {
        for(String fila[] : matriz) {
            for (String valor : fila) {
                System.out.print(valor);
            }
            System.out.println();
        }
    }

    /**
     *
     * Esta función sirve para crear la matriz visible dentro del mismo método, y devolver la matriz resultante,
     * que debe de ser con todas las celdas teniendo un valor de "X". La matriz se crea a partir del numFilas y
     * numColumnas que ha introducido el usuario anteriormente.
     *
     * @return Devuelve la matriz visible (osea, la matriz que empieza siendo todo "X")
     */
    public static String[][] crearMatrizVisible() {
        String[][] matrizVisible = new String[numFilas][numColumnas];

        for (int i = 0; i < matrizVisible.length; i++) {
            for (int j = 0; j < matrizVisible[0].length; j++) {
                matrizVisible[i][j] = "X";
            }
        }

        return matrizVisible;
    }


    /**
     * Este método se utiliza para pedir al usuario las celdas que quiere descubrir hasta completar
     * la cantidad indicada por celdasPorDescubrir. Por cada celda seleccionada, se guardan los valores
     * para utilizarlos posteriormente en el método asignarValorCelda. Al final, se imprime la matriz
     * resultante y, si la celda contiene una bomba (usando el booleano que devuelve el método
     * comprobarGameOver), se muestra "GAME OVER".
     *
     * @param celdasPorDescubrir La cantidad de celdas que el usuario quiere descubrir.
     */

    public static void pedirCeldas (int celdasPorDescubrir) {

        for (int i = 0; i < celdasPorDescubrir; i++) {
            System.out.print("Qué celda quieres descubrir?: ");
            String[] celdaSeleccionada = read.nextLine().split(" ");
            int fila = Integer.parseInt(celdaSeleccionada[0])-1;
            int columna = Integer.parseInt(celdaSeleccionada[1])-1;

            if (fila < 0 || fila >= numFilas || columna < 0 || columna >= numColumnas) {
                System.out.println("Error. La celda seleccionada está fuera del rango.");
                i--;
                continue;
            } else if (!matrizVisible[fila][columna].equals("X")) {
                System.out.println("La celda seleccionada ya ha sido vista. Selecciona otra");
                i--;
                continue;
            }

            asignarValorCelda(fila, columna);

            imprimirMatriz(matrizVisible);

            if (!comprobarGameOver(celdaSeleccionada)) {
                System.out.println("GAME OVER");
                break;
            }

        }
    }


    /**
     * Este método comprueba que la celda que ha decidido buscar el usuario
     * contenga una bomba o no, devolviendo el resultado en forma de booleano.
     * @param celdaSeleccionada La celda que ha escogido ver el usuario
     * @return Devuelve o false (Game Over), o true (no Game Over)
     */
    public static boolean comprobarGameOver  (String[] celdaSeleccionada) {
        int fila = Integer.parseInt(celdaSeleccionada[0])-1;
        int columna = Integer.parseInt(celdaSeleccionada[1])-1;

        if (matrizReal[fila][columna].equals("*"))  {
            return false;
        }

        return true;
    }

    /**
     * Este método se utiliza para asignar el valor que corresponde a la celda que ha seleccionado el usuario,
     * comprobando cuántas bombas hay alrededor. Si la celda tiene bombas a su alrededor, se coloca en la celda
     * el número de bombas. Si no, se marca con "-" y se van descubriendo las celdas que aún no están descubiertas,
     * metiendo su posición (fila y columna) en un array para fila y otro para columna. Teniendo estos arrays, podemos
     * recorrer todas las celdas no descubiertas hasta haber conseguido mostrar todas las celdas que deban de ser
     * descubiertas alrededor de la celda inicial seleccionada.
     *
     * @param fila La fila de la celda que el usuario quiere descubrir.
     * @param columna La columna de la celda que el usuario quiere descubrir.
     */

    public static void asignarValorCelda(int fila, int columna) {

        int maximo = numFilas*numColumnas; //Indicamos el máximo de celdas que se van a poder introducir a los arrays de posiciones


        //Guardamos dos array, guardan las posiciones (fila-columna) de las celdas que aún no han sido procesadas.
        int[] filasPendientes = new int[maximo];
        int[] columnasPendientes = new int[maximo];

        //Es el número de celdas que hay por revisar, empieza siendo 1, porque es la celda seleccionada.
        int celdasPendientes = 1;

        //Añadimos la posición de nuestra celda seleccionada a la lista de celdas pendientes.
        filasPendientes[0] = fila;
        columnasPendientes[0] = columna;


        //Condicional que comprueba que la celda seleccionada sea una bomba, en ese caso, su valor será "*" y terminará la función
        if (matrizReal[fila][columna].equals("*")) matrizVisible[fila][columna] = "*";

        //De lo contrario...
        else {

            //Utilizo este for para recorrer todas las celdas pendientes de procesar, empezando por la 0 (La celda seleccionada) y acabando cuando no queden más celdas
            for(int indice = 0; indice < celdasPendientes; indice++ ) {

                //Variables para tener la celda que se procesará en este momento
                int filaActual = filasPendientes[indice];
                int columnaActual = columnasPendientes[indice];

                //Inicializamos el contador de bombas, que servirá para saber cuantas bombas tiene la celda al rededor (si es que tiene)
                int contadorBombas = 0;

                //Recorremos las 8 celdas que hay al rededor de nuestra celda actual
                for (int i = filaActual -1; i <= filaActual +1; i++) {
                    for (int j = columnaActual -1; j <= columnaActual +1 ; j++) {

                        if (i < 0 || i >= numFilas || j < 0 || j >= numColumnas) continue; //Si la posición de la celda no está en el tablero, ignoramos el caso y pasamos al siguiente

                        if (matrizReal[i][j].equals("*")) contadorBombas++; //Si la celda que estamos revisando contiene bomba, el contador de bombas se sumará en uno
                    }
                }

                if (contadorBombas != 0) { //Si el contador de bombas no es 0 (Por lo que, hay por lo menos una bomba)
                    matrizVisible[filaActual][columnaActual] = Integer.toString(contadorBombas); //Nuestra celda actual, tendrá como valor contadorBombas
                } else { //De lo contrario...

                    matrizVisible[filaActual][columnaActual] = "-"; //La celda actual será un "-", porque no tiene bombas al rededor.

                    //Volvemos a recorrer sus 8 celdas vecinas, pero esta vez para añadirlas a los arrays de pendientes
                    for (int i = filaActual -1; i <= filaActual +1; i++) {
                        for (int j = columnaActual -1; j <= columnaActual +1 ; j++) {

                            if (i < 0 || i >= numFilas || j < 0 || j >= numColumnas) continue; //Volvemos a comprobar que la celda no esté fuera del rango


                            if (matrizVisible[i][j].equals("X")) { //En caso de que la celda que estamos revisando sea "X", añadimos su posición a los arrays (Porque significa que aún no la hemos procesado)
                                filasPendientes[celdasPendientes] = i;
                                columnasPendientes[celdasPendientes] = j;
                                celdasPendientes++;
                            }

                        }
                    }


                }

            }
        }

    }



}
