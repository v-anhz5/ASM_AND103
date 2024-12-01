package fpoly.account.asm_and103.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Product implements Parcelable {
    String productID, productName, productImage, productDescription,categoryid;
    double productPrice;

    public Product() {
    }



    public Product(String productID, String productName, String productImage, String productDescription, String categoryid, double productPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.categoryid = categoryid;
        this.productPrice = productPrice;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    protected Product(Parcel in) {
        productID = in.readString();
        productName = in.readString();
        productDescription = in.readString();
        productPrice = in.readDouble();
        productImage = in.readString();
        categoryid = in.readString();
    }


    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(productID);

        dest.writeString(productName);
        dest.writeString(productDescription);

        dest.writeDouble(productPrice);
        dest.writeString(productImage);
        dest.writeString(categoryid);
    }
}
