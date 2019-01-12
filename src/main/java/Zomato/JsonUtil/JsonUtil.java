package Zomato.JsonUtil;

import Zomato.DTO.Dto;
import Zomato.controller.TestController;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
@Component
public class JsonUtil{
    public <T extends Dto> T convertToObject(String json,Class<T> type)
    {
        return new Gson().fromJson(json,type);
    }


}
