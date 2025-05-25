package project_game.loupGarou.model;

import java.util.List;
import java.util.Scanner;



public class HunterBehavior implements RoleBehavior{

    @Override
    public void doNightAction(Game game, Player self) {
        throw new UnsupportedOperationException("Unimplemented method 'doNightAction'");
    }
    


  public void onDeath(Game game, Player hunter) {
    Runnable action = () -> {
        Scanner scanner = new Scanner(System.in);

        System.out.println("💀 " + hunter.getName() + " (Hunter) is dying... but can shoot one last time!");
        System.out.println("Choose someone to shoot:");

        int index = 0;
        List<Player> targets = game.getPlayers().stream()
                .filter(p -> !p.equals(hunter)) // not to aim at oneself
                .toList();

        for (Player p : targets) {
            System.out.println(index + " : " + p.getName());
            index++;
        }

        int selected = -1;
        while (true) {
            System.out.print("Enter index: ");
            if (scanner.hasNextInt()) {
                selected = scanner.nextInt();
                scanner.nextLine(); // consomme le \n
                if (selected >= 0 && selected < targets.size()) break;
            } else {
                scanner.nextLine();
            }
            System.out.println("Invalid index. Try again.");
        }

        Player victim = targets.get(selected);
        System.out.println("The Hunter shoots " + victim.getName() + "!");
        victim.setDead(true);
        game.getAllPlayersDead().add(victim);
        game.getPlayers().remove(victim);
    };
    game.runWithTimeout(action, 30);
}
}
