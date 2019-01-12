package Zomato.controller;

import Zomato.service.couchbase.CouchBaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "insert",method = RequestMethod.POST)
    public String insert(@RequestBody String body){
        System.out.println(body);
        try {
            couchBaseDAO.insert("Users", body);
            return "Successfully inserted";
        }catch (Exception e){
            return "Unsuccessful" + e;
        }
    }

    @ResponseBody
    @RequestMapping(value = "getData/{id}",method = RequestMethod.GET)
    public String getData(@PathVariable("id") String userId){
        return couchBaseDAO.get("Users",userId);
    }


}
