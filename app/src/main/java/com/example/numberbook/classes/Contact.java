package com.example.numberbook.classes;

public class Contact {

    private int idC;
    private String nom;
    private String telephone;
    private String imei;
    private int pays;
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getPays() {
        return pays;
    }
    public void setPays(int pays) {
        this.pays = pays;
    }
    public int getIdC() {
        return idC;
    }
    public void setIdC(int idC) {
        this.idC = idC;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getImei() {
        return imei;
    }
    public void setImei(String imei) {
        this.imei = imei;
    }
    public Contact(int idC, String nom, String telephone, String imei,int pays) {
        this.idC = idC;
        this.nom = nom;
        this.telephone = telephone;
        this.imei = imei;
        this.pays=pays;
    }
    public Contact(String nom, String telephone, String imei,int pays,String prefix) {
        this.nom = nom;
        this.telephone = telephone;
        this.imei = imei;
        this.pays=pays;
        this.prefix=prefix;
    }
    public Contact(String nom, String telephone, String imei) {
        this.nom = nom;
        this.telephone = telephone;
        this.imei = imei;
    }
    public Contact() {

    }

    @Override
    public String toString() {
        return "Contact{" +
                "idC=" + idC +
                ", nom='" + nom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", imei='" + imei + '\'' +
                ", pays=" + pays +
                '}';
    }
}
