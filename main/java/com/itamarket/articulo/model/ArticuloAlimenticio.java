package com.itamarket.articulo.model;

/*
 * Hereda de Articulo
 * diasParaVencimiento: Cantidad de días de vigencia desde empaquetado
 */
public class ArticuloAlimenticio extends Articulo {

    private int diasParaVencimiento;

    public ArticuloAlimenticio(int codigo, String nombre, double precio, Categoria categoria, int diasParaVencimiento) {
        // Constructor de clase padre: super
        super(codigo, nombre, precio, categoria);
        this.diasParaVencimiento = diasParaVencimiento;
    }

    public int getDiasParaVencimiento() {
        return diasParaVencimiento;
    }

    public void setDiasParaVencimiento(int diasParaVencimiento) {
        this.diasParaVencimiento = diasParaVencimiento;
    }

    @Override
    public String getTipoArticulo() {
        return "Alimenticio";
    }

    @Override
    public String getDetalleEspecifico() {
        return "Días para vencimiento: " + diasParaVencimiento;
    }

    @Override
    public String toString() {
        return super.toString() + " [subtipo alimenticio]";
    }
}
