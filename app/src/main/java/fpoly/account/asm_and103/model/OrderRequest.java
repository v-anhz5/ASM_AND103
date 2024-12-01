package fpoly.account.asm_and103.model;

import java.util.ArrayList;

public class OrderRequest {

    private String userId;
    private ArrayList<Product> products;

    public OrderRequest(String userId, ArrayList<Product> products) {
        this.userId = userId;
        this.products = products;
    }

    public OrderRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}

