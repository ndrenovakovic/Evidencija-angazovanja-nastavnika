/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baza;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Angazovanje;
import model.Predmet;
import model.Profesor;
import model.TipAngazovanja;

/**
 *
 * @author Novak
 */
public class DBBroker {

    public boolean dodajPredmet(Predmet predmet) {
        try {
            String upit = "INSERT INTO PREDMET (naziv, sifra) VALUES (?,?)";
            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setString(1, predmet.getNaziv());
            ps.setString(2, predmet.getSifra());

            int rez = ps.executeUpdate();
            Konekcija.getInstance().getConnection().commit();

            return rez > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean dodajProfesora(Profesor profesor) {
        try {
            String proveraEmaila = "SELECT COUNT(*) AS broj FROM PROFESOR WHERE email = ?";
            PreparedStatement provera = Konekcija.getInstance().getConnection().prepareStatement(proveraEmaila);
            provera.setString(1, profesor.getEmail());
            ResultSet rs = provera.executeQuery();
            if (rs.next() && rs.getInt("broj") > 0) {
                System.out.println("Profesor sa tim emailom vec postoji");
                return false;
            }

            String upit = "INSERT INTO PROFESOR (ime, prezime, email, lozinka) VALUES (?,?,?,?)";
            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setString(1, profesor.getIme());
            ps.setString(2, profesor.getPrezime());
            ps.setString(3, profesor.getEmail());
            ps.setString(4, profesor.getLozinka());

            int rez = ps.executeUpdate();
            Konekcija.getInstance().getConnection().commit();
            return rez > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public void popuniBazu2() {
        Profesor p1 = new Profesor(0, "ime1", "prezime1", "ime1@gmail.com", "lozinka");
        dodajProfesora(p1);

        Predmet pr1 = new Predmet(0, "P1", "1111");
        dodajPredmet(pr1);

        Angazovanje a1 = new Angazovanje(0, p1, pr1, TipAngazovanja.PREDAVANJA);
        dodajAngazovanje(a1);
    }

    public void popuniBazu() {
        try {
            String upit1 = "INSERT INTO predmet(naziv, sifra) VALUES (?,?)";

            PreparedStatement ps1 = Konekcija.getInstance().getConnection().prepareStatement(upit1);
            ps1.setString(1, "P1");
            ps1.setString(2, "111");
            ps1.executeUpdate();
            Konekcija.getInstance().getConnection().commit();

            PreparedStatement ps2 = Konekcija.getInstance().getConnection().prepareStatement(upit1);
            ps2.setString(1, "P2");
            ps2.setString(2, "222");
            ps2.executeUpdate();
            Konekcija.getInstance().getConnection().commit();

            PreparedStatement ps3 = Konekcija.getInstance().getConnection().prepareStatement(upit1);
            ps3.setString(1, "P3");
            ps3.setString(2, "333");
            ps3.executeUpdate();
            Konekcija.getInstance().getConnection().commit();

            String upit2 = "INSERT INTO PROFESOR (ime, prezime, email, lozinka) VALUES (?,?,?,?)";

            PreparedStatement ps4 = Konekcija.getInstance().getConnection().prepareStatement(upit2);
            ps4.setString(1, "Prof1");
            ps4.setString(2, "Prof1");
            ps4.setString(3, "prof1@gmail.com");
            ps4.setString(4, "prof1");
            ps4.executeUpdate();
            Konekcija.getInstance().getConnection().commit();

            PreparedStatement ps5 = Konekcija.getInstance().getConnection().prepareStatement(upit2);
            ps5.setString(1, "Prof2");
            ps5.setString(2, "Prof2");
            ps5.setString(3, "prof2@gmail.com");
            ps5.setString(4, "prof2");
            ps5.executeUpdate();
            Konekcija.getInstance().getConnection().commit();

            String upit3 = "INSERT INTO ANGAZOVANJE (profesor_id, predmet_id, tip) VALUES (?,?,?)";

            PreparedStatement ps6 = Konekcija.getInstance().getConnection().prepareStatement(upit3);
            ps6.setInt(1, 5);
            ps6.setInt(2, 7);
            ps6.setString(3, TipAngazovanja.PREDAVANJA.toString());
            ps6.executeUpdate();
            Konekcija.getInstance().getConnection().commit();

            PreparedStatement ps7 = Konekcija.getInstance().getConnection().prepareStatement(upit3);

            ps7.setInt(1, 6);
            ps7.setInt(2, 7);
            ps7.setString(3, TipAngazovanja.VEZBA.toString());
            ps7.executeUpdate();
            Konekcija.getInstance().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Profesor> vratiProfesore() {
        String upit = "SELECT * FROM PROFESOR";
        List<Profesor> lista = new ArrayList<>();

        Statement st;
        try {
            st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {
                Profesor p = new Profesor();
                p.setId(rs.getInt("id"));
                p.setIme(rs.getString("ime"));
                p.setPrezime(rs.getString("prezime"));
                p.setEmail(rs.getString("email"));
                p.setLozinka(rs.getString("lozinka"));

                lista.add(p);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Profesor login(Profesor profesor) {

        List<Profesor> profesori = vratiProfesore();

        for (Profesor pr : profesori) {
            if (pr.equals(profesor)) {
                return pr;
            }
        }

        return null;
    }

    public List<Angazovanje> vratiAngazovanja(Profesor profesor) {
        try {
            List<Angazovanje> lista = new ArrayList<>();
            String upit = "SELECT * FROM ANGAZOVANJE A JOIN PREDMET P ON a.predmet_id = p.id WHERE profesor_id = " + profesor.getId();
            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {
                Angazovanje a = new Angazovanje();

                a.setProfesor(profesor);

                Predmet p = new Predmet();
                p.setId(rs.getInt("p.id"));
                p.setNaziv(rs.getString("p.naziv"));;
                p.setSifra(rs.getString("p.sifra"));
                a.setPredmet(p);

                String tipString = rs.getString("a.tip");
                a.setTip(TipAngazovanja.valueOf(tipString));
                a.setId(rs.getInt("a.id"));

                lista.add(a);
                return lista;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Angazovanje> vratiAngazovanjaNaPredmetu(Predmet predmet) {
        try {
            List<Angazovanje> lista = new ArrayList<>();
            String upit = "SELECT * FROM ANGAZOVANJE A JOIN PROFESOR P ON a.profesor_id = p.id WHERE predmet_id = " + predmet.getId();
            Statement st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {
                Angazovanje a = new Angazovanje();
                a.setPredmet(predmet);

                Profesor p = new Profesor();
                p.setId(rs.getInt("p.id"));
                p.setIme(rs.getString("p.ime"));
                p.setPrezime(rs.getString("p.prezime"));
                p.setEmail(rs.getString("p.email"));
                p.setLozinka(rs.getString("p.lozinka"));

                a.setProfesor(p);

                String tipString = rs.getString("a.tip");
                a.setTip(TipAngazovanja.valueOf(tipString));
                a.setId(rs.getInt("a.id"));

                lista.add(a);
                return lista;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean obrisiAngazovanje(Angazovanje angazovanje) {
        String upit = "DELETE FROM ANGAZOVANJE WHERE id =" + angazovanje.getId();
        try {
            Statement st = Konekcija.getInstance().getConnection().createStatement();
            int rezultat = st.executeUpdate(upit);

            Konekcija.getInstance().getConnection().commit();
            return rezultat > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Predmet> vratiPredmete() {
        String upit = "SELECT * FROM PREDMET";
        List<Predmet> lista = new ArrayList<>();

        Statement st;
        try {
            st = Konekcija.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(upit);

            while (rs.next()) {
                Predmet p = new Predmet();
                p.setId(rs.getInt("id"));
                p.setNaziv(rs.getString("naziv"));
                p.setSifra(rs.getString("Sifra"));

                lista.add(p);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean dodajAngazovanje(Angazovanje angazovanje) {
        try {
            String upit1 = "SELECT COUNT(*) AS broj FROM ANGAZOVANJE WHERE predmet_id = ? AND tip = ?";

            PreparedStatement ps1 = Konekcija.getInstance().getConnection().prepareStatement(upit1);
            ps1.setInt(1, angazovanje.getPredmet().getId());
            ps1.setString(2, String.valueOf(angazovanje.getTip().PREDAVANJA));

            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next() && rs1.getInt("broj") >= 3) {
                return false; //vec ima 3 profesora na predavanju
            }

            String upit2 = "SELECT COUNT(DISTINCT predmet_id) AS broj FROM ANGAZOVANJE WHERE profesor_id=?";

            PreparedStatement ps2 = Konekcija.getInstance().getConnection().prepareStatement(upit2);
            ps2.setInt(1, angazovanje.getProfesor().getId());

            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next() && rs2.getInt("broj") >= 5) {
                return false; //vec je na 5 predmeta
            }

            String upit = "INSERT INTO ANGAZOVANJE(profesor_id, predmet_id, tip) VALUES (?,?,?)";

            PreparedStatement ps = Konekcija.getInstance().getConnection().prepareStatement(upit);
            ps.setInt(1, angazovanje.getProfesor().getId());
            ps.setInt(2, angazovanje.getPredmet().getId());
            ps.setString(3, String.valueOf(angazovanje.getTip()));

            int rezultat = ps.executeUpdate();
            Konekcija.getInstance().getConnection().commit();
            return rezultat > 0;

        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
