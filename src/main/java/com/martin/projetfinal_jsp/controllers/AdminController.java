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
import java.util.ArrayList;
import java.util.List;
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

    //Methode pour la liste de chalet sur admininistration.jsp
    @GetMapping("/administration")
    public String listeChalets(Model model) {
        List<Chalet> chalets = chaletDbContext.selectAllChaletsForAdmin();
        model.addAttribute("chalets", chalets);
        return "listeChalets";
    }

    // Cette méthode récupère l'extension d'un fichier
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex >= 0) ? filename.substring(dotIndex) : "";
    }

    // Cette méthode sauvegarde un fichier dans le répertoire d'uploads
    private void saveFile(String filename, MultipartFile file) throws IOException {
        String uploadDirectory = "src/main/resources/static/uploads/";
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());
    }

    @PostMapping("/ajouterChalet")
    public String ajouterChalet(@ModelAttribute Chalet chalet,
                                @RequestParam("photos") MultipartFile[] photos, Model model) throws IOException {
        System.out.println("Méthode ajouterChalet appelée.");

        // Initialisation de la liste de photos pour le chalet
        chalet.setListePhotos(new ArrayList<>());

        chalet.setDescription(HtmlUtils.htmlEscape(chalet.getDescription()));
        int numChalet = chaletDbContext.insertChalet(chalet);

        int counter = 1; // Utilisé pour nommer les photos
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                String originalFilename = photo.getOriginalFilename();
                String fileName = "chalet" + numChalet + "_" + counter + getFileExtension(originalFilename);

                try {
                    saveFile(fileName, photo);
                    chaletDbContext.insertPhoto(numChalet, fileName);
                    chalet.getListePhotos().add(fileName);  // Ajoutez le nom de fichier à la liste de photos du chalet
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



    @PostMapping("/modifierChalet")
    public String modifierChalet(@ModelAttribute Chalet chalet,
                                 @RequestParam("photos") MultipartFile[] photos, Model model) throws IOException {
        // Vérifiez si l'utilisateur a les autorisations d'administration ici
        // Assurez-vous que la classe ChaletDbContext a une méthode updateChalet(Chalet chalet) implémentée
        chaletDbContext.updateChalet(chalet);

        // Mettez à jour les photos du chalet
        updatePhotos(chalet.getNumChalet(), photos, model);

        return "redirect:/admin/listeChalets";
    }

    private void deleteFile(String fileName) {
        String uploadDirectory = "src/main/resources/static/uploads/";
        Path filePath = Paths.get(uploadDirectory + fileName);

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de suppression de fichier ici
        }
    }


    private void updatePhotos(int numChalet, MultipartFile[] photos, Model model) throws IOException {
        // Supprimez d'abord toutes les photos existantes pour ce chalet
        for (String existingPhoto : chaletDbContext.getPhotos(numChalet)) {
            chaletDbContext.supprimerPhoto(numChalet, existingPhoto);
            deleteFile(existingPhoto); // Supprimez également le fichier physique de votre système de fichiers local
        }

        // Ensuite, insérez les nouvelles photos
        int counter = 1; // Utilisé pour nommer les photos
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                String originalFilename = photo.getOriginalFilename();
                String fileName = "chalet" + numChalet + "_" + counter + getFileExtension(originalFilename);

                try {
                    saveFile(fileName, photo);
                    chaletDbContext.insertPhoto(numChalet, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    model.addAttribute("errorMessage", "Une erreur s'est produite lors de la sauvegarde des photos.");
                }
                counter++;
            }
        }
    }


    @PostMapping("/ajouterPhoto/{numChalet}")
    public String ajouterPhoto(@PathVariable int numChalet, @RequestParam("photo") MultipartFile photo, Model model) {
        // Traitez l'ajout de la photo ici
        try {
            String originalFilename = photo.getOriginalFilename();
            String fileName = "chalet" + numChalet + "_" + getFileExtension(originalFilename);
            saveFile(fileName, photo);
            chaletDbContext.insertPhoto(numChalet, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Une erreur s'est produite lors de l'ajout de la photo.");
        }
        return "redirect:/admin/modifierChalet/{numChalet}";
    }

    @PostMapping("/supprimerPhoto/{numChalet}/{fileName}")
    public String supprimerPhoto(@PathVariable int numChalet, @PathVariable String fileName, Model model) {
        // Traitez la suppression de la photo ici
        try {
            // Supprimez également le fichier physique de votre système de fichiers local ici
            // Assurez-vous de gérer les exceptions appropriées
            chaletDbContext.supprimerPhoto(numChalet, fileName);
            deleteFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Une erreur s'est produite lors de la suppression de la photo.");
        }
        return "redirect:/admin/modifierChalet/{numChalet}";
    }

}
