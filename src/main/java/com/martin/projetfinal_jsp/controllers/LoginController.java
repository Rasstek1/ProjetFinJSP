package com.martin.projetfinal_jsp.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/inscription")
    public String showInscriptionPage() {
        // Affiche la page d'inscription (inscription.jsp)
        return "inscription";
    }

    @PostMapping("/inscription")
    public String processInscription(Model model /* Ajoutez les paramètres de formulaire ici */) {
        // Traitement de l'inscription, par exemple, enregistrer le nouvel utilisateur dans la base de données
        // Rediriger vers la page de connexion ou afficher un message de succès
        return "redirect:/login";
    }

    @GetMapping("/connexion")
    public String showLoginPage() {
        // Affiche la page de connexion (login.jsp)
        return "login";
    }

    @GetMapping("/deconnexion")
    public String logout() {
        // Effectue la déconnexion de l'utilisateur, généralement géré par Spring Security
        return "redirect:/login?logout";
    }

    @GetMapping("/changerMotDePasse")
    public String showChangerMotDePassePage() {
        // Affiche la page de changement de mot de passe (changerMotDePasse.jsp)
        return "changerMotDePasse";
    }

    @PostMapping("/changerMotDePasse")
    public String processChangerMotDePasse(/* Ajoutez les paramètres de formulaire ici */) {
        // Traitement du changement de mot de passe, par exemple, mise à jour du mot de passe dans la base de données
        // Rediriger vers la page d'accueil ou afficher un message de succès
        return "redirect:/accueil";
    }
}
