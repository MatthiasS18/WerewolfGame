package project_game.loupGarou.model;

import java.util.List;
import java.util.Scanner;

public class CupidoBehavior implements RoleBehavior {

    private final Scanner scanner = new Scanner(System.in); // scanner unique

   @Override
    public void doNightAction(Game game, Player self) {

        Runnable action = () -> {
        List<Player> players = game.getPlayers();

        System.out.println("💘 Cupidon, wake up.");
        System.out.println("Choose 2 different players to fall in love:");

        for (int i = 0; i < players.size(); i++) {
            System.out.println(i + ": " + players.get(i).getName());
        }

        int index1 = askPlayerIndex(players.size(), "Enter index for the first player: ");
        int index2;

        while (true) {
            index2 = askPlayerIndex(players.size(), "Enter index for the second player: ");
            if (index2 != index1) break;
            System.out.println("You can't choose the same player. Try again.");
        }

        Player lover1 = players.get(index1);
        Player lover2 = players.get(index2);

        game.put2PeopleinLove(lover1, lover2);

        System.out.println("💞 " + lover1.getName() + " and " + lover2.getName() + " are now in love!");
    };




    game.runWithTimeout(action, 30);


    
    }











    private int askPlayerIndex(int size, String prompt) {
        int index = -1;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                index = scanner.nextInt();
                scanner.nextLine(); // Consomme le \n
                if (index >= 0 && index < size) break;
            } else {
                scanner.nextLine(); // Consomme la mauvaise entrée
            }
            System.out.println("Invalid index. Try again.");
        }
        return index;
    }
}

    
    
