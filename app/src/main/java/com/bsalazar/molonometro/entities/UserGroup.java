package com.bsalazar.molonometro.entities;

/**
 * Created by bsalazar on 20/10/2016.
 */
public class UserGroup {

    private String name;
    private int Molopuntos;

    public UserGroup(String name, int molopuntos) {
        this.name = name;
        Molopuntos = molopuntos;
    }

    public UserGroup() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMolopuntos() {
        return Molopuntos;
    }

    public void setMolopuntos(int molopuntos) {
        Molopuntos = molopuntos;
    }
}
