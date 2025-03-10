package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Classe Utilisateur (super-classe)
abstract class Utilisateur {
    String nom;
    private String email;
    private String motDePasse;

    public Utilisateur(String nom, String email, String motDePasse) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public boolean verifierMotDePasse(String motDePasse) {
        return this.motDePasse.equals(motDePasse);
    }

    public abstract void afficherRole();
}

// Classe Client
class Client extends Utilisateur {
    public Client(String nom, String email, String motDePasse) {
        super(nom, email, motDePasse);
    }

    @Override
    public void afficherRole() {
        System.out.println("Bienvenue Client " + super.nom);
    }

    public String getNom() {
        return getNom();
    }

}

// Classe Administrateur
class Administrateur extends Utilisateur {
    public Administrateur(String nom, String email, String motDePasse) {
        super(nom, email, motDePasse);
    }

    @Override
    public void afficherRole() {
        System.out.println("WELCOME " + super.nom);
    }

    public void ajouterProduit(Map<String, Produit> produits, String nom, double prix, int stock, String categorie) {
        produits.put(nom, new Produit(nom, prix, stock, categorie));
        System.out.println("Produit ajouté : " + nom);
    }

    public void supprimerProduit(Map<String, Produit> produits, String nomProduit) {
        produits.remove(nomProduit);
        System.out.println("Produit supprimé : " + nomProduit);
    }
}

// Classe Produit
class Produit {
    private String nom;
    private double prix;
    private int stock;
    private String categorie;

    public Produit(String nom, double prix, int stock, String categorie) {
        this.nom = nom;
        this.prix = prix;
        this.stock = stock;
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Nom: " + nom + ", Prix: " + prix + "€, Stock: " + stock + ", Catégorie: " + categorie;
    }

    public String getNom() {
        return nom;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

// Classe Commande
class Commande {
    private static int compteur = 1;
    private int id;
    private Client client;
    private Map<Produit, Integer> produits = new HashMap<>();

    public Commande(Client client) {
        this.id = compteur++;
        this.client = client;
    }

    public void ajouterProduit(Produit produit, int quantite) {
        if (produit.getStock() >= quantite) {
            produits.put(produit, quantite);
            produit.setStock(produit.getStock() - quantite);
            System.out.println(quantite + " " + produit.getNom() + " ajouté(s) à la commande.");
        } else {
            System.out.println("Stock insuffisant pour " + produit.getNom());
        }
    }

    public void afficherCommande() {
        System.out.println("\nCommande de " + client.getNom() + " (ID: " + id + "):");
        produits.forEach((produit, quantite) -> System.out.println(produit.getNom() + " x " + quantite));
    }

    public int getId() {
        return id;
    }
}

// Classe principale BoutiqueEnLigne
public class BoutiqueEnLigne {
    public static Map<String, Utilisateur> utilisateurs = new HashMap<>();
    public static Map<String, Produit> produits = new HashMap<>();
    public static Map<Integer, Commande> commandes = new HashMap<>();

    public static void main(String[] args) {
        initialiserProduits();
        initialiserUtilisateurs();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez votre email: ");
        String email = scanner.nextLine();
        System.out.print("Entrez votre mot de passe: ");
        String motDePasse = scanner.nextLine();

        Utilisateur utilisateur = utilisateurs.get(email);
        if (utilisateur != null && utilisateur.verifierMotDePasse(motDePasse)) {
            utilisateur.afficherRole();
            if (utilisateur instanceof Client) {
                traiterCommande((Client) utilisateur);
            } else if (utilisateur instanceof Administrateur) {
                gererProduits((Administrateur) utilisateur);
            }
        } else {
            System.out.println("Authentification échouée.");
        }
    }

    public static void initialiserProduits() {
        produits.put("T-shirt", new Produit("T-shirt", 15.0, 100, "Vêtements"));
        produits.put("Jeans", new Produit("Jeans", 40.0, 50, "Vêtements"));
        produits.put("Sac à dos", new Produit("Sac à dos", 25.0, 30, "Accessoires"));
    }

    public static void initialiserUtilisateurs() {
        utilisateurs.put("ikram@example.com", new Client("Ikram Eljazouli", "ikram@example.com", "1234"));
        utilisateurs.put("admin@example.com", new Administrateur("Admin", "admin@example.com", "admin123"));
    }

    public static void traiterCommande(Client client) {
        Scanner scanner = new Scanner(System.in);
        Commande commande = new Commande(client);
        boolean continuer = true;

        while (continuer) {
            System.out.println("\nProduits disponibles:");
            produits.forEach((nom, produit) -> System.out.println(produit));
            System.out.print("Entrez le nom du produit que vous souhaitez ajouter (ou 'fin' pour terminer): ");
            String nomProduit = scanner.nextLine();
            if (nomProduit.equals("fin")) {
                continuer = false;
            } else {
                Produit produit = produits.get(nomProduit);
                if (produit != null) {
                    System.out.print("Entrez la quantité: ");
                    int quantite = scanner.nextInt();
                    scanner.nextLine();
                    commande.ajouterProduit(produit, quantite);
                } else {
                    System.out.println("Produit non trouvé.");
                }
            }
        }

        commande.afficherCommande();
        commandes.put(commande.getId(), commande);
    }

    public static void gererProduits(Administrateur administrateur) {
        Scanner scanner = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            System.out.println("\nOptions administrateur:");
            System.out.println("1. Ajouter un produit");
            System.out.println("2. Supprimer un produit");
            System.out.println("3. Quitter");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.print("Nom du produit: ");
                    String nom = scanner.nextLine();
                    System.out.print("Prix: ");
                    double prix = scanner.nextDouble();
                    System.out.print("Stock: ");
                    int stock = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Catégorie: ");
                    String categorie = scanner.nextLine();
                    administrateur.ajouterProduit(produits, nom, prix, stock, categorie);
                    break;
                case 2:
                    System.out.print("Nom du produit à supprimer: ");
                    String nomProduitASupprimer = scanner.nextLine();
                    administrateur.supprimerProduit(produits, nomProduitASupprimer);
                    break;
                case 3:
                    continuer = false;
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }
}
