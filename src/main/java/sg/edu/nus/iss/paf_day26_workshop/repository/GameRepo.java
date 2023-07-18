package sg.edu.nus.iss.paf_day26_workshop.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.paf_day26_workshop.model.Game;

@Repository
public class GameRepo {
    
    // import MongoTemplate to connect to Mongo
    @Autowired
    private MongoTemplate template;

    // write a query to list all games
    public List<Game> getAllGames(Integer limit, Integer offset){


        // set the criteria/specification 
        Criteria c = Criteria.where("");

        // write the query (requires a criteria parameter)
        // .limit() -> set limit of generated query 
        // .with() -> additional filter 
        // use .with(Sort.by("gid")) -> to sort by ascending order for "gid" field
        // use .skip() -> for offset to 'skip' a certain number of fields
        Query q = Query.query(c).limit(limit).skip(offset).with(Sort.by("gid"));

        // (query, class type, collection name)
        List<Document> list = template.find(q, Document.class, "game");

        List<Game> gameList = new ArrayList<>();

        // looping through the first list of all document objects with all attributes, then creating new objects with only required attributes and adding it into another list to retun 
        for (Document d : list) {
            // creating a new object that retrieves only the gid and name from mongo db 
            // note: the attribute name has to match mongo db
            Game game = new Game(d.getInteger("gid"), d.getString("name"));
            gameList.add(game);
        }
    
        return gameList;

    }

    // write a query to count total number of games 
    public long countGames(){

        Criteria c = Criteria.where("");

        Query q = Query.query(c);

        return template.count(q, "game");
    }

        // write a query to list all games by rank
    public List<Game> getGamesByRank(Integer limit, Integer offset){

        Criteria c = Criteria.where("");

        Query q = Query.query(c).limit(limit).skip(offset).with(Sort.by("ranking"));

        List<Document> list = template.find(q, Document.class, "game");

        List<Game> gameList = new ArrayList<>();

        
        for (Document d : list) {
            Game game = new Game(d.getInteger("gid"), d.getString("name"));
            gameList.add(game);
        }
    
        return gameList;

    }

    // create a query for returning game details based on id 
    public Document getGameById(Integer id){

        // .is(id) -> match whatever field we want to check with mongo 
        Criteria c = Criteria.where("gid").is(id);

        Query q = Query.query(c);

        // template.findOne -> return 1 result 
        return template.findOne(q, Document.class, "game");
    }

    // create a query to retrieve every rating for each game
    public Integer getSumOfRatings(Integer id){

        Criteria c = Criteria.where("gid").is(id);
        Query q = Query.query(c);

        List<Document> list = template.find(q, Document.class, "comment");

        List<Integer> ratingList = new ArrayList<>();
        
        for (Document doc: list) {
            ratingList.add(doc.getInteger("rating"));
        }

        Integer sum = 0;
        for (Integer integer : ratingList) {
            sum+= integer;
        }
        return sum;
    }


    public long countComments(Integer id){

        Criteria c = Criteria.where("gid").is(id);

        Query q = Query.query(c);

        return template.count(q, "comment");
    }

}
