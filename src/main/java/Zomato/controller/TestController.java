package Zomato.controller;

import Zomato.DTO.OrderRequest;
import Zomato.JsonUtil.JsonUtil;
import Zomato.Tasks.CuisinePriorityTask;
import Zomato.service.couchbase.CouchBaseDAO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;

@Controller
public class TestController {

    @Autowired
    @Qualifier("TaskExecutor")
    ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    CouchBaseDAO couchBaseDAO;
    @Autowired
    JsonUtil jsonUtil;
    @ResponseBody
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String getPricingSessionResponseXml() {
        try {
            String response ="{\n" +
                    "  \"emailId\": \"hsalatinorr@instagram.com\",\n" +
                    "  \"products\": [\n" +
                    "    {\n" +
                    "      \"name\": \"North Indian\",\n" +
                    "      \"id\": 311\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"Chinese\",\n" +
                    "      \"id\": 315\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"Fast Food\",\n" +
                    "      \"id\": 748\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"Italian\",\n" +
                    "      \"id\": 172\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"Continental\",\n" +
                    "      \"id\": 85\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
   return "done";
        } catch (Exception e) {
            System.out.println("Exception caught");
        return "Error";
        }
   }

    @ResponseBody
    @RequestMapping(value = "checkCouch",method = RequestMethod.GET)
    public String getDiagnosis(){
       return couchBaseDAO.diagnosis();
    }

    @ResponseBody
    @RequestMapping(value = "insertUserDetails",method = RequestMethod.POST)
    public String insertUserDetails(@RequestBody String body){
        System.out.println(body);
        try {
            couchBaseDAO.insert("Users", body);
            return "Successfully inserted";
        }catch (Exception e){
            return "Unsuccessful" + e;
        }
    }

    @ResponseBody
    @RequestMapping(value = "getUserDetails/{id}",method = RequestMethod.GET)
    public String getUserDetails(@PathVariable("id") String userId){
        return couchBaseDAO.get("Users",userId);
    }
    @ResponseBody
    @RequestMapping(value = "order",method = RequestMethod.POST)
   public String getOrder(@RequestBody ArrayList<OrderRequest> requestpojo){
   try{ArrayList<String > email =new ArrayList<String>();
            for(int i=0;i<requestpojo.size();i++)
                email.add(requestpojo.get(i).getUserid());
        Future<String> response=threadPoolExecutor.submit(new CuisinePriorityTask(email));
        if(response.isDone())System.out.println(response.get());

    }
    catch(Exception e)
    {System.out.println("Exception occured while submitting task "+e);
    }
   return "done";
    }

    @ResponseBody
    @RequestMapping(value = "getPredictedRestaurants/{userId}",method = RequestMethod.POST,produces = "application/json")
    public String getPredictedRestaurants(@PathVariable String userId,@RequestBody String body){
        JSONObject object = new JSONObject();

        object.put("results",couchBaseDAO.getPredictedRestaurant(userId));

        return object.toString().replaceAll(Pattern.quote("\\"),Pattern.quote(""));


    }

    @ResponseBody
    @RequestMapping(value = "generateCuisineTable",method = RequestMethod.GET)
    public String generateCuisineTable(){
        try{
            return couchBaseDAO.generateCuisineTable();
        }catch (Exception e){
            return "Unsuccessful";
        }
    }

//    @RequestMapping(value = "generateDegreesAndDistance",method = RequestMethod.GET)
//    public String generateDegreesAndDistance(){
//        return couchBaseDAO.generateDegreesAndDistance();
//    }


    }



