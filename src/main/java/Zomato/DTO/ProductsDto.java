package Zomato.DTO;

public class ProductsDto extends Dto {

    private String id;
    private String name;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ProductsDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }
}
