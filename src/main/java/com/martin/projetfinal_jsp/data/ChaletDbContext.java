package com.martin.projetfinal_jsp.data;

import com.martin.projetfinal_jsp.models.Chalet;
import com.martin.projetfinal_jsp.models.Reservation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


    public class ChaletDbContext {

        // a) La propriété nommée "jdbcTemplate" de type "JdbcTemplate"
        private JdbcTemplate jdbcTemplate;

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




        // d) Méthode pour insérer une photo
        public void insertPhoto(int numChalet, String fileName) {
            String sql = "INSERT INTO Photos (NumChalet, Photo) VALUES (?, ?)";
            jdbcTemplate.update(sql, numChalet, fileName);
        }



        // e) Méthode pour sélectionner tous les chalets
        public List<Map<String, Object>> selectAllChalets() {
            String sql = "SELECT * FROM Chalets";  // Ajout de cette ligne pour définir la requête SQL
            List<Map<String, Object>> chalets = jdbcTemplate.queryForList(sql);
            System.out.println("Nombre de chalets trouvés: " + chalets.size());
            return chalets;
        }


        // f) Méthode pour sélectionner un chalet par son numéro
        public Chalet selectChaletByNumero(int numChalet) {
            String sql = "SELECT * FROM Chalets WHERE NumChalet = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{numChalet}, (rs, rowNum) -> {
                Chalet chalet = new Chalet();
                chalet.setNumChalet(rs.getInt("NumChalet"));
                chalet.setNombreChambres(rs.getInt("NombreChambres"));
                chalet.setDescription(rs.getString("Description"));
                chalet.setPrix(rs.getBigDecimal("Prix"));  // Modifié ici
                return chalet;
            });
        }


        // g) Méthode pour insérer une réservation
        public int insertReservation(Reservation reservation) {
            String sql = "INSERT INTO Reservations (NumChalet, Telephone, NomClient, Courriel, Prix, Duree) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, reservation.getNumChalet(), reservation.getTelephone(), reservation.getNomClient(), reservation.getCourriel(), reservation.getPrix(), reservation.getDuree());
            return jdbcTemplate.queryForObject("SELECT SCOPE_IDENTITY()", Integer.class);  // Retourne le numéro de la dernière réservation
        }
    }
