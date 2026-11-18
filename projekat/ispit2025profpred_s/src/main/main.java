/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import controller.Controller;
import niti.PokreniServer;

/**
 *
 * @author Novak
 */
public class main {

    public static void main(String[] args) {
        PokreniServer ps = new PokreniServer();
        ps.start();
        //Controller.getInstance().popuniBazu();
    }
}
