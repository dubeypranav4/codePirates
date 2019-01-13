package Zomato;

import java.util.HashMap;

public class Result {

    private HashMap<String,Integer> result;
    public Result()
    {result=new HashMap<>();
    }

    public HashMap<String, Integer> getResult() {
        return result;
    }

    public void setResult(HashMap<String, Integer> result) {
        this.result = result;
    }
}
