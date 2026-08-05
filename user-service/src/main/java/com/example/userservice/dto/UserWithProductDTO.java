package com.example.userservice.dto;

public class UserWithProductDTO {

    private Long userId;
    private String name;
    private String email;
    private ProductDTO product;

    public UserWithProductDTO(Long userId, String name, String email, ProductDTO product) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.product = product;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ProductDTO getProduct() {
        return product;
    }
}
