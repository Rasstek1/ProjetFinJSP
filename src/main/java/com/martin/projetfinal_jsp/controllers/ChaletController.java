package com.martin.projetfinal_jsp.controllers;
import com.martin.projetfinal_jsp.models.Chalet;

import org.springframework.web.bind.annotation.RequestParam;

import com.martin.projetfinal_jsp.data.ChaletDbContext;
import com.martin.projetfinal_jsp.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChaletController {

    @Autowired
    private ChaletDbContext chaletDbContext;

    // 1) Accueil
    @GetMapping("/accueil")
    public String accueil() {
        return "accueil";
    }

    // 2) Liste des chalets
    @GetMapping("/listeChalets")
    public String listeChalets(Model model) {
        model.addAttribute("chalets", chaletDbContext.selectAllChalets());
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

        // Ici, ajoutez le code pour envoyer le courriel de confirmation

        return "Confirmation";
    }

    @GetMapping("/photo")
    public String showPhotoDetails(@RequestParam("numChalet") int numChalet, Model model) {
        // Récupérez les détails du chalet en utilisant numChalet
        Chalet chalet = chaletDbContext.selectChaletByNumero(numChalet);

        if(chalet == null) {
            // Handle the error, e.g., redirect to an error page or set an error message
            return "error";  // Change to the appropriate view name for your error page
        }

        // Ajoutez les détails du chalet au modèle
        model.addAttribute("chalet", chalet);

        // Retournez le nom de la vue JSP
        return "photo";
    }

}
