package com.martin.projetfinal_jsp.controllers;

import com.martin.projetfinal_jsp.data.ChaletDbContext;
import com.martin.projetfinal_jsp.models.Chalet;
import com.martin.projetfinal_jsp.models.Client;
import com.martin.projetfinal_jsp.models.EmailService;
import com.martin.projetfinal_jsp.models.Reservation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;



@Controller
public class ChaletController {

    @Autowired
    private ChaletDbContext chaletDbContext;

    @Autowired
    private EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(ChaletController.class);
    // 1) Accueil
    @GetMapping("/accueil")
    public String accueil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authentication Object: " + authentication);

        if (authentication.getPrincipal() instanceof Client) {
            Client client = (Client) authentication.getPrincipal();
            logger.info("Client Object: " + client.toString());
        }

        return "accueil";
    }

    // 2) Liste des chalets
    @GetMapping("/listeChalets")
    public String listeChalets(Model model) {
        List<Map<String, Object>> chaletsMapList = chaletDbContext.selectAllChalets();
        List<Chalet> chalets = new ArrayList<>();
        int maxLength = 100; // Limite de longueur pour la description

        for (Map<String, Object> map : chaletsMapList) {
            Chalet chalet = new Chalet();
            chalet.setNumChalet((int) map.get("numChalet"));
            chalet.setNombreChambres((int) map.get("nombreChambres"));
            String description = (String) map.get("description");

            // Tronquer la description si elle dépasse la longueur maximale
            if (description.length() > maxLength) {
                description = description.substring(0, maxLength) + "...";
            }

            chalet.setDescription(description);
            chalet.setPrix((BigDecimal) map.get("prix"));

            // Récupérez les photos pour ce chalet
            List<String> photos = chaletDbContext.selectPhotosForChalet(chalet.getNumChalet());
            chalet.setListePhotos(photos);

            // Ajoutez le chalet à la liste :
            chalets.add(chalet);
        }

        model.addAttribute("chalets", chalets);
        return "listeChalets";
    }


    // 3) Réserver un chalet
    @GetMapping("/reserverChalet/{numChalet}")
    public String reserverChalet(@PathVariable int numChalet, Model model) {
        // Récupérez les détails de l'utilisateur actuellement authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Client client = (Client) userDetails;
            String prenom = client.getPrenom();
            String courriel = client.getCourriel();
            String telephone = client.getTelephone();  // Assurez-vous que cette variable est bien déclarée dans votre classe Client

            // Log des informations
            logger.info("Informations complètes du Client :");
            logger.info("Nom : " + client.getNom());
            logger.info("Prénom : " + client.getPrenom());
            logger.info("Adresse : " + client.getAdresse());
            logger.info("Téléphone : " + client.getTelephone());
            logger.info("Courriel : " + client.getCourriel());




            model.addAttribute("nomClient", prenom);  // Utilisez 'prenom' pour remplir 'nomClient'
            model.addAttribute("telephone", telephone);
            model.addAttribute("courriel", courriel);
        }

        // Maintenant, vous pouvez créer une nouvelle instance de Reservation avec les valeurs par défaut
        Reservation reservation = new Reservation();
        reservation.setNumChalet(numChalet);
        reservation.setPrix(BigDecimal.ZERO); // Vous pouvez définir une valeur par défaut appropriée
        // Définissez d'autres valeurs par défaut pour votre réservation ici

        model.addAttribute("reservation", reservation);

        model.addAttribute("chalet", chaletDbContext.selectChaletByNumero(numChalet));
        return "reservation";
    }




    // 4) Confirmer la réservation
    @PostMapping("/confirmation")
    public String confirmReserverChalet(Reservation reservation, Model model) {
        // Calcul de la date de début et de fin basée sur dateLocation et duree
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


    @GetMapping("/photo")
    public String showPhotoDetails(@RequestParam("numChalet") int numChalet, Model model) {
        // Récupérez les détails du chalet en utilisant numChalet
        Chalet chalet = chaletDbContext.selectChaletByNumero(numChalet);
        System.out.println("Chalet: " + chalet);
        System.out.println("Photos: " + chalet.getListePhotos());

        if (chalet == null) {
            // Handle the error, e.g., redirect to an error page or set an error message
            return "error";  // Change to the appropriate view name for your error page
        }

        // Ajoutez les détails du chalet et la liste des photos au modèle
        model.addAttribute("chalet", chalet);
        model.addAttribute("photos", chalet.getListePhotos());
        model.addAttribute("test", "Ceci est un test");
        System.out.println("Type de la liste des photos : " + chalet.getListePhotos().getClass().getName());
        // Retournez le nom de la vue JSP
        return "photo";
    }


}


// 4) Confirmer la réservation
   /* @PostMapping("/confirmation")
    public String confirmReserverChalet(Reservation reservation, Model model) {
        int numReservation = chaletDbContext.insertReservation(reservation);
        model.addAttribute("numReservation", numReservation);

       // Créez une instance d'EmailService (vous pouvez l'injecter si elle est gérée par Spring)
        EmailService emailService = new EmailService();

        // Configurez l'e-mail de confirmation
        String destinataire = "adresse@example.com"; // Remplacez par l'adresse de l'utilisateur
        String sujet = "Confirmation de réservation";
        String contenu = "Votre réservation a été confirmée. Numéro de réservation : " + numReservation;

        try {
            // Envoyez l'e-mail de confirmation
            emailService.sendEmail(destinataire, sujet, contenu);
            model.addAttribute("emailSent", true);
        } catch (Exception e) {
            model.addAttribute("emailSent", false);
            e.printStackTrace();
        }

        return "Confirmation";
    }*/