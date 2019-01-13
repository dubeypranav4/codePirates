package Zomato.service.couchbase;

import com.google.gson.Gson;
import com.mongodb.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.regex.Pattern;

@Repository
public class CouchBaseDAO {

	@Value("${mongo.cluster}")
	private  String clusterName;// = "0.0.0.0:8091";

	@Value("${mongo.cluster.username}")
	private String username;// = "admin";

	@Value("${mongo.cluster.password}")
	private String password;// = "codepir";

	@Value("${mongo.dbname}")
	private String dbName;// = "TestBucket";

	@Value("${mongo.db.collections}")
	private String collectionsString;

	private MongoClient mongoClient;

	private DB db ;

	private Map<String,DBCollection> collections;

	public static String USER_EMAIL = "email";
	public static String USER_NAME = "name";
	public static String USER_MOBILE = "mobile";

	//	private static Logger logger = LogManager.getRootLogger();
	@PostConstruct
	private void init(){
		try {
			mongoClient = new MongoClient(new MongoClientURI(clusterName));
			db = mongoClient.getDB(dbName);
			collections = new HashMap<>();
			String[] split = collectionsString.split(Pattern.quote(":"));
			Arrays.asList(split).stream().forEach(t -> {
				collections.put(t,db.getCollection(t));
			});
			System.out.println(collections);
		}catch (Exception e){
			System.out.println("Error in connecting to ");
		}


	}


	public void insert(String collectionName,String jsonString){
		DBCollection dbCollection = collections.get(collectionName);
		JSONObject object =new JSONObject(jsonString);
		BasicDBObject document = new BasicDBObject();
		document.put(USER_EMAIL,object.get(USER_EMAIL));
		document.put(USER_NAME,object.get(USER_NAME));
		document.put(USER_MOBILE,object.get(USER_MOBILE));
		dbCollection.insert(document);
	}

	public String get(String collectionName,String userId){
		DBCollection dbCollection = collections.get(collectionName);
		BasicDBObject basicDBObject = new BasicDBObject(USER_EMAIL, userId);
		return dbCollection.find(basicDBObject).one().toString();
	}







	public void insertDocument(String bucketName){
		// Create a JSON Document



	}
	@PreDestroy
	private void free(){

		System.out.println("Cluster " + clusterName + " disconnected");
	}

	public String diagnosis() {
		return db.getName();
	}

	public JSONArray getPredictedRestaurant(String userId) {
		DBCollection restaurants = collections.get("Restaurant");
		int count = (int) restaurants.count();
		int skipCount = 0;
		if (count>20){
			count = count - 20;
			Random random = new Random();
			skipCount = random.nextInt(count) + 1;
		}
		DBCursor cursor = restaurants.find().skip(skipCount).limit(20);
		JSONArray restaurantDetails = new JSONArray();
		while(cursor.hasNext()){
			DBObject next = cursor.next();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("name",next.get("name"));
			jsonObj.put("geometry",next.get("geometry"));
			jsonObj.put("rating",next.get("rating"));
			restaurantDetails.put(jsonObj);


		}
		return restaurantDetails;
	}

//	public String generateDegreesAndDistance() {
//		DBCollection restaurant = collections.get("Restaurant");
//		int batch_size = 20;
//		int init = 0;
//		int counter_processed = 0;
//		DBCursor cursor = restaurant.find().limit(20);
//		while(cursor!= null){
//			while(cursor.hasNext()){
//				DBObject next = cursor.next();
//				JSONObject geometry = (JSONObject) next.get("geometry");
//				String lat = String.valueOf(geometry.get("lat"));
//				String lng = String.valueOf(geometry.get("lng"));
//
//
//			}
//		}
//	}

	public String generateCuisineTable(){
		DBCollection restaurantCollection = collections.get("Restaurant");
		DBCollection cui2RestCollection = collections.get("Cui2Rest");
		int processedThisTime = 0;
		DBCursor dbObjects = restaurantCollection.find();
		while (dbObjects.hasNext()){
			DBObject next = dbObjects.next();
			if (next.get("isProcessed") == null){
				try {
					process(cui2RestCollection, next);
					BasicDBObject appendObject = new BasicDBObject();
					appendObject.append("$set", new BasicDBObject().append("isProcessed", 1));
					BasicDBObject searchQuery = new BasicDBObject().append("_id", next.get("_id"));
					restaurantCollection.update(searchQuery, appendObject);
					++processedThisTime;
				}catch (Exception e){
					System.out.println(e);
				}

			}else{
				continue;
			}
		}
		return "processed items count : " + processedThisTime;
	}

	private void process(DBCollection cui2RestCollection, DBObject next) {
		String cuisines = String.valueOf(next.get("cuisines"));
		cuisines = cuisines.replaceAll(Pattern.quote("["),"");
		cuisines = cuisines.replaceAll(Pattern.quote("]"),"");
		cuisines = cuisines.replaceAll(Pattern.quote("\""),"");


		String restaurantName = String.valueOf(next.get("name"));
		String restaurantRating = String.valueOf(next.get("rating"));
		int res_id = Double.valueOf(String.valueOf(next.get("res_id"))).intValue();
		List<String> strings = Arrays.asList(cuisines.split(Pattern.quote(",")));
		for (String cuisine : strings){
			cuisine = cuisine.trim();
			BasicDBObject queryObj = new BasicDBObject("cuisine",cuisine);
			DBObject one = cui2RestCollection.findOne(queryObj);
			if (one != null){
				DBObject restaurantItem = new BasicDBObject("restaurants",new BasicDBObject("name",restaurantName).append("rating",restaurantRating).append("res_id",res_id));
				DBObject updatequery = new BasicDBObject("$push",restaurantItem);
				cui2RestCollection.update(new BasicDBObject("_id",one.get("_id")),updatequery);

			}else{
				BasicDBList restList = new BasicDBList();
				restList.add(new BasicDBObject("name",restaurantName).append("rating",restaurantRating).append("res_id",res_id));
				DBObject cuisineEntry = new BasicDBObject("cuisine",cuisine).append("restaurants",restList);
				cui2RestCollection.insert(cuisineEntry);
			}
		}


	}

	public void preDestroy(){
		mongoClient.close();
	}


}
