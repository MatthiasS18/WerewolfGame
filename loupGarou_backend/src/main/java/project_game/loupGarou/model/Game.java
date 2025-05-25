package project_game.loupGarou.model;


import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Game{

    private boolean isFinished = false;
    private List<Player> players = new ArrayList<>();
    private Team teamWinner;
    private Stack<Player> allPlayersDead = new Stack<>();
    private int numberPlayer;
    private Player[] playerInLove = new Player[2];
    private int round = 0;
    private Stack<Role>roles = new Stack<>();
    private static final Scanner scanner = new Scanner(System.in);
    private Player lastVictim;




    public void playGame() {
        addPlayers();
        shuffle(); // shuffle roles
        distributeCharacterToPlayer();


        System.out.println("""
                -------------------------------DEBUG-----------------------------
                DISPLAY
                """);
int n = 0;
for (Player player : players) {
    System.out.println(n+ ". :"+ player.getName() + " - Role : " + player.getRole() + " - Team :" + player.getTeam());
    n++;
}

                System.out.println("------------------------------DEBUG--------------------------------");

        
        // Phase spéciale : Cupido
       for (Player player : players) {
            if (player.getRole() == Role.CUPIDO) {
                System.out.println("Cupido moment");
                cupidoMoment();
                System.out.println("End Cupido moment");
            }
       }
      

    
        // Boucle principale de jeu
        while (!isFinished) {
            round++;
            System.out.println("\n🔄 === Round " + round + " ===");
    
            // Nuit : chaque rôle joue
            startNightPhase();

            for (Player player : players) {
                if (player.getRole() == Role.CUPIDO) {
                    System.out.println("---------------START not displayed in game -----------------");
                    loversReveal();
                    System.out.println("---------------not displayed in game END-----------------");
                }
           }
  
    
            // Mort éventuelle par les loups (à stocker via setLastVictim)
            if (lastVictim != null) {
                lastVictim.setDead(true);
                allPlayersDead.push(lastVictim);
                players.remove(lastVictim);
                System.out.println("☠️ During the night, " + lastVictim.getName() + " was killed!");
            } else {
                System.out.println("No one was killed during the night.");
            }
    
            // Vérifie si amoureux sont morts
            checkLovers();
    
            // Affiche les joueurs restants
            System.out.println("🧑‍🤝‍🧑 Remaining players:");
            displayListPlayer(players);
    
            // Phase de vote du village
            dayVotingPhase();
    
            // Vérifie les conditions de victoire
            checkVictory();
        }
    
        System.out.println("🏁 The game is over. Team " + teamWinner + " wins!");
    }


    public void dayVotingPhase() {
        if (players.isEmpty()) {
            System.out.println("No one left to vote.");
            return;
        }
        checkVictory();
        Runnable voteAction = () -> {
        System.out.println("🗳️ Time to vote! Choose someone to eliminate:");
        displayListPlayer(players);
    
        int selected = -1;
        while (true) {
            System.out.print("Enter the index of the player to eliminate: ");
            if (scanner.hasNextInt()) {
                selected = scanner.nextInt();
                scanner.nextLine(); // évite bug \n
                if (selected >= 0 && selected < players.size()) break;
            } else {
                scanner.nextLine();
            }
            System.out.println("Invalid choice. Try again.");
        }
    
        Player eliminated = players.get(selected);
        eliminated.setDead(true);
        allPlayersDead.push(eliminated);
        players.remove(eliminated);
        System.out.println("🪦 " + eliminated.getName() + " has been eliminated.");
    
        // Effet chasseur ?
        if (eliminated.getRole() == Role.HUNTER) {
            System.out.println("🎯 The Hunter can shoot someone before dying!");
            displayListPlayer(players);
            int shot = -1;
            while (true) {
                System.out.print("Enter index to shoot: ");
                if (scanner.hasNextInt()) {
                    shot = scanner.nextInt();
                    scanner.nextLine();
                    if (shot >= 0 && shot < players.size()) break;
                } else {
                    scanner.nextLine();
                }
                System.out.println("Invalid choice. Try again.");
            }
            Player victim = players.get(shot);
            victim.setDead(true);
            allPlayersDead.push(victim);
            players.remove(victim);
            System.out.println("💥 " + victim.getName() + " has been shot by the Hunter!");
        }
    };
    runWithTimeout(voteAction, 60);
    }
    

    public void checkLovers() {
        if (playerInLove[0] != null && playerInLove[1] != null) {
            boolean p1Dead = playerInLove[0].isDead();
            boolean p2Dead = playerInLove[1].isDead();
    
            if (p1Dead && !p2Dead) {
                System.out.println("💔 " + playerInLove[1].getName() + " dies of heartbreak.");
                playerInLove[1].setDead(true);
                players.remove(playerInLove[1]);
            } else if (!p1Dead && p2Dead) {
                System.out.println("💔 " + playerInLove[0].getName() + " dies of heartbreak.");
                playerInLove[0].setDead(true);
                players.remove(playerInLove[0]);
            }
        }
    }
    


    public void checkVictory() {
        if (onlyLoversAlive()) {
            teamWinner = Team.Lover;
            isFinished = true;
            System.out.println("💘 The lovers are the only survivors and win together!");
            return;
        }
    
        boolean wolvesAlive = players.stream().anyMatch(p -> p.getTeam() == Team.Wolf && !p.isDead());
        boolean villagersAlive = players.stream().anyMatch(p -> p.getTeam() == Team.Villagers && !p.isDead());
    
        if (!wolvesAlive && villagersAlive) {
            teamWinner = Team.Villagers;
            isFinished = true;
            System.out.println("🌞 The Villagers win!");
        }
        else if (wolvesAlive && !villagersAlive) {
            teamWinner = Team.Wolf;
            isFinished = true;
            System.out.println("🐺 The Werewolves win!");
        }
        else {
            long wolfCount = players.stream().filter(p -> p.getTeam() == Team.Wolf && !p.isDead()).count();
            long othersCount = players.stream().filter(p -> p.getTeam() != Team.Wolf && !p.isDead()).count();
            if (wolfCount >= othersCount && wolfCount > 0) {
                teamWinner = Team.Wolf;
                isFinished = true;
                System.out.println("🐺 The Werewolves outnumber the rest and win!");
            }
        }
    }
    
    
    private boolean onlyLoversAlive() {
        if (playerInLove[0] == null || playerInLove[1] == null) return false;
        return players.size() == 2 && players.contains(playerInLove[0]) && players.contains(playerInLove[1]);
    }
    


    

    public void put2PeopleinLove(Player player1, Player player2){
        playerInLove[0] = player1;
        playerInLove[1] = player2;
    }

    /*
     * This method allows to shuffle roles.
     */
    public void shuffle() {
        roles.clear(); 
    
        int nbPlayers = players.size();
    
        int nbWolves;
        if (nbPlayers <= 6) nbWolves = 1;
        else if (nbPlayers <= 8) nbWolves = 2;
        else if (nbPlayers <= 11) nbWolves = 3;
        else if (nbPlayers <= 15) nbWolves = 4;
        else nbWolves = 5;
    
        for (int i = 0; i < nbWolves; i++) {
            roles.add(Role.WEREWOLF);
        }
    
        List<Role> specialRoles = Arrays.asList(
            Role.WITCH,
            Role.CUPIDO,
            Role.HUNTER,
            Role.LITTLE_GIRL,
            Role.THIEF,
            Role.FORTUNE_TELLER
        );
    
        for (Role role : specialRoles) {
            if (roles.size() < nbPlayers) {
                roles.add(role);
            }
        }
    
        while (roles.size() < nbPlayers) {
            roles.add(Role.ORDINARY_TOWNSFOLK);
        }
    
        Collections.shuffle(roles);
    }
    

    /*
    * This method allows to distribute to players.
    */
    public void distributeCharacterToPlayer() {
        if (roles.size() < players.size()) {
            throw new IllegalStateException("Not enough roles for the number of players.");
        }
    
        for (Player player : players) {
            Role assignedRole = roles.pop();
            Team assignedTeam = null;
          switch (assignedRole) {
            case WEREWOLF:
                assignedTeam = Team.Wolf;
                break;
            default:
                assignedTeam = Team.Villagers;
                break;
          }
            player.setRole(assignedRole);
            player.setTeam(assignedTeam);
            player.setRoleBehavior(createBehaviorFromRole(assignedRole));
        }
    }






    

    public void addPlayers() {
        Player alice = new Player("Alice", false, null, 0, false, null, null);
        players.add(alice);
    
        Player bob = new Player("Bob", false, null, 0, false, null,null);
        players.add(bob);
    
        Player charlie = new Player("Charlie", false, null, 0, false, null, null);
        players.add(charlie);
    
        Player diana = new Player("Diana", false, null, 0, false, null, null);
        players.add(diana);
    }
    

    public void cupidoMoment() {
       loop: for (Player player : players) {
            if (player.getRole() == Role.CUPIDO) {
    
                System.out.println("Choose 2 different players to fall in love:");
            
                for (int i = 0; i < players.size(); i++) {
                    System.out.println(i + ": " + players.get(i).getName());
                }
            
                int selectedPlayer1 = -1;
                int selectedPlayer2 = -1;
        
                while (true) {
                    System.out.print("Enter index for the first player: ");
                    selectedPlayer1 = scanner.nextInt();
                    if (selectedPlayer1 >= 0 && selectedPlayer1 < players.size()) break;
                    System.out.println("Invalid index. Try again.");
                }
            
                while (true) {
                    System.out.print("Enter index for the second player: ");
                    selectedPlayer2 = scanner.nextInt();
                    if (selectedPlayer2 >= 0 && selectedPlayer2 < players.size() && selectedPlayer2 != selectedPlayer1) break;
                    System.out.println("Invalid index or same as first. Try again.");
                }
            
                Player lover1 = players.get(selectedPlayer1);
                Player lover2 = players.get(selectedPlayer2);
            
                System.out.println("You selected " + lover1.getName() + " - " + lover2.getName());
            
                put2PeopleinLove(lover1, lover2);
            
                break loop;
            }
        }


      
    }

    public void wereWolf() {
    System.out.println("Werewolves wake up...");
    System.out.println("Choose someone:");

    int index = 0;
    List<Player> potentialVictims = new ArrayList<>();

    for (Player player : players) {
        if (player.getRole() != Role.WEREWOLF) {
            System.out.println(index + " : " + player.getName());
            potentialVictims.add(player);
            index++;
        }
    }

    int someoneChosen = -1;
    Player victim = null;

    System.out.print("Your choice: ");
    try {
        someoneChosen = scanner.nextInt();

        if (someoneChosen >= 0 && someoneChosen < potentialVictims.size()) {
            victim = potentialVictims.get(someoneChosen);
            System.out.println("You chose: " + victim.getName());
        } else {
            System.out.println("Invalid index. No one is killed.");
        }
    } catch (InputMismatchException e) {
        System.out.println("Invalid input (not a number). No one is killed.");
    }
}

    public void loversReveal(){
    System.out.println("Here we have the lovers :");
    System.out.print(playerInLove[0].getName()+ " - " + playerInLove[1].getName());
}
    
    public void littleGirlMoment(){

        System.out.println("Wake up little girl ...");
        System.out.println("You can see the role of the player chosen");
        int index = 0;
        for (Player player : players) {
            System.out.println(index + " : " + player.getName());
            index++;
        }

        int selectedPlayer = -1;
    
        while (true) {
            System.out.print("Enter index for the player that you want to see : ");
            selectedPlayer = scanner.nextInt();
            if (selectedPlayer >= 0 && selectedPlayer < players.size()) break;
            System.out.println("Invalid index. Try again.");
        }


        if (players.get(selectedPlayer).isDead()) {
            System.out.println("This player is dead.");
        } else {
            System.out.println("player : "+ players.get(selectedPlayer).getName()+ " \nrole : "+ players.get(selectedPlayer).getRole());

        }
    
    }



    public void displayListPlayer(List<Player>list){
            int index=  0;
            for (Player player : list) {
                System.out.println(index+". : "+ player.getName());
                index++;
            }
    }


    public void startNightPhase() {
        System.out.println("🌙 Night falls...");
    

        List<Role> activeNightRoles = Arrays.asList(
            Role.WITCH,
            Role.WEREWOLF,
            Role.LITTLE_GIRL
        );

        for (Player player : players) {
            if (!player.isDead() && activeNightRoles.contains(player.getRole())) {
                player.getRoleBehavior().doNightAction(this, player);
            }
        }
    }


    public void setLastVictim(Player player) {
        this.lastVictim = player;
    }



    private RoleBehavior createBehaviorFromRole(Role role) {
        switch (role) {
            case WITCH:
                return new WitchBehavior();
            case CUPIDO:
                return new CupidoBehavior();
            case WEREWOLF:
                return new WerewolvesBehavior();
            case ORDINARY_TOWNSFOLK:
                return new Ordinary_TownsfolkBehavior();
            case HUNTER:
                return new HunterBehavior();
            case THIEF:
                return new ThiefBehavior();
            case LITTLE_GIRL:
                return new LittleGirlBehavior();
            default:
                return null;
        }
    }

        public void runWithTimeout(Runnable action, int seconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<?> future = executor.submit(action);
        System.out.println("You have "+ seconds+ " seconds for acting");
        try {
            future.get(seconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true); 
            System.out.println("⏰ Temps écoulé ! L'action a été ignorée.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
        }
    }
    
    
    

}