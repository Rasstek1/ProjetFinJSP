package com.martin.projetfinal_jsp.controllers;

import com.martin.projetfinal_jsp.data.ChaletDbContext;
import com.martin.projetfinal_jsp.models.Chalet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ChaletDbContext chaletDbContext;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";


    // Ajouter un chalet
    @GetMapping("/ajouterChalet")
    public String ajouterChaletForm(Model model) {
        model.addAttribute("chalet", new Chalet());
        return "administration";
    }


    @PostMapping("/ajouterChalet")
    public String ajouterChalet(@ModelAttribute Chalet chalet,
                                @RequestParam("mainPhoto") MultipartFile mainPhoto,
                                @RequestParam("photos") MultipartFile[] photos, Model model) throws IOException {
        System.out.println("Méthode ajouterChalet appelée.");

        // Échappe les caractères HTML dans les champs pour des raisons de sécurité
        String description = HtmlUtils.htmlEscape(chalet.getDescription());

        // TODO: Ajouter une vérification similaire à l'ISBN pour le chalet si nécessaire

        // Insérer le chalet dans la base de données et récupérer le numéro de chalet généré
        int numChalet = chaletDbContext.insertChalet(chalet);

        // Sauvegarder la photo principale et insérer le nom du fichier dans la table photos
        if (!mainPhoto.isEmpty()) {
            String originalFilename = mainPhoto.getOriginalFilename();
            String safeFilename = generateUniqueFileName(originalFilename);
            String imagePath = UPLOAD_DIR + safeFilename;

            try {
                saveFile(safeFilename, mainPhoto);
                chaletDbContext.insertPhoto(numChalet, safeFilename);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Une erreur s'est produite lors de la sauvegarde de la photo principale.");
                return "ajouterChalet";  // Retour à la même page avec le message d'erreur
            }
        }

        // Sauvegarder les photos supplémentaires et insérer leurs noms dans la table photos
        char suffix = 'a';
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                String originalFilename = photo.getOriginalFilename();
                String fileName = numChalet + suffix + "." + getFileExtension(originalFilename);
                try {
                    saveFile(fileName, photo);
                    chaletDbContext.insertPhoto(numChalet, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("errorMessage", "Une erreur s'est produite lors de la sauvegarde des photos supplémentaires.");
                    return "ajouterChalet";  // Retour à la même page avec le message d'erreur
                }

                if (suffix < 'z') suffix++;  // incrémenter le suffixe pour la prochaine photo
            }
        }

        return "redirect:/listeChalets";
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void saveFile(String fileName, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.write(path, bytes);
    }

    // Méthode pour générer un nom de fichier unique
    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }





}
