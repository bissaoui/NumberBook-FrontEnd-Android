package com.example.numberbook.services;

import com.example.numberbook.classes.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactService {

    private List<Contact> contacts ;
    private static ContactService instance;


    private ContactService(){
        contacts= new ArrayList<>();
    }

    public static ContactService getInstance() {
        if (instance==null)
            instance = new ContactService();
        return instance;
    }
    public List<Contact> getContacts(){
        return contacts;
    }
}
