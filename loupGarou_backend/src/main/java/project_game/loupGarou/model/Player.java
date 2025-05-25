package project_game.loupGarou.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class Player{
    private String name;
    private boolean dead;
    private Team team;
    private int voted;
    private boolean elected;
    private Role role;
    private RoleBehavior roleBehavior;


    public void setVoted(int vote){
        voted = voted + vote;
    }

    public void vote(Player player, int voteNumber){
        player.voted += voteNumber;
    }
}