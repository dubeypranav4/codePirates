package Zomato.controller;

import Zomato.service.couchbase.CouchBaseDAO;
import com.mongodb.util.JSON;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Controller
public class TestController {


    @Autowired
    CouchBaseDAO couchBaseDAO;

    @ResponseBody
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String getPricingSessionResponseXml() {

        String response = "";

        return "done";
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
    @RequestMapping(value = "getPredictedRestaurants/{userId}",method = RequestMethod.GET,produces = "application/json")
    public String getPredictedRestaurants(@PathVariable String userId){
        JSONObject object = new JSONObject();
        object.put("results",couchBaseDAO.getPredictedRestaurant(userId));

        return object.toString().replaceAll(Pattern.quote("\\"),Pattern.quote(""));


    }

//    @RequestMapping(value = "generateDegreesAndDistance",method = RequestMethod.GET)
//    public String generateDegreesAndDistance(){
//        return couchBaseDAO.generateDegreesAndDistance();
//    }




}
