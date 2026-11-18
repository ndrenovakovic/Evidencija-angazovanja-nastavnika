/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import baza.DBBroker;
import java.util.List;
import model.Angazovanje;
import model.Predmet;
import model.Profesor;

/**
 *
 * @author Novak
 */
public class Controller {

    private DBBroker dbb;
    private static Controller instance;

    private Controller() {
        dbb = new DBBroker();
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void popuniBazu() {
        dbb.popuniBazu();
    }

    public Profesor login(Profesor profesor) {
        return dbb.login(profesor);
    }

    public List<Angazovanje> vratiAngazovanja(Profesor profesor) {
        return dbb.vratiAngazovanja(profesor);
    }

    public List<Angazovanje> vratiAngazovanjaNaPredmetu(Predmet predmet) {
        return dbb.vratiAngazovanjaNaPredmetu(predmet);
    }

    public boolean obrisiAngazovanje(Angazovanje angazovanje) {
        return dbb.obrisiAngazovanje(angazovanje);
    }

    public List<Predmet> vratiPredmete() {
        return dbb.vratiPredmete();
    }

    public List<Profesor> vratiProfesore() {
        return dbb.vratiProfesore();
    }

    public boolean dodajAngazovanje(Angazovanje angazovanje) {
        return dbb.dodajAngazovanje(angazovanje);
    }
}
