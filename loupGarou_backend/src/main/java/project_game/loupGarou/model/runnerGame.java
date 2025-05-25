package project_game.loupGarou.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.comments.CommentLine;



@Component
public class runnerGame implements CommandLineRunner{


    
    private Game game = new Game();

    @Override
    public void run(String... args) throws Exception {
        game.playGame();
        
    }



    
}
