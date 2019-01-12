package Zomato.controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class TestController {

    @ResponseBody
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String getPricingSessionResponseXml() {

        String response = "";
        Logger logger = LogManager.getRootLogger();
        logger.info("logger working fine");

        return "done";
    }



}
