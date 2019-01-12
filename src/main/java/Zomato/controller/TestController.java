package Zomato.controller;

import Zomato.service.couchbase.CouchBaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
        return couchBaseDAO.diagnos();
    }


}
