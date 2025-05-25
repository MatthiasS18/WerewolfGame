package project_game.loupGarou.model;

import java.util.List;
import java.util.Scanner;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class WitchBehavior implements RoleBehavior {

    private boolean poisonUsed = false;
    private boolean reviveUsed = false;


    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void doNightAction(Game game, Player self) {
        Runnable action = () -> {
        System.out.println("🧙‍♀️ " + self.getName() + " (Witch) wakes up...");

        List<Player> players = game.getPlayers();
        List<Player> allPlayersDead = game.getAllPlayersDead();

        // 🔪 Potion de poison
        if (!poisonUsed) {
            System.out.print("Do you want to use the poison potion? (yes/no): ");
            String poisonInput = scanner.nextLine().trim();

            if (poisonInput.matches("(?i)yes|no")) {
                if (poisonInput.equalsIgnoreCase("yes")) {
                    System.out.println("You chose to use the poison potion.");
                    displayPlayers(players);

                    System.out.print("Who do you want to kill? Enter the index: ");
                    int selected = askPlayerIndex(players);
                    Player victim = players.get(selected);

                    System.out.println("You killed " + victim.getName());
                    victim.setDead(true);
                    allPlayersDead.add(victim);
                    players.remove(victim);
                    poisonUsed = true;
                } else {
                    System.out.println("You chose not to use the poison potion.");
                }
            } else {
                System.out.println("Invalid input. Skipping poison choice.");
            }
        } else {
            System.out.println("You already used the poison potion.");
        }

        // 💉 Potion de soin
        if (!reviveUsed) {
            if (allPlayersDead.isEmpty() && game.getLastVictim() == null) {
                System.out.println("Everyone is alive! No one to revive.");
                return;
            }

            System.out.println("Here are the dead players:");
            displayPlayers(allPlayersDead);
            

            System.out.print("Do you want to use the revive potion? (yes/no): ");
            String reviveInput = scanner.nextLine().trim();

            if (reviveInput.matches("(?i)yes|no")) {
                if (reviveInput.equalsIgnoreCase("yes")) {
                    System.out.print("Who do you want to revive?");
                    int reviveIndex = askPlayerIndex(allPlayersDead);
                    Player revived = allPlayersDead.get(reviveIndex);

                    System.out.println("You revived " + revived.getName());
                    revived.setDead(false);
                    players.add(revived);
                    allPlayersDead.remove(revived);
                    reviveUsed = true;
                } else {
                    System.out.println("You chose not to use the revive potion.");
                }
            } else {
                System.out.println("Invalid input. Skipping revive choice.");
            }
        } else {
            System.out.println("You already used the revive potion.");
        }
    };
    game.runWithTimeout(action, 30);
}

    private int askPlayerIndex(List<Player> list) {
        int selectedPlayer = -1;
        while (true) {
            System.out.print("Enter index: ");
            if (scanner.hasNextInt()) {
                selectedPlayer = scanner.nextInt();
                scanner.nextLine(); // consomme le \n
                if (selectedPlayer >= 0 && selectedPlayer < list.size()) break;
            } else {
                scanner.nextLine(); // consomme la mauvaise entrée
            }
            System.out.println("Invalid index. Try again.");
        }
        return selectedPlayer;
    }

    private void displayPlayers(List<Player> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + " : " + list.get(i).getName());
        }
    }
}