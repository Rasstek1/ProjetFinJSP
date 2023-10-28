package com.martin.projetfinal_jsp.controllers;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.martin.projetfinal_jsp.models.Client;
import com.martin.projetfinal_jsp.models.MyAuthenticationProvider;
import com.martin.projetfinal_jsp.models.MyUserManager;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    MyAuthenticationProvider myprovider;

    @Autowired
    @Qualifier("userManagerBean")
    private MyUserManager usermanager;

    @Autowired
    SecurityContextRepository securityContextRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }



    @GetMapping("/connexion")
    public String login(@RequestParam(value = "courriel", required = false) String courriel,
                        @RequestParam(value = "motPasse", required = false) String motPasse,
                        @RequestParam(value="loginError", required=false, defaultValue="false") String loginError,
                        Model model) {
        if (courriel != null && motPasse != null) {
            // Si les informations de courriel et de mot de passe sont présentes, pré-remplissez-les dans le modèle
            model.addAttribute("courriel", courriel);
            model.addAttribute("motPasse", motPasse);
        }

        if (loginError.equals("true")) {
            // Si une erreur de connexion est détectée, ajoutez un attribut d'erreur dans le modèle
            model.addAttribute("loginError", true);
        }

        return "connexion";
    }


    @RequestMapping(value="/connexion", method = RequestMethod.POST)
    public String login(HttpServletRequest req, HttpServletResponse response) {
        SecurityContext securitycontext = SecurityContextHolder.createEmptyContext();
        try {
            // Créer un client à partir d'un nom d'utilisateur et d'un mot de passe
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            Client client = new Client(username, password, new ArrayList<>());
            Authentication token =  new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());

            // Valider le jeton d'authentification si possible
            Authentication aut = myprovider.authenticate(token);
            // Sauvegarder le token dans la session
            securitycontext.setAuthentication(aut);
            securityContextRepository.saveContext(securitycontext, req, response);

            // Lire l'URL d'origine pour une redirection après une authentification réussie
            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(req, response);

            if (aut.isAuthenticated()) {
                // Si l'authentification réussit, redirigez vers la page d'accueil
                return "redirect:/accueil";
            } else {
                // Sinon, redirigez vers la page de connexion avec un message d'erreur
                return "redirect:/connexion?loginError=true";
            }
        } catch (AuthenticationException ex) {
            // Gestion de l'exception en cas d'erreur d'authentification
            HttpSession session = req.getSession(true);
            session.setAttribute("LoginError", ex.getMessage());
            // En cas d'échec de la connexion, restez sur la page de connexion avec un message d'erreur
            return "redirect:/connexion?loginError=true";
        } catch (Exception ex) {
            // Gestion de l'exception générale
            HttpSession session = req.getSession(true);
            session.setAttribute("LoginError", ex.getMessage());
            // En cas d'échec de la connexion, restez sur la page de connexion avec un message d'erreur
            return "redirect:/connexion?loginError=true";
        }
    }

    @GetMapping("/logoutPage")
    public String logoutPage() {
        return "logoutPage"; // Le nom de la vue correspondant à votre page de déconnexion
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // Supprimer le jeton d'authentification
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/logoutPage";
    }


    @RequestMapping(value="/login/register", method = RequestMethod.GET)
    public String register(@RequestParam(value="registerError", required=false, defaultValue="false") String registerError) {
        if (registerError.equals("true")) {
            return "inscription?registerError=true";
        } else {
            return "inscription";
        }
    }

    @RequestMapping(value="inscription", method = RequestMethod.POST)
    public String register(HttpServletRequest req, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            String courriel = req.getParameter("courriel");
            String motPasse = req.getParameter("motPasse");
            String nom = req.getParameter("nom");
            String prenom = req.getParameter("prenom");
            String adresse = req.getParameter("adresse");
            String telephone = req.getParameter("telephone");

            // Attribuer le rôle ROLE_USER par défaut au nouveau client
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            // Créez un nouvel objet Client avec toutes les propriétés
            Client client = new Client(nom, prenom, adresse, telephone, courriel, motPasse, true, authorities);

            // Utilisez le gestionnaire d'utilisateurs pour créer le nouvel utilisateur
            this.usermanager.createUser(client);

            // Ajoutez les informations de courriel et de mot de passe aux attributs de redirection
            redirectAttributes.addAttribute("courriel", courriel);
            redirectAttributes.addAttribute("motPasse", motPasse);

            // Redirigez vers la page de connexion
            return "redirect:/connexion";
        } catch (IncorrectResultSizeDataAccessException ex) {
            session.setAttribute("registerErrorMessage", ex.getMessage());
            return "redirect:/inscription?registerError=true";
        }
    }

/*************************Changement de mot de passe************************************/
    // Affiche le formulaire de changement de mot de passe
    @GetMapping("/motDePasse")
    public String afficherFormulaireChangementMotPasse() {
        return "motDePasse"; // le nom de la vue (jsp) pour le formulaire de changement de mot de passe
    }

    // Gère la soumission du formulaire de changement de mot de passe
// Gère la soumission du formulaire de changement de mot de passe
    @PostMapping("/changerMotDePasse")
    public String changerMotDePasse(@RequestParam("ancienMotDePasse") String ancienMotDePasse,
                                    @RequestParam("nouveauMotDePasse") String nouveauMotDePasse,
                                    RedirectAttributes redirectAttributes) {
        try {
            usermanager.changePassword(ancienMotDePasse, nouveauMotDePasse);
            // Redirigez l'utilisateur vers une page de succès si le changement de mot de passe réussit.
            return "redirect:/motDePasseSuccess";
        } catch (Exception e) {
            e.printStackTrace(); // Ajoutez cette ligne pour afficher les détails de l'exception dans la console.
            // En cas d'erreur, ajoutez un attribut d'erreur et redirigez l'utilisateur vers la même page "motDePasse".
            redirectAttributes.addAttribute("MPError", true);
            return "redirect:/motDePasse";
        }
    }



    // Page de succès pour le changement de mot de passe
    @GetMapping("/motDePasseSuccess")
    public String motDePasseSuccess() {
        return "motDePasseSuccess"; // Nom de la vue (jsp) pour la page de succès
    }
/*******************************************************************************************/


}
