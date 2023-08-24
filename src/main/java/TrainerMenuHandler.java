import java.util.Scanner;

public class TrainerMenuHandler {
    private String trainerName;
    private String trainerTeamName;
    private String trainerTeamGender;
    public void showTrainersMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--------------Menu Treinadores-------------");
        System.out.println("|                                         |");
        System.out.println("| 1- Ver Treinadores                      |");
        System.out.println("| 2- Novo Treinadore                      |");
        System.out.println("| 3- Associar Treinadore                  |");
        System.out.println("| 4- Apagar Treinadore                    |");
        System.out.println("| 5- Voltar                               |");
        System.out.println("|_________________________________________|");

        while (true) {
            int athletesChoice = scanner.nextInt();
            switch (athletesChoice) {
                case 1:
                    showTrainersFilterTeam();
                    return;
                case 2:
                    addTrainer(false);
                    showTrainersMenu();
                    return;
                case 3:
                    addAthleteToTeam();
                    showTrainersMenu();
                    return;
                case 4:
                    deleteAthlete();
                    showTrainersMenu();
                    return;
                case 5:
                    return;
                default:
                    System.out.println("Opção incorreta: " + athletesChoice);
                    System.out.println("Por favor, escolha uma opção válida.");
            }
        }
    }

    private void deleteAthlete() {
    }

    private void addAthleteToTeam() {
    }

    private void addTrainer(boolean b) {
    }

    private void showTrainersFilterTeam() {
    }
}
