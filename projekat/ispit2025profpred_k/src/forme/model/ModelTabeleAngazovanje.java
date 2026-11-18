/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forme.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Angazovanje;

/**
 *
 * @author Novak
 */
public class ModelTabeleAngazovanje extends AbstractTableModel {

    List<Angazovanje> lista;
    String[] kolone = {"id", "naziv", "sifra", "tip"};

    public ModelTabeleAngazovanje(List<Angazovanje> lista) {
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }

    public List<Angazovanje> getLista() {
        return lista;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Angazovanje a = lista.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return a.getId();
            case 1:
                return a.getPredmet().getNaziv();
            case 2:
                return a.getPredmet().getSifra();
            case 3:
                return a.getTip().toString();
            default:
                return "n/a";
        }
    }

}
