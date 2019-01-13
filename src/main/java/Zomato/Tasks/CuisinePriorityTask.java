package Zomato.Tasks;

import Zomato.BeanFactory.ZomatoBeanFactory;
import Zomato.DTO.OrdersDto;
import Zomato.DTO.ProductsDto;
import Zomato.Result;
import Zomato.service.couchbase.CouchBaseDAO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class CuisinePriorityTask implements Callable<String> {
    private ArrayList<String> email;
    private OrdersDto ordersDto;
    private CouchBaseDAO couchBaseDAO;
    static HashMap<String, Result> resultTillNow= new HashMap<>();
    public CuisinePriorityTask(ArrayList<String> email)
    {this.email=email;
        couchBaseDAO=(CouchBaseDAO)ZomatoBeanFactory.getBean("couchBaseDAO");
       this.ordersDto=new Gson().fromJson(couchBaseDAO.get("orders",email.get(email.size()-1)),OrdersDto.class);

    }
    public CuisinePriorityTask()
    {
    }

    @Override
    public String call() throws Exception {
    ArrayList<ProductsDto> cuisine =ordersDto.getProducts();
    Result lastResult=copyfrom(findlasResult(email));

   for(int i=0;i<cuisine.size();i++)
   {
     if(lastResult.getResult().containsKey(cuisine.get(i).getName()))
     {int temp=lastResult.getResult().get(cuisine.get(i).getName());
     temp++;
         lastResult.getResult().put(cuisine.get(i).getName(),temp);
     }
     else lastResult.getResult().put(cuisine.get(i).getName(),1);

   }
   resultTillNow.put(getKey(email),lastResult);
return "Success";
    }




    public Result findlasResult(ArrayList<String> emailList)
    {


     for(int i=0;i<emailList.size()-1;i++)
    {
    while(i>=0&&!resultTillNow.containsKey(getKey(emailList.subList(0,i))))i--;

    if(i>=0)return resultTillNow.get(getKey(emailList.subList(0,i)));

    else return formResult(new Gson().fromJson(couchBaseDAO.get("orders",email.get(0)),OrdersDto.class).getProducts());
    }

return formResult(new Gson().fromJson(couchBaseDAO.get("orders",email.get(0)),OrdersDto.class).getProducts());
    }
    public String getKey (List<String> keys)
    {
        StringBuilder key=new StringBuilder("");
        Collections.sort(keys);
        for(int i=0;i<keys.size();i++)
            key.append(keys.get(i));
        return key.toString();
    }
    public Result formResult(ArrayList<ProductsDto> products)
    {
        Result result=new Result();
        for(int i=0;i<products.size();i++)
            if(result.getResult().containsKey(products.get(i).getName()))
            {Integer counter=result.getResult().get(products.get(i).getName());
            counter++;
                result.getResult().put(products.get(i).getName(),counter);
            }
            else result.getResult().put(products.get(i).getName(),1);
    return result;
    }
    public Result copyfrom(Result temp)
    {
        Result result =new Result();
        result.getResult().putAll(temp.getResult());
         return result;
    }
}
