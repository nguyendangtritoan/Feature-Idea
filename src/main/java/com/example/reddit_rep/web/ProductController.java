package com.example.reddit_rep.web;

import com.example.reddit_rep.domain.Product;
import com.example.reddit_rep.domain.User;
import com.example.reddit_rep.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products/{productId}")
    public String getProduct(@PathVariable Long productId, ModelMap modelMap, HttpServletResponse response) throws IOException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            modelMap.put("product", product);
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value(), "Product with id " + productId + " was not found");
            return "product";
        }
        return "product";
    }

    @PostMapping("products/{productId}")
    public String saveProduct(@PathVariable Long productId, Product product) {

        product = productRepository.save(product);

        return "redirect:/products/" + product.getId();
    }

    @PostMapping("/products")
    public String createProduct(@AuthenticationPrincipal User user) {
        Product product = new Product();

        product.setPublished(false);
        product.setUser(user);

        product = productRepository.save(product);

        return "redirect:/products/" + product.getId();
    }
}
