package project_game.loupGarou.model;

import java.util.List;
import java.util.Scanner;

public class Fortune_TellerBehavior implements RoleBehavior{

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void doNightAction(Game game, Player self) {
        Runnable action = () -> {
        List<Player> players = game.getPlayers();

        System.out.println("Fortune teller, wake up.");
        System.out.println("Choose a player to see their role:");

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (!p.equals(self)) {
                System.out.println(i + ": " + p.getName());
            }
        }

        int selectedIndex = -1;
        while (true) {
            System.out.print("Enter the player index : ");
            if (scanner.hasNextInt()) {
                selectedIndex = scanner.nextInt();
                scanner.nextLine(); 
                if (selectedIndex >= 0 && selectedIndex < players.size()
                        && !players.get(selectedIndex).equals(self)) {
                    break;
                }
            } else {
                scanner.nextLine(); 
            }
            System.out.println("Invalid index. Try again.");
        }

        Player target = players.get(selectedIndex);
        System.out.println("🔍 The role of " + target.getName() + " is : " + target.getRole());
    };
    game.runWithTimeout(action, 30);
    }
}