package sg.edu.nus.iss.paf_day26_workshop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.paf_day26_workshop.model.Game;
import sg.edu.nus.iss.paf_day26_workshop.repository.GameRepo;

@Service
public class GameService {
    
    @Autowired
    GameRepo repo;

    public List<Game> listGames(Integer limit, Integer offset){
        return repo.getAllGames(limit, offset);
    }

    public long countGames(){
        return repo.countGames();
    }

    public List<Game> listGameByRank(Integer limit, Integer offset){
        return repo.getGamesByRank(limit, offset);
    }

    public Document listGameById(Integer id){
        return repo.getGameById(id);
    }

    public Integer getSumOfRatings(Integer id){
        return repo.getSumOfRatings(id);
    }

    public long countComments(Integer id){
        return repo.countComments(id);
    }

    // create a returnJson method for task a & b 
    public JsonObject createReturnJson(List<Game> list, Integer limit, Integer offset){
         JsonArrayBuilder gamesArray = Json.createArrayBuilder();
        for (Game game : list) {
            JsonObject object = Json.createObjectBuilder()
                            .add("game_id", game.getId())
                            .add("name", game.getName())
                            .build();
            gamesArray.add(object);
        }

        // display according to task (a)
        // can try to transfer to service class
        JsonObject returnJson = Json.createObjectBuilder()
                .add("games", gamesArray.build())
                .add("limit", limit)
                .add("offset", offset)
                .add("total", countGames())
                .add("timestamp", LocalDateTime.now().toString())
                .build();
        
        return returnJson;
    }
}
