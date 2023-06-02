public class Resultado {

        public Resultado() {

        }

        public void ImprimirTblaPlRes(TblaReservadas[] aReservadas) {
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%70s", "Tabla de Plabras Reservadas");
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%25s %25s %25s %10s %10s", "Token Palabra Reservada", "Lexema", "Linea", "Inicio",
                                "Final");
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");

                for (int x = 0; x < aReservadas.length; x++) {
                        var reserv = aReservadas[x];

                        System.out.format("%25s %25s %25s %10s %10s", reserv.getReservada(), reserv.getLexema(),
                                        reserv.getLinea(),
                                        reserv.getPosInicioReserv(), reserv.getPosFinalReserv());
                        System.out.println();

                }
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");

        }// Fin del metodo ImprimirTblaPlRes

        public void ImprimirTblaSimb(TblaSimbolos[] aSimbolos) {
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%70s", "Tabla de Simbolos");
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%25s %25s %25s %10s %10s", "Simbolo", "Lexema", "Linea", "Inicio", "Final");
                System.out.println();
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");

                

                for (int x = 0; x < aSimbolos.length; x++) {
                        var aSimbolo = aSimbolos[x];

                        System.out.format("%25s %25s %25s %10s %10s", aSimbolo.getSimbolo(), aSimbolo.getLexema(),
                                        aSimbolo.getLinea(),
                                        aSimbolo.getPosInicioSimbolo(), aSimbolo.getPosFinalSimbolo());
                        System.out.println();

                }
                System.out.println(
                                "---------------------------------------------------------------------------------------------------------------------------------");

        }

}
