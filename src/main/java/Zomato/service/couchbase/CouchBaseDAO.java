package Zomato.service.couchbase;

import com.mongodb.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
}
