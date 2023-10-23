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
            String fileName = "chalet" + numChalet + "Main." + getFileExtension(originalFilename);

            try {
                saveFile(fileName, mainPhoto);
                chaletDbContext.insertPhoto(numChalet, fileName, true); // true indique que c'est la photo principale
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Une erreur s'est produite lors de la sauvegarde de la photo principale.");
                return "ajouterChalet";
            }
        }

        int counter = 1;
        for(MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                String originalFilename = photo.getOriginalFilename();
                String fileName = "chalet" + numChalet + "_" + counter + getFileExtension(originalFilename);

                try {
                    saveFile(fileName, photo);
                    chaletDbContext.insertPhoto(numChalet, fileName, false); // false car ce n'est pas la photo principale
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
