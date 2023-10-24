package com.martin.projetfinal_jsp.controllers;
import com.martin.projetfinal_jsp.models.Chalet;

import com.martin.projetfinal_jsp.models.EmailService;
import org.springframework.web.bind.annotation.RequestParam;

import com.martin.projetfinal_jsp.data.ChaletDbContext;
import com.martin.projetfinal_jsp.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class ChaletController {

    @Autowired
    private ChaletDbContext chaletDbContext;


    @Autowired
    private EmailService emailService;

    // 1) Accueil
    @GetMapping("/accueil")
    public String accueil() {
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
        model.addAttribute("chalet", chaletDbContext.selectChaletByNumero(numChalet));
        return "reservation";
    }

    // 4) Confirmer la réservation
    @PostMapping("/confirmReserverChalet")
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
    }


    @GetMapping("/photo")
    public String showPhotoDetails(@RequestParam("numChalet") int numChalet, Model model) {
        // Récupérez les détails du chalet en utilisant numChalet
        Chalet chalet = chaletDbContext.selectChaletByNumero(numChalet);
        System.out.println("Chalet: " + chalet);
        System.out.println("Photos: " + chalet.getListePhotos());

        if(chalet == null) {
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
