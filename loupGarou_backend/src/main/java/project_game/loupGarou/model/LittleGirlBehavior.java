package project_game.loupGarou.model;

import java.util.List;
import java.util.Scanner;

public class LittleGirlBehavior implements RoleBehavior{

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void doNightAction(Game game, Player self) {

        Runnable action = () -> {
        List<Player> players = game.getPlayers();

        System.out.println("Little girl, wake up quietly...");
        System.out.println("You can spy on a player and see if they are Werewolf.");

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (!p.equals(self)) {
                System.out.println(i + ": " + p.getName());
            }
        }

        int selectedIndex = -1;
        while (true) {
            System.out.print("Enter the index of the player to spy on : ");
            if (scanner.hasNextInt()) {
                selectedIndex = scanner.nextInt();
                scanner.nextLine(); // consomme le \n
                if (selectedIndex >= 0 && selectedIndex < players.size()
                        && !players.get(selectedIndex).equals(self)) {
                    break;
                }
            } else {
                scanner.nextLine(); // mauvaise entrée
            }
            System.out.println("Invalid index. Try again.");
        }

        Player target = players.get(selectedIndex);
        if (target.getTeam() == Team.Wolf) {
            System.out.println(target.getName() + " is a Werewolf !");
        } else {
            System.out.println(target.getName() + " is not a Werewolf.");
        }
    };
    game.runWithTimeout(action, 30);
}
}
