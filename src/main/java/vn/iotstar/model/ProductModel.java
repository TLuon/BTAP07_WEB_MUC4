package vn.iotstar.model;

import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;
import java.util.Date;

public class ProductModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int productId;
    private String productName;
    private String description;
    private double unitPrice;
    private double price;
    private double discount;
    private int quantity;
    private int stock;
    private int status = 1;
    private Integer categoryId;
    private String categoryName;
    private Date createDate;
    private MultipartFile imageFile;
    private String images;

    public ProductModel() {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice > 0 ? unitPrice : price;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.price = unitPrice;
    }

    public double getPrice() {
        return price > 0 ? price : unitPrice;
    }

    public void setPrice(double price) {
        this.price = price;
        this.unitPrice = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity > 0 ? quantity : stock;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.stock = quantity;
    }

    public int getStock() {
        return stock > 0 ? stock : quantity;
    }

    public void setStock(int stock) {
        this.stock = stock;
        this.quantity = stock;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
