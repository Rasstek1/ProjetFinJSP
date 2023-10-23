package com.martin.projetfinal_jsp.controllers;

import com.martin.projetfinal_jsp.data.ChaletDbContext;
import com.martin.projetfinal_jsp.models.Chalet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.io.File;
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


    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex >= 0) {
            return filename.substring(dotIndex);
        }
        return "";
    }

    private void saveFile(String filename, MultipartFile file) throws IOException {
        String uploadDirectory = "src/main/resources/static/uploads/";
        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        Path path = Paths.get(uploadDirectory, filename);
        byte[] bytes = file.getBytes();
        Files.write(path, bytes);
    }

    @PostMapping("/ajouterChalet")
    public String ajouterChalet(@ModelAttribute Chalet chalet,
                                @RequestParam("mainPhoto") MultipartFile mainPhoto,
                                @RequestParam("photos") MultipartFile[] photos, Model model) throws IOException {
        System.out.println("Méthode ajouterChalet appelée.");

        String description = HtmlUtils.htmlEscape(chalet.getDescription());

        int numChalet = chaletDbContext.insertChalet(chalet);

        // Sauvegarder la photo principale et insérer le nom du fichier dans la table photos
        if (!mainPhoto.isEmpty()) {
            String originalFilename = mainPhoto.getOriginalFilename();
            // Modification ici: changement du nommage de la photo principale
            String fileName = "chalet" + numChalet + "_1" + getFileExtension(originalFilename);
            chalet.setPhotoPrincipale(fileName);

            try {
                saveFile(fileName, mainPhoto);
                chaletDbContext.insertPhoto(numChalet, fileName); // Pas besoin de 'true' ou 'false' ici
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Une erreur s'est produite lors de la sauvegarde de la photo principale.");
                return "ajouterChalet";
            }
        }

        int counter = 2; // Modification ici: débuter le compteur à 2 car _1 est déjà utilisé pour la photo principale
        for(MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                String originalFilename = photo.getOriginalFilename();
                String fileName = "chalet" + numChalet + "_" + counter + getFileExtension(originalFilename);

                try {
                    saveFile(fileName, photo);
                    chaletDbContext.insertPhoto(numChalet, fileName); // Pas besoin de 'true' ou 'false' ici
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("errorMessage", "Une erreur s'est produite lors de la sauvegarde des photos.");
                    return "ajouterChalet";
                }
                counter++;
            }
        }

        return "redirect:/listeChalets";
    }






}
