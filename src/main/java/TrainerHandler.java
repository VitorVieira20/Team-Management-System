import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TrainerHandler {
    private String trainerName;
    private String trainerTeamName;
    private String trainerTeamGender;

    public TrainerHandler(String trainerName, String trainerTeamName, String trainerTeamGender) {
        this.trainerName = trainerName;
        this.trainerTeamName = trainerTeamName;
        this.trainerTeamGender = trainerTeamGender;
    }

    public void showAllTrainers() {
        String trainersSql = "SELECT trainers.id as id, trainers.name as name, GROUP_CONCAT(CONCAT(teams.team_name, ' (', teams.team_gender, ')')) as teams_info, COUNT(teams.id) as team_count " +
                "FROM trainers " +
                "INNER JOIN trainers_has_teams ON trainers.id = trainers_has_teams.trainers_id " +
                "INNER JOIN teams ON trainers_has_teams.teams_id = teams.id " +
                "GROUP BY trainers.id, trainers.name";

        String trainersNoTeamSql = "SELECT trainers.id as id, trainers.name as name " +
                "FROM trainers " +
                "LEFT JOIN trainers_has_teams ON trainers.id = trainers_has_teams.trainers_id " +
                "WHERE trainers_has_teams.trainers_id IS NULL;";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement getTrainers = connection.prepareStatement(trainersSql);
            ResultSet trainersSet = getTrainers.executeQuery();

            if (!trainersSet.isBeforeFirst()) {
                System.out.println("Ainda não existem treinador associados a equipas");
            } else {
                System.out.println("Treinadores com equipas associadas");
                while (trainersSet.next()) {
                    trainerName = trainersSet.getString("name");
                    int trainerId = trainersSet.getInt("id");
                    String trainerTeamsInfo = trainersSet.getString("teams_info");

                    System.out.println("----------------------");
                    System.out.println("ID: " + trainerId);
                    System.out.println("Nome: " + trainerName);
                    int teamCount = trainersSet.getInt("team_count");

                    if (teamCount == 1) {
                        System.out.println("Equipa: " + trainerTeamsInfo);
                    } else {
                        System.out.println("Equipas: " + trainerTeamsInfo);
                    }
                }
            }

            PreparedStatement getTrainersNoTeam = connection.prepareStatement(trainersNoTeamSql);
            ResultSet trainersNoTeam = getTrainersNoTeam.executeQuery();

            if (trainersNoTeam.isBeforeFirst()) {
                System.out.println("");
                System.out.println("Treinadores sem equipas associadas");
                while (trainersNoTeam.next()) {
                    trainerName = trainersNoTeam.getString("name");
                    int trainerId = trainersNoTeam.getInt("id");
                    System.out.println("----------------------");
                    System.out.println("ID: " + trainerId);
                    System.out.println("Nome: " + trainerName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertTrainer() {
        String insertSql = "INSERT INTO trainers (name) VALUES (?)";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(insertSql);
            ps.setString(1, trainerName);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getLastTrainerInserted() {
        String getTrainer = "SELECT id FROM trainers ORDER BY id DESC LIMIT 1;";
        int trainerId = -1;

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(getTrainer);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                trainerId = resultSet.getInt("id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return trainerId;
    }

    public int getTeamId() {
        String getTeam = "SELECT * FROM teams WHERE team_name = ? AND team_gender = ?";
        int teamId = -1;

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(getTeam);
            ps.setString(1, trainerTeamName);
            ps.setString(2, trainerTeamGender);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                teamId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return teamId;
    }

    public void addTrainerToTeam(int trainerId, int teamId){
        String addTrainerToTeam = "INSERT INTO trainers_has_teams (trainers_id, teams_id) VALUES (?, ?)";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(addTrainerToTeam);
            ps.setInt(1, trainerId);
            ps.setInt(2, teamId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showTrainersList() {
        String sql = "SELECT * FROM trainers";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("Não existem treinadores");
            } else {
                while (resultSet.next()) {
                    int trainerId = resultSet.getInt("id");
                    trainerName = resultSet.getString("name");
                    System.out.println("-------------------");
                    System.out.println("ID: " + trainerId);
                    System.out.println("Nome: " + trainerName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidId(int trainerId) {
        String sql = "SELECT * FROM trainers WHERE id = ?";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, trainerId);
            ResultSet resultSet = ps.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("Este treinador não existe");
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean seeAlreadyAssociate(int trainerId, int teamId) {
        String sql = "SELECT * FROM trainers_has_teams WHERE trainers_id = ? AND teams_id = ?";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, trainerId);
            ps.setInt(2, teamId);
            ResultSet resultSet = ps.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertIntoTeam(int trainerId, int teamId) {
        String sql = "INSERT INTO trainers_has_teams (trainers_id, teams_id) VALUES (?, ?)";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, trainerId);
            ps.setInt(2, teamId);
            ps.executeUpdate();
            ps.close();

            System.out.println("Treinador associado a equipa com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showTrainerById(int trainerId) {
        Scanner scanner = new Scanner(System.in);
        String getTrainerSql = "SELECT * FROM trainers WHERE id = ?";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(getTrainerSql);
            ps.setInt(1, trainerId);
            ResultSet getTrainerResult = ps.executeQuery();

            if (!getTrainerResult.isBeforeFirst()) {
                System.out.println("Treinador não encontrado.");
                return;
            }

            while (getTrainerResult.next()) {
                trainerName = getTrainerResult.getString("name");
                System.out.println("----------------------");
                System.out.println("ID: " + trainerId);
                System.out.println("Nome: " + trainerName);
            }

            System.out.println("Deseja mesmo apagar este treinador?");
            System.out.println("1- Sim");
            System.out.println("2- Não");

            while (true) {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        String trainerAssociate = "SELECT * FROM trainers_has_teams WHERE trainers_id = ?";

                        try {
                            PreparedStatement trainer = connection.prepareStatement(trainerAssociate);
                            trainer.setInt(1, trainerId);
                            ResultSet trainerResult = trainer.executeQuery();

                            if (!trainerResult.isBeforeFirst()) {
                                String deleteTrainer = "DELETE FROM trainers WHERE trainers.id = ?";
                                try {
                                    PreparedStatement delete = connection.prepareStatement(deleteTrainer);
                                    delete.setInt(1, trainerId);
                                    delete.executeUpdate();
                                    delete.close();
                                    System.out.println("Treinador apagado com sucesso!");
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                String deleteFisrtTable = "DELETE FROM trainers_has_teams WHERE trainers_has_teams.trainers_id = ?";
                                String deleteSecondTable = "DELETE FROM trainers WHERE trainers.id = ?";

                                try {
                                    PreparedStatement firstTable = connection.prepareStatement(deleteFisrtTable);
                                    firstTable.setInt(1, trainerId);
                                    ;
                                    firstTable.executeUpdate();
                                    firstTable.close();

                                    PreparedStatement secondTable = connection.prepareStatement(deleteSecondTable);
                                    secondTable.setInt(1, trainerId);
                                    secondTable.executeUpdate();
                                    secondTable.close();
                                    System.out.println("Treinador apagado com sucesso!");
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    case 2:
                        return;
                    default:
                        System.out.println("Opção incorreta: " + choice);
                        System.out.println("Por favor, escolha uma opção válida.");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
