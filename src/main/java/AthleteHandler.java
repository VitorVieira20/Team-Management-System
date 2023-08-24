import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class AthleteHandler {
    private String athleteName;
    private int athleteNumber;
    private String athleteTeam;
    private String athleteGender;

    public AthleteHandler(String athleteName, int athleteNumber, String athleteTeam, String athleteGender) {
        this.athleteName = athleteName;
        this.athleteNumber = athleteNumber;
        this.athleteTeam = athleteTeam;
        this.athleteGender = athleteGender;
    }

    public void seeIfAthleteExistsAndInsert() {
        String checkIfExistsSql = "SELECT athletes.id as id, athlete_name, athlete_number, team_name, team_gender FROM athletes " +
                "INNER JOIN athletes_has_teams ON athletes.id = athletes_has_teams.athletes_id " +
                "INNER JOIN teams ON athletes_has_teams.teams_id = teams.id " +
                "WHERE athletes.athlete_name = ? AND teams.team_name = ? AND teams.team_gender = ?";

        String insertAthleteSql = "INSERT INTO athletes (athlete_name, athlete_number) VALUES (?, ?)";

        String teamIdSql = "SELECT id FROM teams WHERE team_name = ? AND team_gender = ?";

        try {
            Connection connection = Conn.getConn();

            PreparedStatement checkIfExistsPs = connection.prepareStatement(checkIfExistsSql);
            checkIfExistsPs.setString(1, athleteName);
            checkIfExistsPs.setString(2, athleteTeam);
            checkIfExistsPs.setString(3, athleteGender);
            ResultSet resultSet = checkIfExistsPs.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                PreparedStatement insertAthletePs = connection.prepareStatement(insertAthleteSql, PreparedStatement.RETURN_GENERATED_KEYS);
                insertAthletePs.setString(1, athleteName);
                insertAthletePs.setInt(2, athleteNumber);
                insertAthletePs.executeUpdate();

                ResultSet generatedKeys = insertAthletePs.getGeneratedKeys();
                int newAthleteId = -1;
                if (generatedKeys.next()) {
                    newAthleteId = generatedKeys.getInt(1);
                }

                if (newAthleteId != -1) {
                    PreparedStatement getTeamIdPs = connection.prepareStatement(teamIdSql);
                    getTeamIdPs.setString(1, athleteTeam);
                    getTeamIdPs.setString(2, athleteGender);
                    ResultSet teamIdResultSet = getTeamIdPs.executeQuery();

                    if (teamIdResultSet.next()) {
                        int teamId = teamIdResultSet.getInt("id");

                        String insertAthleteTeamSql = "INSERT INTO athletes_has_teams (athletes_id, teams_id) VALUES (?, ?)";
                        PreparedStatement insertAthleteTeamPs = connection.prepareStatement(insertAthleteTeamSql);
                        insertAthleteTeamPs.setInt(1, newAthleteId);
                        insertAthleteTeamPs.setInt(2, teamId);
                        insertAthleteTeamPs.executeUpdate();

                        System.out.println("Atleta inserido com sucesso!");
                    }
                }
            } else {
                System.out.println("Não foi possível adicionar o atleta pois o mesmo já existe nesta equipa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showAllAthletes() {
        String sql = "SELECT * FROM athletes";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            System.out.println("Lista de Atletas");

            while (resultSet.next()) {
                System.out.println("--------------------");
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Nome: "+ resultSet.getString("athlete_name"));
                System.out.println("");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void seeIfAthleteExistsInTeamAndInsert (int athleteId) {
        // TODO: Falta fazer a verificação se o atleta existe em de fazer a inserção

        String teamIdSql = "SELECT * FROM teams WHERE team_name = ? AND team_gender = ?";

        String athleteExistSql = "SELECT * FROM athletes_has_teams WHERE athletes_id = ? AND teams_id = ?";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement getTeamIdPs = connection.prepareStatement(teamIdSql);
            getTeamIdPs.setString(1, athleteTeam);
            getTeamIdPs.setString(2, athleteGender);
            ResultSet teamIdResult = getTeamIdPs.executeQuery();

            if (teamIdResult.next()) {
                int teamId = teamIdResult.getInt("id");
                System.out.println("TEAM ID: " + teamId);

                PreparedStatement seeIfExist = connection.prepareStatement(athleteExistSql);
                seeIfExist.setInt(1, athleteId);
                seeIfExist.setInt(2, teamId);
                ResultSet athleteExistResult = seeIfExist.executeQuery();

                if (!athleteExistResult.isBeforeFirst()) {
                    String insertDataSql = "INSERT INTO athletes_has_teams (athletes_id, teams_id) VALUES (?, ?)";
                    PreparedStatement insertData = connection.prepareStatement(insertDataSql);
                    insertData.setInt(1, athleteId);
                    insertData.setInt(2, teamId);
                    insertData.executeUpdate();

                    System.out.println("Atleta associado a nova equipa com sucesso!");
                } else {
                    System.out.println("O atleta já existe nesta equipa");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean showAthleteById(int athleteId, boolean validId) {
        String getAthlete = "SELECT athletes.athlete_name as name, athletes.athlete_number as number, teams.team_name as team, teams.team_gender as gender FROM athletes " +
                "INNER JOIN athletes_has_teams ON athletes.id = athletes_has_teams.athletes_id " +
                "INNER JOIN teams ON athletes_has_teams.teams_id = teams.id " +
                "WHERE athletes.id = ?";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement getData = connection.prepareStatement(getAthlete);
            getData.setInt(1, athleteId);
            ResultSet resultSet = getData.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("Este atleta não existe");
                return false;
            } else {
                resultSet.next();
                String athleteName = resultSet.getString("name");
                int athleteNumber = resultSet.getInt("number");

                List<String> teams = new ArrayList<>();
                do {
                    String team = resultSet.getString("team");
                    String gender = resultSet.getString("gender");
                    teams.add(team + " (" + gender + ")");
                } while (resultSet.next());

                System.out.println("Deseja apagar o atleta?");
                System.out.println("Nome: " + athleteName);
                System.out.println("Número: " + athleteNumber);

                if (teams.size() == 1) {
                    System.out.println("Equipa: " + teams.get(0));
                } else {
                    System.out.println("Equipas: " + String.join(", ", teams));
                }
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAthleteById(int athleteId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1- Sim");
        System.out.println("2- Não");

        while (true) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    String deleteAthleteFisrtTable = "DELETE FROM athletes_has_teams WHERE athletes_has_teams.athletes_id = ?";
                    String deleteAthleteSecondTable = "DELETE FROM athletes WHERE athletes.id = ?";

                    try {
                        Connection connection = Conn.getConn();

                        PreparedStatement deleteFisrt = connection.prepareStatement(deleteAthleteFisrtTable);
                        deleteFisrt.setInt(1, athleteId);
                        deleteFisrt.executeUpdate();

                        PreparedStatement deleteSecond = connection.prepareStatement(deleteAthleteSecondTable);
                        deleteSecond.setInt(1, athleteId);
                        deleteSecond.executeUpdate();

                        System.out.println("Atleta apagado com sucesso");
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
    }

}

