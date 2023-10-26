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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.martin.projetfinal_jsp.models.Client;
import com.martin.projetfinal_jsp.models.MyAuthenticationProvider;
import com.martin.projetfinal_jsp.models.MyUserManager;


import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    MyAuthenticationProvider myprovider;

    @Autowired
    @Qualifier("userManagerBean") // Utilisez le nom correct de la méthode
    private MyUserManager usermanager;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @RequestMapping(value="/connexion", method = RequestMethod.GET)
    public String login(@RequestParam(value="loginError", required=false, defaultValue="false") String loginError) {
        if (loginError.equals("true")) {
            return "connexion?loginError=true";
        } else {
            return "connexion";
        }
    }

    @RequestMapping(value="/accueil", method = RequestMethod.POST)
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

            if (savedRequest == null) {
                return "redirect:/accueil";
            } else {
                return "redirect:" + savedRequest.getRedirectUrl();
            }
        } catch (AuthenticationException ex) {
            HttpSession session = req.getSession(true);
            session.setAttribute("LoginErrorMessage", ex.getMessage());
            return "redirect:/connexion?loginError=true";
        } catch (Exception ex) {
            HttpSession session = req.getSession(true);
            session.setAttribute("LoginErrorMessage", ex.getMessage());
            return "redirect:/connexion?loginError=true";
        }
    }

    @RequestMapping(value="/login/logout", method = RequestMethod.POST)
    public String logout (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // Supprimer le jeton d'authentification
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/home/accueil";
    }

    @RequestMapping(value="/login/register", method = RequestMethod.GET)
    public String register(@RequestParam(value="registerError", required=false, defaultValue="false") String registerError) {
        if (registerError.equals("true")) {
            return "inscription?registerError=true";
        } else {
            return "inscription";
        }
    }

    @RequestMapping(value="/login/register", method = RequestMethod.POST)
    public String register(HttpServletRequest req, HttpSession session) {
        try {
            String nom = req.getParameter("nom");
            String prenom = req.getParameter("prenom");
            String adresse = req.getParameter("adresse");
            String telephone = req.getParameter("telephone");
            String courriel = req.getParameter("courriel");
            String motPasse = req.getParameter("password"); // Assurez-vous que "password" correspond au nom du champ dans le formulaire

            // Attribuer le rôle ROLE_USER par défaut au nouveau client
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            // Créez un nouvel objet Client avec toutes les propriétés
            Client client = new Client(nom, prenom, adresse, telephone, courriel, motPasse, true, authorities);

            // Utilisez le gestionnaire d'utilisateurs pour créer le nouvel utilisateur
            this.usermanager.createUser(client);
            return "redirect:/home/accueil";
        } catch (IncorrectResultSizeDataAccessException ex) {
            session.setAttribute("registerErrorMessage", ex.getMessage());
            return "redirect:/inscription?registerError=true";
        }
    }

}
