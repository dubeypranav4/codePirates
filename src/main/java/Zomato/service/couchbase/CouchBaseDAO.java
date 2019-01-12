package Zomato.service.couchbase;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CouchBaseDAO {

	@Value("${couchbase.cluster}")
	private  String clusterName;// = "0.0.0.0:8091";

	@Value("${couchbase.cluster.username}")
	private String username;// = "admin";

	@Value("${couchbase.cluster.password}")
	private String password;// = "codepir";

	@Value("${couchbase.default.bucket}")
	private String bucketName;// = "TestBucket";

	private Cluster cluster = null;

	private Map<String, Bucket> buckets = null;

//	private static Logger logger = LogManager.getRootLogger();
	@PostConstruct
	private void init(){
//		logger.log(Level.INFO,"Trying to connect to couchbase cluster : " + clusterName);
		System.out.println(username + " " + clusterName + " " + password);
		cluster = CouchbaseCluster.create(clusterName);
		buckets = new HashMap<>();
		cluster.authenticate(username,password);
		openBucket(bucketName);
//		logger.log(Level.INFO,"Successfully connected to couchbase cluster :" + clusterName);
//		logger.log(Level.INFO,cluster.diagnostics().toString());

	}


	public String diagnos(){
		return cluster.diagnostics().toString();
	}

	private Bucket openBucket(String bucketName) {
		if(cluster == null){
//			logger.log(Level.INFO,"cluster not initialized ... run init");
			return null;
		}

		if (buckets.containsKey(bucketName)){
			return buckets.get(bucketName);
		}
		Bucket bucket = null;
		try {
			bucket = cluster.openBucket(bucketName);
		}catch (Exception e){
//			logger.log(Level.ERROR,e);
			return null;
		}

		buckets.put(bucketName,bucket);
		return bucket;
	}



	@PreDestroy
	private void free(){
//		buckets.entrySet().stream().forEach(t -> {t.getValue().close();logger.log(Level.INFO,"bucket " + t.getKey() + " closed");});
		buckets = null;
		cluster.disconnect();
		cluster = null;
//		logger.info("Cluster " + clusterName + " disconnected");
	}
}
