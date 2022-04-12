package com.example.numberbook.services;

import com.example.numberbook.classes.Contact;
import com.example.numberbook.classes.Pays;

import java.util.ArrayList;
import java.util.List;

public class PaysService {

    private List<Pays> pays ;
    private static PaysService instance;


    private PaysService(){
        pays= new ArrayList<>();
    }

    public static PaysService getInstance() {
        if (instance==null)
            instance = new PaysService();
        return instance;
    }
    public Pays findIdByPrefix(String prfix){
        Pays t=null;
        for (Pays p : pays){
            if (p.getPrefix().equals(prfix)){
                t=p;
            }
        }
        return t;
    }
    public List<Pays> getPays(){
        return pays;
    }
}
