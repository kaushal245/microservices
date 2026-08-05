package com.example.userservice.service;

import com.example.userservice.client.ProductClient;
import com.example.userservice.dto.ProductDTO;
import com.example.userservice.dto.UserWithProductDTO;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductClient productClient;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // This is the cross-service call: User Service -> (Feign) -> Product Service
    public UserWithProductDTO getUserWithProduct(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        ProductDTO product = null;
        if (user.getProductId() != null) {
            try {
                product = productClient.getProductById(user.getProductId());
            } catch (FeignException.NotFound ex) {
                product = null; // product no longer exists
            } catch (FeignException ex) {
                // product-service is down or unreachable
                throw new RuntimeException("Product service unavailable: " + ex.getMessage());
            }
        }

        return new UserWithProductDTO(user.getId(), user.getName(), user.getEmail(), product);
    }
}
