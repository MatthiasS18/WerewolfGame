package project_game.loupGarou.model;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WerewolvesBehavior implements RoleBehavior{

 private final Scanner scanner = new Scanner(System.in);

    @Override
    public void doNightAction(Game game, Player self) {
        Runnable action = () -> {
        List<Player> alivePlayers = game.getPlayers().stream()
                .filter(p -> !p.isDead())
                .collect(Collectors.toList());

        List<Player> victims = alivePlayers.stream()
                .filter(p -> !p.getRole().equals(Role.WEREWOLF))
                .collect(Collectors.toList());

        if (victims.isEmpty()) {
            System.out.println("No valid victim available.");
            return;
        }

        System.out.println("Werewolves, wake up.");
        System.out.println("Choose a player to kill tonight:");

        for (int i = 0; i < victims.size(); i++) {
            System.out.println(i + ": " + victims.get(i).getName());
        }

        int selected = askPlayerIndex(victims.size());

        Player target = victims.get(selected);
        System.out.println("You chose to kill: " + target.getName());

        game.setLastVictim(target);
        game.getAllPlayersDead().add(target); // weird maybe not a good pratic from matthias
    };
    game.runWithTimeout(action, 30);
}

    private int askPlayerIndex(int size) {
        int index = -1;
        while (true) {
            System.out.print("Enter victim index: ");
            if (scanner.hasNextInt()) {
                index = scanner.nextInt();
                scanner.nextLine(); 
                if (index >= 0 && index < size) break;
            } else {
                scanner.nextLine(); 
            }
            System.out.println("Invalid index. Try again.");
        }
        return index;
    }
}
    

