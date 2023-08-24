import java.util.Scanner;

public class TrainerMenuHandler {
    private String trainerName;
    private String trainerTeamName;
    private String trainerTeamGender;
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
                        trainerTeamName = "babybasket";
                        return trainerTeamName;
                    case 2:
                        trainerTeamName = "mini-8";
                        return trainerTeamName;
                    case 3:
                        trainerTeamName = "mini-10";
                        return trainerTeamName;
                    case 4:
                        trainerTeamName = "mini-1";
                        return trainerTeamName;
                    case 5:
                        trainerTeamName = "sub-14";
                        return trainerTeamName;
                    case 6:
                        trainerTeamName = "sub-16";
                        return trainerTeamName;
                    case 7:
                        trainerTeamName = "sub-18";
                        return trainerTeamName;
                    case 8:
                        trainerTeamName = "sub-19";
                        return trainerTeamName;
                    case 9:
                        trainerTeamName = "seniores";
                        return trainerTeamName;
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
                    trainerTeamGender = "male";
                    return trainerTeamGender;
                case 2:
                    trainerTeamGender = "female";
                    return trainerTeamGender;
                default:
                    System.out.println("Opção incorreta: " + genderChoice);
                    System.out.println("Por favor, escolha uma opção válida.");
            }
        }
    }
    public void showTrainersMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--------------Menu Treinadores-------------");
        System.out.println("|                                         |");
        System.out.println("| 1- Ver Treinadores                      |");
        System.out.println("| 2- Novo Treinador                       |");
        System.out.println("| 3- Associar Treinador                   |");
        System.out.println("| 4- Apagar Treinador                     |");
        System.out.println("| 5- Voltar                               |");
        System.out.println("|_________________________________________|");

        while (true) {
            int athletesChoice = scanner.nextInt();
            switch (athletesChoice) {
                case 1:
                    showTrainers();
                    return;
                case 2:
                    addTrainerMenu();
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
    private void showTrainers() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Lista de Treinadores");
        TrainerHandler trainerHandler = new TrainerHandler(trainerName, trainerTeamName, trainerTeamGender);
        trainerHandler.showAllTrainers();

        System.out.println("");
        System.out.println(" 1- Adicionar novo treinador");
        System.out.println(" 2- Voltar ");
        System.out.println(" 3- Sair ");

        while (true) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addTrainerMenu();
                    showTrainersMenu();
                    return;
                case 2:
                    showTrainersMenu();
                    return;
                case 3:
                    return;
                default:
                    System.out.println("Opção incorreta: " + choice);
                    System.out.println("Por favor, escolha uma opção válida.");
            }
        }
    }
    private void addTrainerMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("----Adicionar Treinador----");
        System.out.println("Introduza o nome do treinador");
        trainerName = scanner.nextLine();

        System.out.println("Pretendo associar o treinador a uma equipa?");
        System.out.println("1- Sim");
        System.out.println("2- Não");

        while (true) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addTrainerToTeam();
                    return;
                case 2:
                    addTrainerNoTeam();
                    return;
                default:
                    System.out.println("Opção incorreta: " + choice);
                    System.out.println("Por favor, escolha uma opção válida.");
            }
        }

    }

    private void addTrainerNoTeam() {
        TrainerHandler trainerHandler = new TrainerHandler(trainerName, trainerTeamName, trainerTeamGender);
        trainerHandler.insertTrainer();
    }

    private void addTrainerToTeam() {
        TrainerHandler trainerHandler = new TrainerHandler(trainerName, trainerTeamName, trainerTeamGender);
        trainerHandler.insertTrainer();

        TrainerHandler getTrainerId = new TrainerHandler(trainerName, trainerTeamName, trainerTeamGender);
        int trainerId = getTrainerId.getLastTrainerInserted();

        showTeamOptions();
        showGenderOptions();

        TrainerHandler getTeamId = new TrainerHandler(trainerName, trainerTeamName, trainerTeamGender);
        int teamId = getTeamId.getTeamId();

        TrainerHandler associateTrainer = new TrainerHandler(trainerName, trainerTeamName, trainerTeamGender);
        associateTrainer.addTrainerToTeam(trainerId, teamId);

        System.out.println("Treinador associado a uma equipa com sucesso!");

    }

    private void addAthleteToTeam() {
        Scanner scanner = new Scanner(System.in);
        boolean isValid = false;
        boolean alreadyExist = false;
        int trainerId;
        System.out.println("Lista de treinadores");
        System.out.println("");
        TrainerHandler trainerHandler = new TrainerHandler(trainerName, trainerTeamName, trainerTeamGender);
        trainerHandler.showTrainersList();
        System.out.println("");

        do {
            System.out.println("Insira o ID do treinador que quer associar a uma equipa");
            trainerId = scanner.nextInt();
            isValid = trainerHandler.isValidId(trainerId);
        } while (!isValid);

        showTeamOptions();
        showGenderOptions();

        TrainerHandler trainerTeamId = new TrainerHandler(trainerName, trainerTeamName, trainerTeamGender);
        int teamID = trainerTeamId.getTeamId();

        alreadyExist = trainerHandler.seeAlreadyAssociate(trainerId, teamID);

        if (alreadyExist == false) {
            trainerHandler.insertIntoTeam(trainerId, teamID);
        } else {
            System.out.println("Este treinador já está associado a esta equipa");
        }
    }

    private void deleteAthlete() {
        Scanner scanner = new Scanner(System.in);
        boolean isValid;
        int trainerId;
        TrainerHandler trainerHandler = new TrainerHandler(trainerName, trainerTeamName, trainerTeamGender);
        trainerHandler.showAllTrainers();

        do {
            System.out.println("Insira o ID do treinador que quer apagar");
            trainerId = scanner.nextInt();
            isValid = trainerHandler.isValidId(trainerId);
        } while (!isValid);

        TrainerHandler getTrainerById = new TrainerHandler(trainerName, trainerTeamName, trainerTeamGender);
        getTrainerById.showTrainerById(trainerId);

        //AthleteHandler athleteHandlerTeam = new AthleteHandler(athleteName, athleteNumber, athleteTeam, athleteGender);
    }

}
