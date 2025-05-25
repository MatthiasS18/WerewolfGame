package project_game.loupGarou.model;

import java.util.*;
import java.util.stream.Collectors;

public class ThiefBehavior implements RoleBehavior{

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void doNightAction(Game game, Player self) {
        Runnable action = () -> {
        System.out.println("Thief, wake up.");
        System.out.println("You may choose a role to steal among two unused roles.");

        List<Role> allRoles = new ArrayList<>(Arrays.asList(Role.values()));

        List<Role> usedRoles = game.getPlayers().stream()
            .map(Player::getRole)
            .collect(Collectors.toList());

        allRoles.removeAll(usedRoles);

        if (allRoles.size() < 2) {
            System.out.println("Not enough unused roles available for the thief to choose from.");
            return;
        }

        Collections.shuffle(allRoles);
        Role role1 = allRoles.get(0);
        Role role2 = allRoles.get(1);

        System.out.println("Available roles to steal:");
        System.out.println("1: " + role1);
        System.out.println("2: " + role2);

        int choice = -1;
        while (true) {
            System.out.print("Choose 1 or 2: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1 || choice == 2) break;
            } else {
                scanner.nextLine();
            }
            System.out.println("Invalid choice. Please choose 1 or 2.");
        }

        Role chosenRole = (choice == 1) ? role1 : role2;
        self.setRole(chosenRole);

        System.out.println("You have now become: " + chosenRole);

        if (chosenRole.equals(Role.WEREWOLF)) {
            System.out.println("You are now a Loup-Garou, but you do not know who the others are.");
        }
    };
    game.runWithTimeout(action, 30);
}
}