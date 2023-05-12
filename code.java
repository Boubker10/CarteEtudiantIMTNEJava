import java.time.DayOfWeek;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.*;

class PlageHoraire {
    
    private LocalTime debut;
    private LocalTime fin;
    private boolean accesWeekend;
    
    
    public PlageHoraire(LocalTime deb, LocalTime fin, boolean accesWeekend){
        this.debut = deb;
        this.fin = fin;
        this.accesWeekend = accesWeekend;
        
        
    }

    public boolean estDansLaPlage(LocalDateTime i) {
        boolean res = false;
        if (this.accesWeekend) {
            if (i.getHour() >= this.debut.getHour() && i.getHour() < this.fin.getHour()) {
                res = true;
            }

        } else if (i.getDayOfWeek() != DayOfWeek.SATURDAY && i.getDayOfWeek() != DayOfWeek.SUNDAY) {
            if (i.getHour() >= this.debut.getHour() && i.getHour() < this.fin.getHour()) {
                res = true;
            }

        }
        return res;
    }

    public String toString() {
        String retour = "";
        retour += "Accès de " + this.debut.toString() + " à " + this.fin.toString();
        if (this.accesWeekend) {
            retour += " tous les jours";

        } else {
            retour += " sauf le weekend";
        }
        return retour;
    }

}

class Batiment{
    
    private String nom;
    private String localisation;
    
    public Batiment (String nouveauNom, String nouvelleLocalisation) {
        this.nom = nouveauNom;
        this.localisation = nouvelleLocalisation;
    }

    public void setNom(String newNom) {
        this.nom = newNom;
    }

    public void setLocalisation(String newLoc) {
        this.localisation = newLoc;

    }

    public String toString() {
        String retour = "";
        retour += nom + " " + "("+ localisation + ")";
        return retour;
    }
}

class DroitsAcces {
       private LinkedHashMap<Batiment, PlageHoraire> droits = new LinkedHashMap<Batiment, PlageHoraire>();

    public void ajouteDroits(Batiment b, PlageHoraire p) {
        this.droits.put(b, p);
    }

    public String toString() {
        String retour = "";

        for (Map.Entry carte : this.droits.entrySet()) {
            retour += "\n" + carte.getKey().toString() + " : " + carte.getValue().toString();
        }
        return retour;
    }
    
    public PlageHoraire getPlageHoraire(Batiment b){
        return this.droits.get(b);
    }
}

class Titulaire {
  private String nom;
    private String prenom;
    private DroitsAcces droits;
    
    public Titulaire(String nom, String prenom, DroitsAcces d){
        this.nom = nom;
        this.prenom = prenom;
        this.droits = d;
    }
    
    
    public String toString(){
        String retour = "";
        retour += "Titulaire : " + this.nom + " " + this.prenom;
        retour += droits.toString();
        
        return retour;
    }
    
    public DroitsAcces getDroitsAcces(){
        return this.droits;
    }
}

class Eleve extends Titulaire {
 
    private LocalDate finDeScolarite;

    public Eleve(String nom, String prenom, DroitsAcces d, LocalDate f) {
        super(nom, prenom, d);

        this.finDeScolarite = f;
    }

    public String toString() {
        String retour = "";
        retour += Eleve.super.toString();
        retour += "\n" + "Fin de scolarité : " + this.finDeScolarite.toString();

        return retour;
    }
}

class Personnel extends Titulaire{
   private LocalDate finDeContrat;
    
    public Personnel(String nom, String prenom, DroitsAcces d, LocalDate f){
        super(nom,prenom,d);
        
        this.finDeContrat = f;
    }
    
    public String toString(){
        String retour = "";
        retour += Personnel.super.toString();
        retour += "\n" + "Fin de contrat : " + this.finDeContrat.toString();
        return retour;
    }
}

class Carte {
 
    private Titulaire t;
    private int num;
    private int nbTotalCartes;
    private double solde;

    public Carte(Titulaire t) {
        this.t = t;
        this.num = this.nbTotalCartes + 1;
        this.nbTotalCartes += 1;
    }

    public PlageHoraire getPlageHoraireAccesBatiment(Batiment b) {
        DroitsAcces d = t.getDroitsAcces();
        return d.getPlageHoraire(b);

    }
    
    public String toString(){
        String retour = "";
        retour += "Carte numéro : " + this.num;
        retour += "\n" + t.toString() + "\nSolde : " + solde + " euros";
        return retour;
    }

    public void debit(double montant){
        if(this.solde - montant < 0){
            System.out.println("Solde insuffisant");
        }
        else{
            this.solde = this.solde - montant;
            System.out.println("Nouveau solde : " + this.solde + " euros");
        }
    }
    
    public void credit(double montant){
        this.solde += montant;
        System.out.println("Nouveau solde : " + this.solde + " euros");
        
    }
    
    
}

class LecteurCarte {
  Batiment bat;

    LecteurCarte(Batiment b){
        bat = b;
    }

    void lecture(Carte c){
        PlageHoraire p = c.getPlageHoraireAccesBatiment(bat);
        if(p.estDansLaPlage(LocalDateTime.now())){
            System.out.println("Accès autorisé");
        }
        else{
            System.out.println("Accès refusé");
        }
    }
}

class IMTNE {
    ArrayList<Batiment> lesBatiments;
    DroitsAcces eleves;
    DroitsAcces personnel;

public IMTNE(){
    creerIMTNordEurope();
    eleves = new DroitsAcces();
    personnel = new DroitsAcces();
    creerDroitsPersonnel();
    creerDroitsEleves();
}

    public void creerIMTNordEurope() {
        lesBatiments = new ArrayList<Batiment>();
        lesBatiments.add((new Batiment("Laplace", "Bourseul")));
        lesBatiments.add((new Batiment("Newton", "Bourseul")));
        lesBatiments.add((new Batiment("CERI SN", "Lahure")));
        lesBatiments.add((new Batiment("CERI SN", "Vda")));
        lesBatiments.add((new Batiment("GC1", "Lahure")));
        lesBatiments.add((new Batiment("GC2", "Lahure")));
        lesBatiments.add((new Batiment("DISI", "Lahure")));
        lesBatiments.add((new Batiment("Batiment central", "Lahure")));

    }
    
    public void creerDroitsPersonnel(){
        PlageHoraire p = new PlageHoraire(LocalTime.of(7,0), LocalTime.of(21,0), true);
        for(int i =0; i<lesBatiments.size();i++){
            personnel.ajouteDroits(lesBatiments.get(i),p);
            
        }
        
    }
    
    public void creerDroitsEleves(){
        PlageHoraire p = new PlageHoraire(LocalTime.of(8,0), LocalTime.of(19, 0),false);
        for(int i = 0; i< lesBatiments.size(); i++){
            eleves.ajouteDroits(lesBatiments.get(i),p);
        }
        PlageHoraire minuit = new PlageHoraire(LocalTime.of(0,0), LocalTime.of(0,0), false);
        for(int i = 0; i < 2; i++){
            eleves.ajouteDroits(lesBatiments.get(i+6),minuit);
        }
    }

}
