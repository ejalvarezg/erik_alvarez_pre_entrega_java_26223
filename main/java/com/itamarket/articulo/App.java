package com.itamarket.articulo;

import java.util.ArrayList;
import java.util.Scanner;

import com.itamarket.articulo.model.Articulo;
import com.itamarket.articulo.model.ArticuloAlimenticio;
import com.itamarket.articulo.model.ArticuloElectronico;
import com.itamarket.articulo.model.Categoria;

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        /* La lista está declarada como ArrayList<Articulo>.
        * Es decir, almacena artículos genéricos (de poder crearse) y de clases heredadas
        * */
        ArrayList<Articulo> articulos = new ArrayList<>();

        ArrayList<Categoria> categorias = new ArrayList<>();
        precargarCategorias(categorias);

        int opcion;

        do {
            System.out.println("\n======================================================");
            System.out.println(" SISTEMA DE ARTÍCULOS");
            System.out.println("======================================================");
            System.out.println("1 - Ingresar artículo");
            System.out.println("2 - Mostrar artículos");
            System.out.println("3 - Buscar un artículo");
            System.out.println("4 - Editar un artículo");
            System.out.println("5 - Eliminar un artículo");
            System.out.println("6 - Mostrar categorías");
            System.out.println("0 - Salir");
            System.out.println("======================================================");

            opcion = leerEntero(scanner, "Ingrese una opción: ");

            switch (opcion) {
                case 1:
                    ingresarArticulo(scanner, articulos, categorias);
                    continuar(scanner);
                    break;
                case 2:
                    mostrarArticulos(articulos);
                    continuar(scanner);
                    break;
                case 3:
                    buscarArticulo(scanner, articulos);
                    continuar(scanner);
                    break;
                case 4:
                    editarArticulo(scanner, articulos, categorias);
                    continuar(scanner);
                    break;
                case 5:
                    eliminarArticulo(scanner, articulos);
                    continuar(scanner);
                    break;
                case 6:
                    mostrarCategorias(categorias);
                    continuar(scanner);
                    break;
                case 0:
                    System.out.println("\nSaliendo de la consola. ¡Adiós!");
                    break;
                default:
                    System.out.println("\nLa opción que ingresaste no es válida");
            }
        } while (opcion != 0);

        scanner.close();
    }

    /*
     * METHOD: precargarCategorias
     * --------------------------------------------------
     * Por ahora son categorías sin CRUD propio
     */
    public static void precargarCategorias(ArrayList<Categoria> categorias) {
        categorias.add(new Categoria(1, "Alimentos", "Productos alimenticios"));
        categorias.add(new Categoria(2, "Electrónica", "Productos tecnológicos y electrónicos"));
        categorias.add(new Categoria(3, "Periféricos", "Accesorios para computadora"));
        categorias.add(new Categoria(4, "Limpieza", "Artículos de limpieza del hogar"));
    }

    /*
     * METHOD: ingresarArticulo
     * --------------------------------------------------
     * Ingresa un artículo: genéricos y específicos según categoría
     */
    public static void ingresarArticulo(
            Scanner scanner,
            ArrayList<Articulo> articulos,
            ArrayList<Categoria> categorias
    ) {
        System.out.println("\n--- INGRESAR ARTÍCULO ---");
        System.out.println("1 - Artículo alimenticio");
        System.out.println("2 - Artículo electrónico");

        int tipo;
        do {
            tipo = leerEntero(scanner, "Seleccione el tipo de artículo: ");
            if (tipo != 1 && tipo != 2) {
                System.out.println("Error: debe elegir 1 o 2.");
            }
        } while (tipo != 1 && tipo != 2);

        int codigo = leerEntero(scanner, "Ingrese el Código del artículo: ");

        // Verifica si existe el artículo
        if (buscarArticuloPorCodigo(articulos, codigo) != null) {
            System.out.println("Error: ya existe un artículo con ese código.");
            return;
        }

        String nombre = leerTextoNoVacio(scanner, "Ingrese el nombre del artículo: ");
        double precio = leerDoubleNoNegativo(scanner, "Ingrese el precio del artículo: ");

        mostrarCategorias(categorias);
        Categoria categoria = pedirCategoriaExistente(scanner, categorias);

        // Variable sin instanciar - Se hará con una clase hija
        Articulo articulo;

        if (tipo == 1) {
            // El artículo es alimenticio
            int diasParaVencimiento = leerEnteroNoNegativo(scanner, "Ingrese los días hasta vencimiento: ");
            articulo = new ArticuloAlimenticio(codigo, nombre, precio, categoria, diasParaVencimiento);
        } else {
            // El artículo es electrónico
            int garantiaMeses = leerEnteroNoNegativo(scanner, "Ingrese la garantía en meses: ");
            articulo = new ArticuloElectronico(codigo, nombre, precio, categoria, garantiaMeses);
        }

        articulos.add(articulo);

        System.out.println("Artículo ingresado correctamente.");
        System.out.println("Resumen del artículo creado:");
        System.out.println(articulo);
    }

    /*
     * METHOD: mostrarArticulos
     * --------------------------------------------------
     * Muestra los artículos. Toma el toString() de la clase hija.
     */
    public static void mostrarArticulos(ArrayList<Articulo> articulos) {
        System.out.println("\n--- LISTADO DE ARTÍCULOS ---");

        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }

        for (Articulo articulo : articulos) {
            System.out.println(articulo);
        }
    }

    /*
     * METHOD: buscarArticulo
     * --------------------------------------------------
     * Busca y muestra un artículo por id
     */
    public static void buscarArticulo(Scanner scanner, ArrayList<Articulo> articulos) {
        System.out.println("\n--- BUSCAR ARTÍCULO ---");

        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }

        int codigo = leerEntero(scanner, "Ingrese el código del artículo a buscar: ");
        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }

        System.out.println("Artículo encontrado:");
        System.out.println(articulo);
        System.out.println("Detalle específico: " + articulo.getDetalleEspecifico());
    }

    /*
     * METHOD: editarArticulo
     * --------------------------------------------------
     * Modifica los datos comunes y los específicos del artículo.
     * Se busca por codigo. Se usa intanceof para editar especificidades según clase heredada
     */
    public static void editarArticulo(
            Scanner scanner,
            ArrayList<Articulo> articulos,
            ArrayList<Categoria> categorias
    ) {
        System.out.println("\n--- EDITAR ARTÍCULO ---");

        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }

        int codigo = leerEntero(scanner, "Ingrese el código del artículo a editar: ");

        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }

        System.out.println("\n(Presione ENTER en cualquier campo para mantener el valor actual)");

        String nuevoNombre = leerTextoOpcional(scanner, "Nombre", articulo.getNombre());
        double nuevoPrecio = leerDoubleOpcional(scanner, "Precio", articulo.getPrecio());

        mostrarCategorias(categorias);
        Categoria nuevaCategoria = pedirCategoriaOpcional(scanner, categorias, articulo.getCategoria());

        articulo.setNombre(nuevoNombre);
        articulo.setPrecio(nuevoPrecio);
        articulo.setCategoria(nuevaCategoria);

        // Si es electrónico, modifica la garantía.
        if (articulo instanceof ArticuloElectronico) {
            ArticuloElectronico electronico = (ArticuloElectronico) articulo;

            int nuevaGarantia = leerEnteroOpcional(scanner, "Garantía en meses", electronico.getGarantiaMeses());
            electronico.setGarantiaMeses(nuevaGarantia);
        }

        // Si es alimenticio, modifica los días para vencimiento.
        if (articulo instanceof ArticuloAlimenticio) {
            ArticuloAlimenticio alimenticio = (ArticuloAlimenticio) articulo;

            int nuevosDias = leerEnteroOpcional(scanner, "Días para vencimiento", alimenticio.getDiasParaVencimiento());
            alimenticio.setDiasParaVencimiento(nuevosDias);
        }

        System.out.println("Artículo modificado correctamente.");
    }

    /*
     * METHOD: eliminarArticulo
     * --------------------------------------------------
     * Elimina un artículo por código. Pide confirmación
     */
    public static void eliminarArticulo(Scanner scanner, ArrayList<Articulo> articulos) {
        System.out.println("\n--- ELIMINAR ARTÍCULO ---");

        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }

        int codigo = leerEntero(scanner, "Ingrese el código del artículo a eliminar: ");

        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }

        String seguro;
        seguro = leerTextoNoVacio(scanner, "Está seguro de que desea eliminar el artículo " + articulo.getNombre() + " (1 para confirmar)? ");
        if (seguro.equals("1")) {
            articulos.remove(articulo);
            System.out.println("Artículo eliminado correctamente.");
        } else {
            System.out.println("No se eliminó ningún artículo.");
        }


    }

    /*
     * METHOD: mostrarCategorias
     * --------------------------------------------------
     * Muestra las categorías disponibles.
     */
    public static void mostrarCategorias(ArrayList<Categoria> categorias) {
        System.out.println("\n--- CATEGORÍAS DISPONIBLES ---");

        for (Categoria categoria : categorias) {
            System.out.println(categoria);
        }
    }

    /*
     * METHOD: pedirCategoriaExistente
     * --------------------------------------------------
     * Obliga a elegir una categoría existente.
     */
    public static Categoria pedirCategoriaExistente(Scanner scanner, ArrayList<Categoria> categorias) {
        while (true) {

            // Pide id y busca la categoria
            int codigoCategoria = leerEntero(scanner, "Ingrese el Código de la categoría: ");
            Categoria categoria = buscarCategoriaPorCodigo(categorias, codigoCategoria);

            if (categoria != null) {
                return categoria;
            }
            // O escribió un id inexistente
            System.out.println("Error: la categoría no existe.");
        }
    }

    /*
     * METHOD: buscarArticuloPorCodigo
     * --------------------------------------------------
     * Recorre la lista y devuelve el objeto cuyo codigo coincida.
     */
    public static Articulo buscarArticuloPorCodigo(ArrayList<Articulo> articulos, int codigo) {
        for (Articulo articulo : articulos) {
            if (articulo.getCodigo() == codigo) {
                return articulo;
            }
        }
        return null;
    }

    /*
     * METHOD: buscarCategoriaPorCodigo
     * --------------------------------------------------
     * Idea: es casi el mismo código que buscarArticuloPorCodigo()
     * ¿Qué se puede hacer para evitar repetir código?
     */
    public static Categoria buscarCategoriaPorCodigo(ArrayList<Categoria> categorias, int codigo) {
        for (Categoria categoria : categorias) {
            if (categoria.getCodigo() == codigo) {
                return categoria;
            }
        }
        return null;
    }

    /*
     * METHOD: leerEntero
     * --------------------------------------------------
     * Lee enteros de forma segura.
     */
    public static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número entero válido.");
            }
        }
    }

    /*
     * METHOD: leerEnteroNoNegativo
     * --------------------------------------------------
     * Lee enteros y además valida que no sean negativos.
     */
    public static int leerEnteroNoNegativo(Scanner scanner, String mensaje) {
        while (true) {
            int valor = leerEntero(scanner, mensaje);

            if (valor < 0) {
                System.out.println("Error: el valor no puede ser negativo.");
                continue;
            }

            return valor;
        }
    }

    /*
     * METHOD: leerDoubleNoNegativo
     * --------------------------------------------------
     * Lee un decimal y valida que no sea negativo.
     */
    public static double leerDoubleNoNegativo(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = Double.parseDouble(scanner.nextLine());

                if (valor < 0) {
                    System.out.println("Error: el precio no puede ser negativo.");
                    continue;
                }

                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número decimal válido.");
            }
        }
    }

    /*
     * METHOD: leerTextoNoVacio
     * --------------------------------------------------
     * Obliga a que el texto no sea vacío
     */
    public static String leerTextoNoVacio(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine();

            if (!texto.trim().isEmpty()) {
                return texto.trim();
            }

            System.out.println("Error: el texto no puede estar vacío.");
        }
    }

    /*
     * METHOD: leerTextoOpcional
     * --------------------------------------------------
     * Lee texto. Si presiona Enter (cadena vacía), conserva el valor actual
     * Esta función se implementa para mostrar el valor por defecto y aceptarlo
     * con un Enter, cuando se esté editando un artículo.
     */
    public static String leerTextoOpcional(Scanner scanner, String mensaje, String valorActual) {
        System.out.print(mensaje + " [" + valorActual + "]: ");
        String entrada = scanner.nextLine().trim();
        if (entrada.isEmpty()) {
            return valorActual;
        }
        return entrada;
    }

    /*
     * METHOD: leerDoubleOpcional
     * --------------------------------------------------
     * Lee decimales. Si presiona Enter, conserva el valor actual
     * Esta función se implementa para mostrar el valor por defecto y aceptarlo
     * con un Enter, cuando se esté editando un artículo.
     */
    public static double leerDoubleOpcional(Scanner scanner, String mensaje, double valorActual) {
        while (true) {
            System.out.print(mensaje + " [" + valorActual + "]: ");
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                return valorActual;
            }
            try {
                double valor = Double.parseDouble(entrada);
                if (valor < 0) {
                    System.out.println("Error: el valor no puede ser negativo.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número decimal válido o presionar ENTER para mantener el actual.");
            }
        }
    }

    /*
     * METHOD: leerEnteroOpcional
     * --------------------------------------------------
     * Lee enteros. Si presiona Enter, conserva el valor actual
     * Esta función se implementa para mostrar el valor por defecto y aceptarlo
     * con un Enter, cuando se esté editando un artículo.
     */
    public static int leerEnteroOpcional(Scanner scanner, String mensaje, int valorActual) {
        while (true) {
            System.out.print(mensaje + " [" + valorActual + "]: ");
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                return valorActual;
            }
            try {
                int valor = Integer.parseInt(entrada);
                if (valor < 0) {
                    System.out.println("Error: el valor no puede ser negativo.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número entero válido o presionar ENTER para mantener el actual.");
            }
        }
    }

    public static Categoria pedirCategoriaOpcional(Scanner scanner, ArrayList<Categoria> categorias, Categoria categoriaActual) {
        while (true) {
            System.out.print("Ingrese el código de la categoría [" + categoriaActual.getCodigo() + " - " + categoriaActual.getNombre() + "] (ENTER para mantener): ");
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty()) {
                return categoriaActual;
            }

            try {
                int codigo = Integer.parseInt(entrada);
                Categoria categoria = buscarCategoriaPorCodigo(categorias, codigo);
                if (categoria != null) {
                    return categoria;
                }
                System.out.println("Error: la categoría no existe.");
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número entero válido o presionar ENTER para mantener el actual.");
            }
        }
    }

    /*
     * METHOD: continuar
     * --------------------------------------------------
     * Pide que se presione Enter para continuar con el programa
     */
    public static void continuar(Scanner scanner) {
        System.out.print("\nPresione ENTER para volver al menú...");
        scanner.nextLine();
    }
}