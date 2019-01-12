package Zomato.DTO;

import java.util.ArrayList;

public class OrdersDto extends Dto {
    private String emailId;
    private ArrayList<ProductsDto> products;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public ArrayList<ProductsDto> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "OrdersDto{" +
                "emailId='" + emailId + '\'' +
                ", products=" + products +
                '}';
    }

    public void setProducts(ArrayList<ProductsDto> products) {
        this.products = products;
    }
}
