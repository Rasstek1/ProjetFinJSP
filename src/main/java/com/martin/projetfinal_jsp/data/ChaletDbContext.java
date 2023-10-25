package com.martin.projetfinal_jsp.data;

import com.martin.projetfinal_jsp.models.Chalet;
import com.martin.projetfinal_jsp.models.Reservation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
    public class ChaletDbContext {

        // a) La propriété nommée "jdbcTemplate" de type "JdbcTemplate"
        private JdbcTemplate jdbcTemplate;

        private static final Logger logger = LoggerFactory.getLogger(ChaletDbContext.class);

        // b) Le constructeur qui reçoit un objet de type "JdbcTemplate"
        public ChaletDbContext(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }


        // c) Méthode pour insérer un chalet
        public int insertChalet(Chalet chalet) {
            String sql = "INSERT INTO Chalets (NombreChambres, Description, Prix) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            try {
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, chalet.getNombreChambres());
                    ps.setString(2, chalet.getDescription());
                    ps.setBigDecimal(3, chalet.getPrix());  // Utilisation de setBigDecimal ici
                    return ps;
                }, keyHolder);

                // Pour le débogage, imprimer la clé générée:
                System.out.println("NumChalet généré: " + keyHolder.getKey().intValue());

                // Récupérer et retourner le NumChalet généré
                return keyHolder.getKey().intValue();
            } catch (DataAccessException e) {
                // Imprime l'exception pour le débogage
                e.printStackTrace();

                // Loggue l'erreur
                System.err.println("Erreur lors de l'insertion du chalet dans la base de données : " + e.getMessage());

                // Si vous ne voulez pas propager l'erreur, vous pouvez retourner -1 ou un autre numéro d'erreur pour indiquer une insertion échouée.
                return -1;
            }
        }

        // Méthode pour mettre à jour un chalet dans la base de données
        public void updateChalet(Chalet chalet) {
            String sql = "UPDATE chalets SET nombreChambres = ?, description = ?, prix = ? WHERE numChalet = ?";

            jdbcTemplate.update(
                    sql,
                    chalet.getNombreChambres(),
                    chalet.getDescription(),
                    chalet.getPrix(),
                    chalet.getNumChalet()
            );
        }



/************************GESTION PHOTOS CHALET*******************************************************************/

        public void insertPhoto(int numChalet, String fileName) {
            String sql = "INSERT INTO Photos (NumChalet, Photo) VALUES (?, ?)";
            jdbcTemplate.update(sql, numChalet, fileName);
        }

        public void supprimerPhoto(int numChalet, String fileName) {
            String sql = "DELETE FROM Photos WHERE NumChalet = ? AND Photo = ?";
            jdbcTemplate.update(sql, numChalet, fileName);
        }
        public List<String> getPhotos(int numChalet) {
            String sql = "SELECT Photo FROM Photos WHERE NumChalet = ?";
            return jdbcTemplate.queryForList(sql, String.class, numChalet);
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


        public List<String> selectPhotosForChalet(int numChalet) {
            String sql = "SELECT Photo FROM Photos WHERE NumChalet = ?";
            return jdbcTemplate.queryForList(sql, new Object[]{numChalet}, String.class);
        }
/***************************************************************************************************************/

        // e) Méthode pour sélectionner tous les chalets
        public List<Map<String, Object>> selectAllChalets() {
            String sql = "SELECT * FROM Chalets";  // Ajout de cette ligne pour définir la requête SQL
            List<Map<String, Object>> chalets = jdbcTemplate.queryForList(sql);
            System.out.println("Nombre de chalets trouvés: " + chalets.size());
            return chalets;
        }

        public List<Chalet> selectAllChaletsForAdmin() {
            String sql = "SELECT * FROM Chalets";
            List<Chalet> chalets = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Chalet chalet = new Chalet();
                chalet.setNumChalet(rs.getInt("NumChalet"));
                chalet.setNombreChambres(rs.getInt("NombreChambres"));
                chalet.setDescription(rs.getString("Description"));
                chalet.setPrix(rs.getBigDecimal("Prix"));

                // Ajoutez ici la logique pour récupérer les photos associées au chalet
                List<String> photos = selectPhotosForChalet(chalet.getNumChalet());
                chalet.setListePhotos(photos);

                // Ajoutez des messages de journalisation pour déboguer
                logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Chalet NumChalet: {}", chalet.getNumChalet());
                logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Chalet NombreChambres: {}", chalet.getNombreChambres());
                logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Chalet Description: {}", chalet.getDescription());
                logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Chalet Prix: {}", chalet.getPrix());

                // Vérifiez la liste de photos
                if (photos != null) {
                    logger.debug("Photos associées au chalet: {}", photos);
                } else {
                    logger.debug("Aucune photo associée au chalet.");
                }

                return chalet;
            });

            return chalets;
        }

        // f) Méthode pour sélectionner un chalet par son numéro
        public Chalet selectChaletByNumero(int numChalet) {
            String sql = "SELECT * FROM Chalets WHERE NumChalet = ?";
            Chalet chalet = jdbcTemplate.queryForObject(sql, new Object[]{numChalet}, (rs, rowNum) -> {
                Chalet tempChalet = new Chalet();
                tempChalet.setNumChalet(rs.getInt("NumChalet"));
                tempChalet.setNombreChambres(rs.getInt("NombreChambres"));
                tempChalet.setDescription(rs.getString("Description"));
                tempChalet.setPrix(rs.getBigDecimal("Prix"));
                return tempChalet;
            });

            // Ajout des photos au chalet
            List<String> photos = selectPhotosForChalet(numChalet);
            chalet.setListePhotos(photos);

            return chalet;
        }



        // g) Méthode pour insérer une réservation
        public int insertReservation(Reservation reservation) {
            String sql = "INSERT INTO Reservations (NumChalet, Telephone, NomClient, Courriel, Prix, Duree) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, reservation.getNumChalet(), reservation.getTelephone(), reservation.getNomClient(), reservation.getCourriel(), reservation.getPrix(), reservation.getDuree());
            return jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Integer.class);  // Retourne le numéro de la dernière réservation
        }


    }
