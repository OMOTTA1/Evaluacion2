
package triqui;


import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Triqui {

    final static int Filas = 3;
    final static int Columnas = 3;
    final static char Jugador_1 = 'X';
    final static char Jugador_2 = 'O';
    final static char Espacio_Vacio = ' ';
    final static int Conteo = 3;
    // Modos de juego
    final static int Jugador_Jugador = 1;
    // Para leer del teclado
    final static Scanner sc = new Scanner(System.in);

    // Clona la matriz. Útil para las simulaciones que se hacen, así no se modifica el tablero original
    static char[][] clonarMatriz(char[][] tableroOriginal) {
        char[][] copia = new char[Filas][Columnas];
        for (int y = 0; y < Filas; y++) {
            for (int x = 0; x < Columnas; x++) {
                copia[y][x] = tableroOriginal[y][x];
            }
        }
        return copia;
    }

    // Establece el tablero en espacios vacíos
    static void limpiarTablero(char[][] tablero) {
        int y;
        for (y = 0; y < Filas; y++) {
            int x;
            for (x = 0; x < Columnas; x++) {
                tablero[y][x] = Espacio_Vacio;
            }
        }
    }

    // Imprime el tablero de juego
    static void imprimirTablero(char[][] tablero) {
        System.out.print("\n");
        int y;
        int x;
        // Imprimir encabezado
        System.out.print("| ");
        for (x = 0; x < Columnas; x++) {
            System.out.printf("|%d", x + 1);
        }
        System.out.print("|\n");
        for (y = 0; y < Filas; y++) {
            System.out.printf("|%d", y + 1);
            for (x = 0; x < Columnas; x++) {
                System.out.printf("|%c", tablero[y][x]);
            }
            System.out.print("|\n");
        }
    }

    // Indica si el tablero está vacío en las coordenadas indicadas
    static boolean coordenadasVacias(int y, int x, char[][] tablero) {
        return tablero[y][x] == Espacio_Vacio;
    }

    // Coloca la X o O en las coordenadas especificadas
    static void colocarPieza(int y, int x, char pieza, char[][] tablero) {
        if (y < 0 || y >= Filas) {
            System.out.print("Fila incorrecta");
            return;
        }

        if (x < 0 || x >= Columnas) {
            System.out.print("Columna incorrecta");
            return;
        }
        if (pieza != Jugador_2 && pieza != Jugador_1) {
            System.out.printf("La pieza debe ser %c o %c", Jugador_2, Jugador_1);
            return;
        }
        if (!coordenadasVacias(y, x, tablero)) {
            System.out.print("Coordenadas ya ocupadas");
            return;
        }
        tablero[y][x] = pieza;
    }

    /*
        Funciones de conteo. Simplemente cuentan cuántas piezas del mismo jugador están
        alineadas
     */
    static int contarHaciaArriba(int x, int y, char jugador, char[][] tablero) {
        int yInicio = (y - Conteo >= 0) ? y - Conteo + 1 : 0;
        int contador = 0;
        for (; yInicio <= y; yInicio++) {
            if (tablero[yInicio][x] == jugador) {
                contador++;
            } else {
                contador = 0;
            }
        }
        return contador;
    }

    static int contarHaciaDerecha(int x, int y, char jugador, char[][] tablero) {
        int xFin = (x + Conteo < Columnas) ? x + Conteo - 1 : Columnas - 1;
        int contador = 0;
        for (; x <= xFin; x++) {
            if (tablero[y][x] == jugador) {
                contador++;
            } else {
                contador = 0;
            }
        }
        return contador;
    }

    static int contarHaciaArribaDerecha(int x, int y, char jugador, char[][] tablero) {
        int xFin = (x + Conteo < Columnas) ? x + Conteo - 1 : Columnas - 1;
        int yInicio = (y - Conteo >= 0) ? y - Conteo + 1 : 0;
        int contador = 0;
        while (x <= xFin && yInicio <= y) {
            if (tablero[y][x] == jugador) {
                contador++;
            } else {
                contador = 0;
            }
            x++;
            y--;
        }
        return contador;
    }

    static int contarHaciaAbajoDerecha(int x, int y, char jugador, char[][] tablero) {
        int xFin = (x + Conteo < Columnas) ? x + Conteo - 1 : Columnas - 1;
        int yFin = (y + Conteo < Filas) ? y + Conteo - 1 : Filas - 1;
        int contador = 0;
        while (x <= xFin && y <= yFin) {
            if (tablero[y][x] == jugador) {
                contador++;
            } else {
                contador = 0;
            }
            x++;
            y++;
        }
        return contador;
    }

    // Indica si el jugador gana
    
    static boolean comprobarSiGana(char jugador, char[][] tablero) {
        int y;
        for (y = 0; y < Filas; y++) {
            int x;
            for (x = 0; x < Columnas; x++) {
                if (contarHaciaArriba(x, y, jugador, tablero) >= Conteo
                        || contarHaciaDerecha(x, y, jugador, tablero) >= Conteo
                        || contarHaciaArribaDerecha(x, y, jugador, tablero) >= Conteo
                        || contarHaciaAbajoDerecha(x, y, jugador, tablero) >= Conteo) {
                    return true;
                }
            }
        }
        // Terminamos de recorrer y no conectó
        return false;
    }

    // Devuelve el jugador contrario al que se le pasa. Es decir, le das un O y te devuelve el X
    static char oponenteDe(char jugador) {
        if (jugador == Jugador_2) {
            return Jugador_1;
        } else {
            return Jugador_2;
        }
    }

    // Debería llamarse después de verificar si alguien gana
    // Indica si hay un empate
    static boolean empate(char[][] tableroOriginal) {
        int y;
        for (y = 0; y < Filas; y++) {
            int x;
            for (x = 0; x < Columnas; x++) {
                // Si hay al menos un espacio vacío se dice que no hay empate
                if (tableroOriginal[y][x] == Espacio_Vacio) {
                    return false;
                }
            }
        }
        return true;
    }

    // Devuelve un número aleatorio en un rango, incluyendo los límites
    public static int aleatorioEnRango(int minimo, int maximo) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }

    // Devuelve coordenadas válidas
    static int[] obtenerCoordenadasAleatorias(char jugador, char[][] tableroOriginal) {
        int x, y;
        do {
            x = aleatorioEnRango(0, Columnas - 1);
            y = aleatorioEnRango(0, Filas - 1);
        } while (!coordenadasVacias(y, x, tableroOriginal));
        return new int[]{x, y};
    }

    // Devuelve las coordenadas en las que se puede ganar, o -1 y -1 si no se puede ganar
    static int[] coordenadasParaGanar(char jugador, char[][] tableroOriginal) {
        int y, x;
        for (y = 0; y < Filas; y++) {
            for (x = 0; x < Columnas; x++) {
                char[][] copiaTablero = clonarMatriz(tableroOriginal);
                if (coordenadasVacias(y, x, copiaTablero)) {
                    colocarPieza(y, x, jugador, copiaTablero);
                    if (comprobarSiGana(jugador, copiaTablero)) {
                        return new int[]{x, y};
                    }
                }
            }
        }
        return new int[]{-1, -1};
    }

    /*
        Esta función cuenta y te dice el mayor puntaje, pero no te dice en cuál X ni cuál Y.
    */
    static int contarSinSaberCoordenadas(char jugador, char[][] copiaTablero) {
        int conteoMayor = 0;
        int x, y;
        for (y = 0; y < Filas; y++) {
            for (x = 0; x < Columnas; x++) {
                // Colocamos y contamos el puntaje
                int conteoTemporal;
                conteoTemporal = contarHaciaArriba(x, y, jugador, copiaTablero);
                if (conteoTemporal > conteoMayor) {
                    conteoMayor = conteoTemporal;
                }
                conteoTemporal = contarHaciaArribaDerecha(x, y, jugador, copiaTablero);
                if (conteoTemporal > conteoMayor) {
                    conteoMayor = conteoTemporal;
                }

                conteoTemporal = contarHaciaDerecha(x, y, jugador, copiaTablero);
                if (conteoTemporal > conteoMayor) {
                    conteoMayor = conteoTemporal;
                }

                conteoTemporal = contarHaciaAbajoDerecha(x, y, jugador, copiaTablero);
                if (conteoTemporal > conteoMayor) {
                    conteoMayor = conteoTemporal;
                }
            }
        }
        return conteoMayor;
    }

    // Devuelve un jugador aleatorio
    static char jugadorAleatorio() {
        if (aleatorioEnRango(0, 1) == 0) {
            return Jugador_2;
        } else {
            return Jugador_1;
        }
    }
    
    // Loop principal del juego

    static void iniciarJuego(int modo) {
        if (modo != Jugador_Jugador) {
            System.out.print("Modo de juego no permitido");
            return;
        }

        // Para que salgan cosas aleatorias
        // Iniciar tablero de juego
        char[][] tablero = new char[Filas][Columnas];
        // Y limpiarlo
        limpiarTablero(tablero);
        // Elegir jugador que inicia al azar
        char jugadorActual = jugadorAleatorio();
        System.out.printf("El jugador que inicia es: %c\n", jugadorActual);
        int x = 0, y = 0;
        // Y allá vamos
        int[] coordenadas = new int[2];
        while (true) {
            imprimirTablero(tablero);
            if (modo == Jugador_Jugador) {
                System.out.printf("Jugador %c. Ingresa coordenadas (x,y) para colocar la pieza\n", jugadorActual);
                do {
                    x = solicitarNumeroValido("Ingresa las coordenadas X: ", 1, Columnas);
                    y = solicitarNumeroValido("Ingresa las coordenadas Y: ", 1, Filas);
                    if (!coordenadasVacias(y - 1, x - 1, tablero)) {
                        System.out.println("Coordenadas ocupadas. Intenta de nuevo");
                    }
                } while (!coordenadasVacias(y - 1, x - 1, tablero));
                // Al usuario se le solicitan números comenzando a contar en 1, pero en los arreglos comenzamos desde el 0
                // así que necesitamos restar uno en ambas variables
                x--;
                y--;
            }
            // Sin importar cuál modo haya sido, colocamos la pieza según las coordenadas elegidas

            colocarPieza(y, x, jugadorActual, tablero);
            // Puede que después de colocar la pieza el jugador gane o haya un empate, así que comprobamos
            if (comprobarSiGana(jugadorActual, tablero)) {
                imprimirTablero(tablero);
                System.out.printf("El jugador %c gana\n", jugadorActual);
                return;
            } else if (empate(tablero)) {
                imprimirTablero(tablero);
                System.out.println("Empate");
                return;
            }
            // Si no, es turno del otro jugador
            jugadorActual = oponenteDe(jugadorActual);
        }
    }

    public static int solicitarNumeroValido(String mensaje,int minimo, int maximo) {
        int numero;
        while (true) {
            System.out.print(mensaje);
            if (sc.hasNextInt()) {
                numero = sc.nextInt();
                if (numero >= minimo && numero <= maximo) {
                    return numero;
                } else {
                    System.out.println("Numero fuera de rango. Intente de nuevo");
                }
            } else {
                sc.next();
            }
        }
    }

    public static void main(String[] args) {
        int modo;
        String Nombre_1, Nombre_2;
        
        System.out.print("Escribe el nombre del jugador 1: ");
        Nombre_1 = sc.next();
        
        System.out.print("Escribe el nombre del jugador 2: ");
        Nombre_2 = sc.next();
        
        String menu = Nombre_1+" sera el jugador O contra jugador X que es "+Nombre_2+"\nEscribe 1 para empezar: ";
        modo = solicitarNumeroValido(menu, 1, 1);
        iniciarJuego(modo);
        sc.close();
    }
}