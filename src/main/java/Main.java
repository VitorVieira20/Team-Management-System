import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AthleteMenuHandler athleteMenuHandler = new AthleteMenuHandler();
        TrainerMenuHandler trainerMenuHandler = new TrainerMenuHandler();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("-----Seja Bem-Vindo à nossa aplicação-----");
            System.out.println("|                                         |");
            System.out.println("| 1- Menu dos Atletas                     |");
            System.out.println("| 2- Menu dos Treinadores                 |");
            System.out.println("| 3- Sair da Aplicação                    |");
            System.out.println("|_________________________________________|");

            int menuChoice = scanner.nextInt();
            switch (menuChoice) {
                case 1:
                    athleteMenuHandler.showAthletesMenu();
                    break;
                case 2:
                    trainerMenuHandler.showTrainersMenu();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opção incorreta: " + menuChoice);
                    System.out.println("Por favor, escolha uma opção válida.");
            }
        }
    }
}
