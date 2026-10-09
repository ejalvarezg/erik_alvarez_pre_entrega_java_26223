package com.itamarket.articulo.model;

public abstract class Articulo {
    private int codigo;
    private String nombre;
    private double precio;
    private Categoria categoria;

    // Constructores, getters y setters

    public Articulo(int codigo, String nombre, double precio, Categoria categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /* Métodos abstractos
    * No se definen acá, sino en las subclases
    * */
    public abstract String getTipoArticulo();
    public abstract String getDetalleEspecifico();

    /* Reescritura de toString(): polimorfismo*/
    @Override
    public String toString() {
        return "Artículo {" +
                "código=" + codigo +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", categoría='" + categoria.getNombre() + '\'' +
                ", tipo='" + this.getTipoArticulo() + '\'' +
                ", detalle='" + this.getDetalleEspecifico() + '\'' +
                '}';
    }
}