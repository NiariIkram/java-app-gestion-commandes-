//Ikram NIARI 20236097
//Eya ZARDI 20234306 
//2ème année LDD SDV-INFO 
//Projet 4 livraison de pizza  

// Importation de toutes les classes utilitaires de Java (Scanner, List, Map, etc.)
import java.util.*;


// Classe Pizza

// Représente une pizza du menu, avec un nom, une liste d'ingrédients et un prix de base
class Pizza {
    private String nom;
    private List<String> ingredients;
    private float prixBase;

    public Pizza(String nom, List<String> ingredients, float prixBase) {
        this.nom = nom;
        this.ingredients = ingredients;
        this.prixBase = prixBase;
    }

    public String getNom() {
        return nom;
    }

    // Calcule le prix final selon la taille de la pizza
    public float calculerPrix(String taille) {
        switch (taille.toUpperCase()) {
            case "NAINE": return prixBase * 0.67f;
            case "HUMAINE": return prixBase;
            case "OGRESSE": return prixBase * 1.33f;
            default: throw new IllegalArgumentException("Taille invalide : " + taille);
        }
    }
}


// Classe Client

// Représente un client avec un numéro de téléphone, un nom, un solde et un compteur de pizzas achetées
class Client {
    private String telephone;
    private String nom;
    private float soldeCompte;
    private int nombrePizzasAchetees;

    public Client(String telephone, String nom, float soldeCompte) {
        this.telephone = telephone;
        this.nom = nom;
        this.soldeCompte = soldeCompte;
        this.nombrePizzasAchetees = 0;
    }

    public String getTelephone() {
        return telephone;
    }

    public boolean verifierSolde(float montant) {
        return soldeCompte >= montant;
    }

    public void deduireSolde(float montant) {
        soldeCompte -= montant;
    }

    public void incrementerPizzasAchetees(int quantite) {
        nombrePizzasAchetees += quantite;
    }

    // Si le client a acheté au moins 10 pizzas, il peut avoir une pizza gratuite
    public boolean aDroitPizzaGratuite() {
        if (nombrePizzasAchetees >= 10) {
            nombrePizzasAchetees -= 10;
            return true;
        }
        return false;
    }

    public int getNombrePizzasAchetees() {
        return nombrePizzasAchetees;
    }
}


// Classe LigneCommande

// Représente une ligne de commande : une pizza, une taille et une quantité
class LigneCommande {
    private Pizza pizza;
    private String taille;
    private int quantite;

    public LigneCommande(Pizza pizza, String taille, int quantite) {
        this.pizza = pizza;
        this.taille = taille;
        this.quantite = quantite;
    }

    public float calculerPrixTotal() {
        return pizza.calculerPrix(taille) * quantite;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public int getQuantite() {
        return quantite;
    }
}


// Classe Commande

// Gère une commande complète pour un client (plusieurs lignes de pizzas)
class Commande {
    private static int compteur = 1;
    private int idCommande;
    private Client client;
    private List<LigneCommande> lignesCommande;
    private float prixTotal;

    public Commande(Client client) {
        this.idCommande = compteur++;
        this.client = client;
        this.lignesCommande = new ArrayList<>();
        this.prixTotal = 0;
    }

    public void ajouterLigne(LigneCommande ligne) {
        lignesCommande.add(ligne);
        prixTotal += ligne.calculerPrixTotal();
    }

    // Applique une réduction si le client a droit à une pizza gratuite
    public void appliquerPizzaGratuite() {
        if (client.aDroitPizzaGratuite() && !lignesCommande.isEmpty()) {
            prixTotal -= lignesCommande.get(0).calculerPrixTotal() / lignesCommande.get(0).getQuantite();
            System.out.println("→ Félicitations ! Une pizza gratuite vous a été offerte grâce à votre fidélité.");
        }
    }

    // Si la livraison dépasse 30 minutes, la commande devient gratuite
    public void verifierLivraison(int tempsLivraison) {
        if (tempsLivraison > 30) {
            prixTotal = 0;
        }
    }

    public float getPrixTotal() {
        return prixTotal;
    }

    public List<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public Client getClient() {
        return client;
    }
}


// Classe Paiement

// Gère le paiement d'une commande
class Paiement {
    private float montant;
    private Date date;
    private Commande commande;

    public Paiement(float montant, Date date, Commande commande) {
        this.montant = montant;
        this.date = date;
        this.commande = commande;
    }

    // Effectue le paiement si le client a assez d'argent
    public boolean effectuerPaiement() {
        Client client = commande.getClient();
        if (client.verifierSolde(montant)) {
            client.deduireSolde(montant);
            System.out.println("→ Paiement de " + montant + "€ effectué le " + date);
            return true;
        } else {
            System.out.println("→ Paiement échoué : solde insuffisant.");
            return false;
        }
    }
}


// Classe Livreur

// Représente un livreur avec un nom et un véhicule
class Livreur {
    private int idLivreur;
    private String nom;
    private String vehicule;

    public Livreur(int idLivreur, String nom, String vehicule) {
        this.idLivreur = idLivreur;
        this.nom = nom;
        this.vehicule = vehicule;
    }

    public void livrer(Commande commande, int minutes) {
        commande.verifierLivraison(minutes);
        System.out.println("→ Commande livrée par " + nom + " en " + minutes + " minutes.");
    }
}


// Classe RestoRaPizza

// Représente le restaurant et les pizzas qu’il propose
class RestoRaPizza {
    private int id;
    private String adresse;
    private List<Pizza> pizzasDisponibles;

    public RestoRaPizza(int id, String adresse, List<Pizza> pizzasDisponibles) {
        this.id = id;
        this.adresse = adresse;
        this.pizzasDisponibles = pizzasDisponibles;
    }

    // Recherche d'une pizza dans le menu par son nom
    public Pizza trouverPizzaParNom(String nom) {
        for (Pizza pizza : pizzasDisponibles) {
            if (pizza.getNom().equalsIgnoreCase(nom)) {
                return pizza;
            }
        }
        return null;
    }
}


// Classe BaseClients

// Représente la base de données des clients (simulée avec une HashMap)
class BaseClients {
    private Map<String, Client> clients;

    public BaseClients() {
        clients = new HashMap<>();
    }

    public void ajouterClient(Client client) {
        clients.put(client.getTelephone(), client);
    }

    public Client trouverClient(String telephone) {
        return clients.get(telephone);
    }
}


// Classe Main 

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Création des clients
        BaseClients baseClients = new BaseClients();
        baseClients.ajouterClient(new Client("0612345678", "Jean Dupont", 150.0f));
        baseClients.ajouterClient(new Client("0624681357", "Sara Martin", 30.0f));
        baseClients.ajouterClient(new Client("0613579246", "Lucas Petit", 50.0f));

        // Création du menu des pizzas
        List<Pizza> catalogue = Arrays.asList(
            new Pizza("Margarita", Arrays.asList("Tomate", "Mozzarella", "Basilic"), 10.0f),
            new Pizza("Pepperoni", Arrays.asList("Tomate", "Mozzarella", "Pepperoni"), 12.0f)
        );
        RestoRaPizza resto = new RestoRaPizza(1, "5 rue des pizzas", catalogue);

        // Identification du client
        System.out.print("Entrez votre numéro de téléphone: ");
        String telephone = scanner.nextLine();
        Client client = baseClients.trouverClient(telephone);

        if (client == null) {
            System.out.println("→ Client non trouvé.");
            return;
        }

        // Création d'une commande
        Commande commande = new Commande(client);
        System.out.println("Composez votre commande :");

        // Boucle de commande
        boolean continuerCommande = true;
        while (continuerCommande) {
            System.out.print("Nom de pizza (Margarita / Pepperoni): ");
            String nomPizza = scanner.nextLine();

            Pizza pizza = resto.trouverPizzaParNom(nomPizza);
            if (pizza == null) {
                System.out.println("→ Pizza non disponible !");
                continue;
            }

            System.out.print("Taille (NAINE / HUMAINE / OGRESSE): ");
            String taille = scanner.nextLine();

            System.out.print("Quantité: ");
            int quantite = Integer.parseInt(scanner.nextLine());

            commande.ajouterLigne(new LigneCommande(pizza, taille, quantite));

            System.out.print("Ça sera tout ? (oui/non): ");
            String reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("oui")) {
                continuerCommande = false;
            }
        }

        // Si aucune pizza n'est commandée
        if (commande.getLignesCommande().isEmpty()) {
            System.out.println("→ Aucune pizza commandée.");
            return;
        }

        // Simulation de la livraison AVANT paiement
        Livreur livreur = new Livreur(1, "Paul Martin", "Moto");
        int tempsLivraison = new Random().nextInt(36) + 10; // aléatoire entre 10 et 45 min
        livreur.livrer(commande, tempsLivraison);

        // Paiement uniquement si la commande est payante (pas offerte)
        if (commande.getPrixTotal() > 0) {
            Paiement paiement = new Paiement(commande.getPrixTotal(), new Date(), commande);
            if (!paiement.effectuerPaiement()) {
                return;
            }
        } else {
            System.out.println("→ Livraison en retard ! Commande offerte.");
        }

        // Mise à jour du compteur de fidélité
        int totalPizzas = commande.getLignesCommande().stream().mapToInt(lc -> lc.getQuantite()).sum();
        client.incrementerPizzasAchetees(totalPizzas);
        commande.appliquerPizzaGratuite();

        // Affichage final de la commande validée avec son prix total
        System.out.println("→ Commande validée. Prix final: " + commande.getPrixTotal() + "€.");
        // Affichage du nombre de pizzas restantes avant la prochaine pizza gratuite
        int restantes = 10 - client.getNombrePizzasAchetees();
        System.out.println("→ Continuez à commander, plus que " + restantes + " pizza(s) avant une gratuite !");
    }
}
