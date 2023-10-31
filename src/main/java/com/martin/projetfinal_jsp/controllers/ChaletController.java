package com.martin.projetfinal_jsp.controllers;

import com.martin.projetfinal_jsp.data.ChaletDbContext;
import com.martin.projetfinal_jsp.models.Chalet;
import com.martin.projetfinal_jsp.models.Client;
import com.martin.projetfinal_jsp.models.EmailService;
import com.martin.projetfinal_jsp.models.Reservation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;



@Controller
public class ChaletController {

    @Autowired
    private ChaletDbContext chaletDbContext;

    @Autowired
    private EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(ChaletController.class);

    // Page d'accueil
    @GetMapping("/accueil")
    public String accueil() {
        // Obtient l'objet d'authentification de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authentication Object: " + authentication);

        // Vérifie si l'utilisateur est un client et affiche ses informations
        if (authentication.getPrincipal() instanceof Client) {
            Client client = (Client) authentication.getPrincipal();
            logger.info("Client Object: " + client.toString());
        }

        return "accueil";
    }

    // Liste des chalets
    @GetMapping("/listeChalets")
    public String listeChalets(Model model) {
        // Récupère la liste de tous les chalets depuis la base de données
        List<Map<String, Object>> chaletsMapList = chaletDbContext.selectAllChalets();//
        List<Chalet> chalets = new ArrayList<>();
        int maxLength = 100; // Limite de longueur pour la description a 100 caractères

        // Parcourt la liste des chalets et crée des objets Chalet pour chaque chalet
        for (Map<String, Object> map : chaletsMapList) {
            Chalet chalet = new Chalet();
            chalet.setNumChalet((int) map.get("numChalet"));// fait le cast de l'objet map en int
            chalet.setNombreChambres((int) map.get("nombreChambres"));// fait le cast de l'objet map en int
            String description = (String) map.get("description");// fait le cast de l'objet map en String

            // Coupe la description si elle dépasse la longueur maximale
            if (description.length() > maxLength) {
                description = description.substring(0, maxLength) + "...";
            }

            chalet.setDescription(description);//Recupere la description du chalet
            chalet.setPrix((BigDecimal) map.get("prix")); //Recupere le prix du chalet

            // Récupère les photos associées à ce chalet
            List<String> photos = chaletDbContext.selectPhotosForChalet(chalet.getNumChalet());
            chalet.setListePhotos(photos);

            // Ajoute le chalet à la liste
            chalets.add(chalet);
        }

        model.addAttribute("chalets", chalets);
        return "listeChalets";
    }

    // Page de réservation d'un chalet
    @GetMapping("/reserverChalet/{numChalet}")
    public String reserverChalet(@PathVariable int numChalet, Model model) {
        // Récupère les détails de l'utilisateur actuellement authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Client client = (Client) userDetails; // Cast l'objet UserDetails en Client
            String prenom = client.getPrenom(); // Récupère le prénom du client
            String courriel = client.getCourriel(); // Récupère le courriel du client
            String telephone = client.getTelephone();  // Récupère le numéro de téléphone du client

            // Ajoute les informations du client au modèle
            model.addAttribute("nomClient", prenom);
            model.addAttribute("telephone", telephone);
            model.addAttribute("courriel", courriel);

            // Crée une nouvelle instance de réservation avec les valeurs par défaut
            Reservation reservation = new Reservation();
            reservation.setNumChalet(numChalet);
            reservation.setPrix(BigDecimal.ZERO); // Assurez-vous que votre classe Reservation a une méthode setPrix

            // Assigne le numéro de téléphone du client à la réservation
            reservation.setTelephone(telephone);

            model.addAttribute("reservation", reservation);
        }

        // Ajoute les détails du chalet au modèle
        model.addAttribute("chalet", chaletDbContext.selectChaletByNumero(numChalet));
        return "reservation";
    }



    // Confirmation de la réservation d'un chalet
    @PostMapping("/confirmation")
    public String confirmReserverChalet(@ModelAttribute("reservation") @Valid Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // En cas d'erreur de validation, capture le numéro de chalet
            int numChalet = reservation.getNumChalet();

            // Redirige l'utilisateur vers la page de réservation avec le numéro de chalet en tant que paramètre
            return "redirect:/reserverChalet/" + numChalet + "?error=true";
        }

        // Calcul de la date de début et de fin basée sur dateLocation et duree
        //Partie rajoutée par pour calculer la date de fin et empecher le chevauchement de date de reservation
        Date startDate = reservation.getStartDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, reservation.getDuree());
        Date endDate = calendar.getTime();

        // Vérifiez la disponibilité du chalet
        if (!chaletDbContext.isAvailable(reservation.getNumChalet(), startDate, endDate)) {
            model.addAttribute("availability", false);

            // Pour conserver les données du formulaire lors du retour à la vue en cas de non disponibilité
            model.addAttribute("nomClient", reservation.getNomClient());
            model.addAttribute("telephone", reservation.getTelephone());
            model.addAttribute("courriel", reservation.getCourriel());
            return "reservation"; // Retourne à la même vue avec un message d'indisponibilité
        }

        // Insérez la réservation dans la base de données
        int numReservation = chaletDbContext.insertReservation(reservation);

        System.out.println("Date de début : " + reservation.getStartDate());

        if (numReservation <= 0) {
            model.addAttribute("availability", false);
            return "reservation"; // Quelque chose a mal tourné pendant l'insertion
        }

        // Récupérez le numéro du chalet et le prix à partir de la réservation
        int numChalet = reservation.getNumChalet();
        BigDecimal prix = reservation.getPrix();

        // Récupérez l'adresse e-mail du destinataire à partir de la réservation
        String destinataire = reservation.getCourriel();

        // Initialisez les variables sujet et contenu avec les valeurs appropriées
        String sujet = "Confirmation de réservation";
        String contenu = "Votre réservation a été confirmée. Numéro de réservation : " + numReservation;

        model.addAttribute("numReservation", numReservation);

        try {
            // Envoyez l'e-mail de confirmation
            emailService.sendEmail(destinataire, sujet, contenu);
            model.addAttribute("emailSent", true);
        } catch (Exception e) {
            model.addAttribute("emailSent", false);
            e.printStackTrace();
        }

        return "confirmation";
    }

    // Afficher les détails d'une photo
    @GetMapping("/photo")
    public String showPhotoDetails(@RequestParam("numChalet") int numChalet, Model model) {
        // Récupérez les détails du chalet en utilisant numChalet
        Chalet chalet = chaletDbContext.selectChaletByNumero(numChalet);
        System.out.println("Chalet: " + chalet);
        System.out.println("Photos: " + chalet.getListePhotos());

        if (chalet == null) {
            // Gérez l'erreur,
            return "error";
        }

        // Ajoutez les détails du chalet et la liste des photos au modèle
        model.addAttribute("chalet", chalet);
        model.addAttribute("photos", chalet.getListePhotos());
        model.addAttribute("test", "Ceci est un test");
        System.out.println("Type de la liste des photos : " + chalet.getListePhotos().getClass().getName());


        return "photo";
    }
}

