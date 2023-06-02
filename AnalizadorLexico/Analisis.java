
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Analisis {

    String sCodigoFuente = "";
    String sCodigoFuenteErrores = "";
    TblaTokens[] TaToken = new TblaTokens[0];
    TblaSimbolos[] TaSimbolos = new TblaSimbolos[0];
    TblaReservadas[] TaReserv = new TblaReservadas[0];
    TblaErrores[] TaErrors = new TblaErrores[0];

    Resultado oResultado = new Resultado();
    int nPosLectura = 0;
    int nLinea = 0;
    String tipoGRM = "";
    Set<String> simbolosAgregados = new HashSet<>();
    Set<String> PRAgregados = new HashSet<>();

    public Analisis(String sCodigoFuente) {
        this.sCodigoFuente = sCodigoFuente;
        this.sCodigoFuenteErrores = sCodigoFuente;
    }

    public void Generar() {

        // Establecemos una cadena de coincidencias Esto es una expresion regular
        String regexCaracteres = "(Linea|Circulo|Triangulo|Cuadrado|Rectangulo)\\b|"
        + "([-])|"
        + "(\\()|"
        + "(\\))|"
        + "(\\,)|"
        + "([0-9]+)|"
        + "(\\s+)|"
        + "(^(\\s)*)";

        String regexFigura = "(((\\(\\d{1,2},\\d{1,2}\\)){2,4})+-+\\([1-3]\\)+-+\\((25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{1,2}),(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{1,2}),(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{1,2})\\))|"
                + "(((\s)*))";

        String regexCoordenada = "\\((\\d,[0-9])\\)"; 

        String regexGrosor = "\\(([1-3])\\)";

        String regexColor = "\\((\\d{1,3},\\d{1,3},\\d{1,3})\\)";


        // Define un patron de busquedas dentro de nuestra cadena de coincidencias
        Pattern pPatronCaracter = Pattern.compile(regexCaracteres);
        Pattern pPatronFigura = Pattern.compile(regexFigura);
        Pattern pPatronCoordenada = Pattern.compile(regexCoordenada);
        Pattern pPatronColor = Pattern.compile(regexColor);
        Pattern pPatronGrosor = Pattern.compile(regexGrosor);
        // ralizara la búsqueda de nuestra coincidencias
        Matcher mMatcherCaracter = pPatronCaracter.matcher(sCodigoFuente);
        Matcher mMatcherFigura = pPatronFigura.matcher(sCodigoFuente);
        Matcher mMatcherCoordenada = pPatronCoordenada.matcher(sCodigoFuente);
        Matcher mMatcherGrosor = pPatronGrosor.matcher(sCodigoFuente);
        Matcher mMatcherColor = pPatronColor.matcher(sCodigoFuente);


        while (mMatcherCoordenada.find()) {
            String tokenCoordenadas = mMatcherCoordenada.group(1);

            nLinea = ObtenerLinea(sCodigoFuente, mMatcherCoordenada.start());

            if(tokenCoordenadas != null){
                nPosLectura += tokenCoordenadas.length();
                int nPosInicioLexema = nPosLectura - tokenCoordenadas.length();


                AgregarTablaSimbolos("Coordenadas", tokenCoordenadas, nLinea, nPosInicioLexema,
                nPosLectura);
            }
        }


        while(mMatcherFigura.find()){

            
            String tokenFiguras = mMatcherFigura.group(1);
            String tokenEspacios = mMatcherFigura.group(2);

            nLinea = ObtenerLinea(sCodigoFuente, mMatcherFigura.start());

            if (tokenEspacios != null) {
                if (tokenEspacios.length() > 0) {
                    nPosLectura += tokenEspacios.length();
                }
            }

            if (tokenFiguras != null) {
                nPosLectura += tokenFiguras.length();
                int nPosInicioLexema = nPosLectura - tokenFiguras.length();

                AgregarTablaSimbolos("Figura Carac:", tokenFiguras, nLinea, nPosInicioLexema,
                        nPosLectura);
            }

        }


        //regexGrosor
        while(mMatcherGrosor.find()){

            String tokenGrosor = mMatcherGrosor.group(1);

            nLinea = ObtenerLinea(sCodigoFuente, mMatcherGrosor.start());


            if (tokenGrosor != null) {
                nPosLectura += tokenGrosor.length();
                int nPosInicioLexema = nPosLectura - tokenGrosor.length();

                AgregarTablaSimbolos("Grosor:", tokenGrosor, nLinea, nPosInicioLexema,
                        nPosLectura);
            }
        }

        while(mMatcherColor.find()){

            String tokenColor = mMatcherColor.group(1);

            nLinea = ObtenerLinea(sCodigoFuente, mMatcherColor.start());


            // Obtener los componentes de color
        String[] colorComponents = tokenColor.split(",");
        int red = Integer.parseInt(colorComponents[0]);
        int green = Integer.parseInt(colorComponents[1]);
        int blue = Integer.parseInt(colorComponents[2]);

        // Validar los rangos de los componentes de color
        if (red >= 0 && red <= 255 && green >= 0 && green <= 255 && blue >= 0 && blue <= 255) {
        // Los componentes de color están dentro del rango válido
        // Realiza las acciones necesarias
        int nPosInicioLexema = mMatcherColor.start();
        int nPosLectura = mMatcherColor.end();

        AgregarTablaSimbolos("Color:", tokenColor, nLinea, nPosInicioLexema, nPosLectura);
    } else {
                                this.nPosLectura += tokenColor.length();
                                int nPosInicioLexema = this.nPosLectura - tokenColor.length();
                                System.out.println();
                                System.out.format("%10s %10s %10s %10s",
                                        " \033[31mERROR léxico:  \033[0m" + tokenColor, " Linea " + nLinea,
                                        " Inicia " + nPosInicioLexema, " Termina " + nPosLectura);
                                System.out.println();
    }
        }

        // Buscamos las coincidencias con el ciclo While
        while (mMatcherCaracter.find()) {

            String tokenPalabrasrReservadas = mMatcherCaracter.group(1);
            String tokenOAritmeticas = mMatcherCaracter.group(2);
            String tokenParentesisIzq = mMatcherCaracter.group(3);
            String tokenParentesisDer = mMatcherCaracter.group(4);
            String tokenComa = mMatcherCaracter.group(5);
            String tokenDigito = mMatcherCaracter.group(6);
            String tokenEspacios = mMatcherCaracter.group(7);

            

            int nPosInicioLexema = 0;
            nLinea = ObtenerLinea(sCodigoFuente, mMatcherCaracter.start());


            //Palabras Reservadas
            if (tokenPalabrasrReservadas != null) {
                nPosLectura += tokenPalabrasrReservadas.length();
                nPosInicioLexema = nPosLectura - tokenPalabrasrReservadas.length();
                AgregarTablaToken("Palabras reservadas:", tokenPalabrasrReservadas, nLinea, nPosInicioLexema,
                        nPosLectura);
            if (!PRAgregados.contains(tokenPalabrasrReservadas))
                AgregarTablaReservadas("Palabra Reservada", tokenPalabrasrReservadas, nLinea, nPosInicioLexema, nPosLectura);
                PRAgregados.add(tokenPalabrasrReservadas);
            }

            //OAritmeticas
            if(tokenOAritmeticas != null){
                nPosLectura += tokenOAritmeticas.length();
                nPosInicioLexema = nPosLectura - tokenOAritmeticas.length();
                AgregarTablaToken("Operador/separador", tokenOAritmeticas, nLinea, nPosInicioLexema,
                nPosLectura);
                if(!simbolosAgregados.contains(tokenOAritmeticas)){
                AgregarTablaSimbolos("Operador/separador: ", tokenOAritmeticas, nLinea, nPosInicioLexema,
                nPosLectura);
                simbolosAgregados.add(tokenOAritmeticas);
                }
            }

            //ParentesisIzquierdo
            if (tokenParentesisIzq != null) {
                nPosLectura += tokenParentesisIzq.length();
                nPosInicioLexema = nPosLectura - tokenParentesisIzq.length();
                AgregarTablaToken("Delimitador:", tokenParentesisIzq, nLinea, nPosInicioLexema, nPosLectura);
                if(!simbolosAgregados.contains(tokenParentesisIzq)){
                AgregarTablaSimbolos("Delimitador: ", tokenParentesisIzq, nLinea, nPosInicioLexema,
                nPosLectura);
                simbolosAgregados.add(tokenParentesisIzq);
            }
            }
            
            //ParentesisDerecho
            if(tokenParentesisDer != null){
                nPosLectura += tokenParentesisDer.length();
                nPosInicioLexema = nPosLectura - tokenParentesisDer.length();
                AgregarTablaToken("Delimitador:", tokenParentesisDer, nLinea, nPosInicioLexema, nPosLectura);
                if(!simbolosAgregados.contains(tokenParentesisDer)){
                AgregarTablaSimbolos("DER Delimitador: ", tokenParentesisDer, nLinea, nPosInicioLexema, nPosLectura);
                simbolosAgregados.add(tokenParentesisDer);
                }
            }

            //Coma
            if(tokenComa != null){
                nPosLectura += tokenComa.length();
                nPosInicioLexema = nPosLectura - tokenComa.length();
                AgregarTablaToken("Delimitador:", tokenComa, nLinea, nPosInicioLexema, nPosLectura);
                if(!simbolosAgregados.contains(tokenComa)){
                AgregarTablaSimbolos("Delimitador: ", tokenComa, nLinea, nPosInicioLexema, nPosLectura);
                simbolosAgregados.add(tokenComa);
            }
            }
            

        //Digito
     if (tokenDigito != null) {
        nPosLectura += tokenDigito.length();
        nPosInicioLexema = nPosLectura - tokenDigito.length();
        AgregarTablaToken("Digito:", tokenDigito, nLinea, nPosInicioLexema, nPosLectura);
        if (!simbolosAgregados.contains(tokenDigito)) {
            AgregarTablaSimbolos("Digito: ", tokenDigito, nLinea, nPosInicioLexema, nPosLectura);
            simbolosAgregados.add(tokenDigito);
    }
}
           //Espacios
            if (tokenEspacios != null) {
                nPosLectura += tokenEspacios.length();
                nPosInicioLexema = nPosLectura - tokenEspacios.length();
            }
            
            if (tokenPalabrasrReservadas == null &&
                tokenOAritmeticas == null &&
                tokenParentesisIzq == null &&
                tokenParentesisDer == null &&
                tokenComa == null &&
                tokenDigito == null &&
                tokenEspacios == null 
                ) {

                String tokenDesconocido = sCodigoFuente.substring(nPosLectura, mMatcherCaracter.end());
                nPosLectura += tokenDesconocido.length();
                nPosInicioLexema = nPosLectura - tokenDesconocido.length();
                AgregarTablaErrores("Símbolo no reconocido:", tokenDesconocido, nLinea, nPosInicioLexema, nPosLectura);
}

        }

                // Codigo fuente a chart para leer parte por parte
                var cLetras = this.sCodigoFuenteErrores.toCharArray();
                String sPalabra = "";
                Pattern pPatronError = Pattern.compile(
                        "(((\\(\\d{1,2},\\d{1,2}\\)){2,4})+-+\\([1-3]\\)+-+\\((25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{1,2}),(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{1,2}),(25[0-5]|2[0-4]\\d|1\\d{1,2}|\\d{1,2})\\))");
                this.nPosLectura = 0;
                // Recorre palabra por palabra encontrada
                for (int y = 0; y < cLetras.length; y++) {
        
                    nLinea = ObtenerLinea(sCodigoFuenteErrores, y);
                    sPalabra += cLetras[y];
                    this.nPosLectura++;
        
                    if (sPalabra.split("\\s").length > 0) {
                        this.nPosLectura -= 1;
                        sPalabra = sPalabra.trim();
        
                        char cLeta = cLetras[y];
                        var x = ((cLeta + "").replace("", " ").trim());
        
                        // System.out.println(">" + sPalabra);
        
                        if (sPalabra.equals("Rectangulo")) {
                            sPalabra = sPalabra.replaceAll("Rectangulo", "");
                            nPosLectura += "Rectangulo".length();
                        }
        
                        if (sPalabra.equals("Triangulo")) {
                            sPalabra = sPalabra.replaceAll("Triangulo", "");
                            nPosLectura += "Triangulo".length();
                        }
        
                        if (sPalabra.equals("Cuadrado")) {
                            sPalabra = sPalabra.replaceAll("Cuadrado", "");
                            nPosLectura += "Cuadrado".length();
                        }
                        if (sPalabra.equals("Circulo")) {
                            sPalabra = sPalabra.replaceAll("Circulo", "");
                            nPosLectura += "Circulo".length();
                        }
                        if (sPalabra.equals("Linea")) {
                            sPalabra = sPalabra.replaceAll("Linea", "");
                            nPosLectura += "Linea".length();
                        }
                        // System.out.println(">" + nPosLectura);
        
                        if (x.length() == 0) {
                            Matcher mMatcherError = pPatronError.matcher(sPalabra);
        
                            if (mMatcherError.find()) {
                                sPalabra = "";
                            } else {
                                this.nPosLectura += sPalabra.length();
                                int nPosInicioLexema = this.nPosLectura - sPalabra.length();
                                System.out.println();
                                System.out.format("%10s %10s %10s %10s",
                                        " \033[31mERROR léxico:  \033[0m" + sPalabra, " Linea " + nLinea,
                                        " Inicia " + nPosInicioLexema, " Termina " + nPosLectura);
                                System.out.println();
                                sPalabra = "";
                            }
                        }
                    }
                }

        // oResultado.ImprimirTblaTokens(TaToken);
        oResultado.ImprimirTblaPlRes(TaReserv);
        oResultado.ImprimirTblaSimb(TaSimbolos);
        // oResultado.ImprimirTblaError(TaErrors);


    }

    /**
     * Metodo que agrega los simbolos a la tabla de tokens
     * @param sToken
     * @param sLexema
     * @param nLinea
     * @param nPosInicioLexema
     * @param nPosFinalLexema
     */
    public void AgregarTablaToken(String sToken, String sLexema, int nLinea, int nPosInicioLexema, int nPosFinalLexema) {

        TblaTokens[] aSimboloNuevo = this.TaToken;
        aSimboloNuevo = new TblaTokens[this.TaToken.length + 1];
        System.arraycopy(this.TaToken, 0, aSimboloNuevo, 0, this.TaToken.length);

        TblaTokens oSimbolo = new TblaTokens();
        oSimbolo.Token = sToken;
        oSimbolo.Lexema = sLexema;
        oSimbolo.Linea = nLinea;
        oSimbolo.PosInicioLexema = nPosInicioLexema;
        oSimbolo.PosFinalLexema = nPosFinalLexema;

        aSimboloNuevo[aSimboloNuevo.length - 1] = oSimbolo;

        this.TaToken = aSimboloNuevo;

    }//Fin del metodo AgregarTablaToken


    /**
     * Metodo que agrega los simbolos a la tabla de Errores
     * @param sError
     * @param sLexema
     * @param sLinea
     * @param sPosInicioLexema
     * @param sPosFinalLexema
     */
    public void AgregarTablaErrores( String sError, String sLexema, int sLinea, int sPosInicioLexema, int sPosFinalLexema){

        TblaErrores[] aErrorNuevo = this.TaErrors;
        aErrorNuevo = new TblaErrores[this.TaErrors.length + 1];
        System.arraycopy(this.TaErrors, 0, aErrorNuevo, 0, this.TaErrors.length);

        TblaErrores oError = new TblaErrores();
        oError.setError(sError);
        oError.setLexema(sLexema);
        oError.setLinea(sLinea);
        oError.setInicioPos(sPosInicioLexema);
        oError.setFinalPos(sPosFinalLexema);

        aErrorNuevo[aErrorNuevo.length - 1] = oError;

        this.TaErrors = aErrorNuevo;
    }//Fin del metodo AgregarTablaErrores

    /**
     * Metodo que agrega los simbolos a la tabla Palabras Reservadas
     * @param sReservada
     * @param sLexema
     * @param sLinea
     * @param sPosInicioLexema
     * @param sPosFinalLexema
     */
    public void AgregarTablaReservadas( String sReservada, String sLexema, int sLinea, int sPosInicioLexema, int sPosFinalLexema){

               //Se verifica si la Palabra reservada no haya sido agregada con anterioridad


        TblaReservadas[] aReservNuevo = this.TaReserv;
        aReservNuevo = new TblaReservadas[this.TaReserv.length + 1];
        System.arraycopy(this.TaReserv, 0, aReservNuevo, 0, this.TaReserv.length);

        TblaReservadas oReserv = new TblaReservadas();
        oReserv.setReservada(sReservada);
        oReserv.setLexema(sLexema);
        oReserv.setLinea(sLinea);
        oReserv.setPosInicioReserv(sPosInicioLexema);
        oReserv.setPosFinalReserv(sPosFinalLexema);

        aReservNuevo[aReservNuevo.length - 1] = oReserv;

        this.TaReserv = aReservNuevo;
    }//Fin del metodo AgregarTablaReservadas

    /**
     * Metodo para agregar un simbolo a la Tabla de simbolos
     * @param sSimbolo
     * @param sLexema
     * @param sLinea
     * @param sPosInicioLexema
     * @param sPosFinalLexema
     */
    public void AgregarTablaSimbolos( String sSimbolo, String sLexema, int sLinea, int sPosInicioLexema, int sPosFinalLexema){

        TblaSimbolos[] aSimboloNuevo = this.TaSimbolos;
        aSimboloNuevo = new TblaSimbolos[this.TaSimbolos.length + 1];
        System.arraycopy(this.TaSimbolos, 0, aSimboloNuevo, 0, this.TaSimbolos.length);

        TblaSimbolos oSimbolo = new TblaSimbolos();
        oSimbolo.setSimbolo(sSimbolo);
        oSimbolo.setLexema(sLexema);
        oSimbolo.setLinea(sLinea);
        oSimbolo.setFinalPosSimbolo(sPosInicioLexema);
        oSimbolo.setFinalPosSimbolo(sPosFinalLexema);

        aSimboloNuevo[aSimboloNuevo.length - 1] = oSimbolo;

        this.TaSimbolos = aSimboloNuevo;
    }//Fin del metodo AgregarTablaSimbolos

    int ObtenerLinea(String sCodigoFuente, int nInicio) {
        int nLinea = 1;
        Pattern pPatron = Pattern.compile("\n");
        Matcher mMatcher = pPatron.matcher(sCodigoFuente);
        mMatcher.region(0, nInicio);

        while (mMatcher.find()) {
            nLinea++;
        }
        return (nLinea);
    }
}