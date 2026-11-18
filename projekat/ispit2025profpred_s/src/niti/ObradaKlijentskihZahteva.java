/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package niti;

import controller.Controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Angazovanje;
import model.Predmet;
import model.Profesor;
import operacije.Operacije;
import transfer.KlijentskiZahtev;
import transfer.ServerskiOdgovor;

/**
 *
 * @author Novak
 */
public class ObradaKlijentskihZahteva extends Thread {
    
    private Socket s;
    
    public ObradaKlijentskihZahteva(Socket s) {
        this.s = s;
    }
    
    @Override
    public void run() {
        while (true) {
            KlijentskiZahtev kz = primiZahtev();
            ServerskiOdgovor so = new ServerskiOdgovor();
            
            switch (kz.getOperacija()) {
                case Operacije.LOGIN:
                    Profesor p = Controller.getInstance().login((Profesor) kz.getParam());
                    so.setOdgovor(p);
                    break;
                
                case Operacije.VRATI_ANGAZOVANJA_PROFESORA:
                    List<Angazovanje> angazovanja = Controller.getInstance().vratiAngazovanja((Profesor) kz.getParam());
                    System.out.println("usao u glavnu formu");
                    so.setOdgovor(angazovanja);
                    break;
                
                case Operacije.VRATI_ANGAZOVANJA_NA_PREDMETU:
                    List<Angazovanje> angazovanja2 = Controller.getInstance().vratiAngazovanjaNaPredmetu((Predmet) kz.getParam());
                    so.setOdgovor(angazovanja2);
                    break;
                
                case Operacije.OBRISI_ANGAZOVANJE:
                    boolean uspeh = Controller.getInstance().obrisiAngazovanje((Angazovanje) kz.getParam());
                    so.setOdgovor(uspeh);
                    break;
                
                case Operacije.VRATI_PREDMETE:
                    List<Predmet> predmeti = Controller.getInstance().vratiPredmete();
                    so.setOdgovor(predmeti);
                    break;
                
                case Operacije.VRATI_PROFESORE:
                    List<Profesor> profesori = Controller.getInstance().vratiProfesore();
                    so.setOdgovor(profesori);
                    break;
                
                case Operacije.DODAJ_ANGAZOVANJE:
                    boolean uspeh2 = Controller.getInstance().dodajAngazovanje((Angazovanje) kz.getParam());
                    so.setOdgovor(uspeh2);
                    break;
                default:
                    System.out.println("Greska");
            }
            
            posaljiOdgovor(so);
        }
    }
    
    private KlijentskiZahtev primiZahtev() {
        try {
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            return (KlijentskiZahtev) ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(ObradaKlijentskihZahteva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ObradaKlijentskihZahteva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private void posaljiOdgovor(ServerskiOdgovor so) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(so);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ObradaKlijentskihZahteva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
