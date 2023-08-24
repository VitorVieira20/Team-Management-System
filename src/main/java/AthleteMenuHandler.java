import java.sql.*;
import java.util.Scanner;

public class AthleteMenuHandler {
    private String athleteName;
    private int athleteNumber;
    private String athleteTeam;
    private String athleteGender;
    public String showTeamOptions() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Escolha o escalão do atleta:");
            System.out.println();
            System.out.println(" 1-  Babybasket");
            System.out.println(" 2-  Mini-8");
            System.out.println(" 3-  Mini-10");
            System.out.println(" 4-  Mini-12");
            System.out.println(" 5-  Sub-14");
            System.out.println(" 6-  Sub-16");
            System.out.println(" 7-  Sub-18");
            System.out.println(" 8-  Sub-19");
            System.out.println(" 9-  Seniores");

            while (true) {
                int teamChoice = scanner.nextInt();
                switch (teamChoice) {
                    case 1:
                        athleteTeam = "babybasket";
                        return athleteTeam;
                    case 2:
                        athleteTeam = "mini-8";
                        return athleteTeam;
                    case 3:
                        athleteTeam = "mini-10";
                        return athleteTeam;
                    case 4:
                        athleteTeam = "mini-1";
                        return athleteTeam;
                    case 5:
                        athleteTeam = "sub-14";
                        return athleteTeam;
                    case 6:
                        athleteTeam = "sub-16";
                        return athleteTeam;
                    case 7:
                        athleteTeam = "sub-18";
                        return athleteTeam;
                    case 8:
                        athleteTeam = "sub-19";
                        return athleteTeam;
                    case 9:
                        athleteTeam = "seniores";
                        return athleteTeam;
                    default:
                        System.out.println("Opção incorreta: " + teamChoice);
                        System.out.println("Por favor, escolha uma opção válida.");
                }
            }
        }
    }
    public String showGenderOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("A equipa é de masculinos ou femininos?");
        System.out.println();
        System.out.println(" 1-  Masculinos");
        System.out.println(" 2-  Femininos");

        while (true) {
            int genderChoice = scanner.nextInt();
            switch (genderChoice) {
                case 1:
                    athleteGender = "male";
                    return athleteGender;
                case 2:
                    athleteGender = "female";
                    return athleteGender;
                default:
                    System.out.println("Opção incorreta: " + genderChoice);
                    System.out.println("Por favor, escolha uma opção válida.");
            }
        }
    }

    private void showGamesMenu() {
        System.out.println("Menu dos jogos");
    }
    private void showTrainersMenu() {
        System.out.println("Menu dos treinadores");
    }
    public void showAthletesMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("---------------Menu Atletas---------------");
        System.out.println("|                                         |");
        System.out.println("| 1- Ver Atletas                          |");
        System.out.println("| 2- Novo Atleta                          |");
        System.out.println("| 3- Associar Atleta                      |");
        System.out.println("| 4- Apagar Atleta                        |");
        System.out.println("| 5- Voltar                               |");
        System.out.println("|_________________________________________|");

        while (true) {
            int athletesChoice = scanner.nextInt();
            switch (athletesChoice) {
                case 1:
                    showAthletesFilterTeam();
                    return;
                case 2:
                    addAthlete(false);
                    showAthletesMenu();
                    return;
                case 3:
                    addAthleteToTeam();
                    showAthletesMenu();
                    return;
                case 4:
                    deleteAthlete();
                    showAthletesMenu();
                    return;
                case 5:
                    return;
                default:
                    System.out.println("Opção incorreta: " + athletesChoice);
                    System.out.println("Por favor, escolha uma opção válida.");
            }
        }
    }
    private void showAthletesFilterTeam() {
        Scanner scanner = new Scanner(System.in);
        showTeamOptions();
        showGenderOptions();
        String sql = "SELECT athletes.id as id, athletes.athlete_name as name, athletes.athlete_number as number " +
                "FROM athletes " +
                "INNER JOIN athletes_has_teams ON athletes.id = athletes_has_teams.athletes_id " +
                "INNER JOIN teams ON athletes_has_teams.teams_id = teams.id " +
                "WHERE teams.team_name = ? AND teams.team_gender = ?";

        try {
            Connection connection = Conn.getConn();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, athleteTeam);
            ps.setString(2, athleteGender);
            ResultSet resultSet = ps.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("Equipa não existe");
            }

            while (resultSet.next()) {
                System.out.println("-----------------------");
                System.out.println("|  ID: " + resultSet.getInt("id"));
                System.out.println("|  Nome: " + resultSet.getString("name"));
                System.out.println("|  Número " + resultSet.getInt("number"));
            }

            System.out.println("");
            System.out.println(" 1- Adicionar novo atleta");
            System.out.println(" 2- Voltar ");
            System.out.println(" 3- Sair ");

            int menuChoice = scanner.nextInt();

            while (true) {
                switch (menuChoice) {
                    case 1:
                        addAthlete(true);
                        showAthletesMenu();
                        return;
                    case 2:
                        showAthletesMenu();
                        return;
                    case 3:
                        return;
                    default:
                        System.out.println("Opção incorreta: " + menuChoice);
                        System.out.println("Por favor, escolha uma opção válida.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addAthlete(Boolean teamDefined) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("----Adicionar Atleta----");
        System.out.println("Introduza o nome");
        athleteName = scanner.nextLine();
        System.out.println("Introduza o número");
        athleteNumber = scanner.nextInt();
        scanner.nextLine();

        if (!teamDefined) {
            athleteTeam = showTeamOptions();
            athleteGender = showGenderOptions();
        }

        AthleteHandler athleteHandler = new AthleteHandler(athleteName, athleteNumber, athleteTeam, athleteGender);
        athleteHandler.seeIfAthleteExistsAndInsert();
    }
    private void addAthleteToTeam() {
        Scanner scanner = new Scanner(System.in);
        AthleteHandler athleteHandlerSow = new AthleteHandler(athleteName, athleteNumber, athleteTeam, athleteGender);
        athleteHandlerSow.showAllAthletes();

        System.out.println("Introduza o ID do atleta que quer associar a uma equipa");
        int athleteId = scanner.nextInt();

        showTeamOptions();
        showGenderOptions();

        AthleteHandler athleteHandlerTeam = new AthleteHandler(athleteName, athleteNumber, athleteTeam, athleteGender);
        athleteHandlerTeam.seeIfAthleteExistsInTeamAndInsert(athleteId);
    }
    private void deleteAthlete() {
        Scanner scanner = new Scanner(System.in);
        AthleteHandler athleteHandlerShow = new AthleteHandler(athleteName, athleteNumber, athleteTeam, athleteGender);
        athleteHandlerShow.showAllAthletes();

        boolean validId = false;
        int athleteId;

        AthleteHandler athleteHandlerTeam = new AthleteHandler(athleteName, athleteNumber, athleteTeam, athleteGender);

        do {
            System.out.println("Introduza o ID do atleta que quer apagar");
            athleteId = scanner.nextInt();
            validId = athleteHandlerTeam.showAthleteById(athleteId, validId);
        } while (!validId);

        AthleteHandler athleteHandlerDelete = new AthleteHandler(athleteName, athleteNumber, athleteTeam, athleteGender);
        athleteHandlerDelete.deleteAthleteById(athleteId);

    }
}
