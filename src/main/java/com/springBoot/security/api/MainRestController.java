package com.springBoot.security.api;

import com.springBoot.security.auth.AuthUserUtil;
import com.springBoot.security.model.Order;
import com.springBoot.security.model.Product;
import com.springBoot.security.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
@RequiredArgsConstructor
public class MainRestController extends AuthUserUtil {

    private final CustomerService customerService;

    @GetMapping(value = "/allorders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Order>> getOrders() {
        List<Order> orders = customerService.sortOrderByStatus(false);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping(value = "/searchproduct")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Product>> searchProduct(@CookieValue(value = "category") String category,
                                                       @RequestParam(name = "productName") String productName) {
//        List<Product> products;
//        if (!category.equals("0")) {
//            products = customerService.sortCategoryById(Long.parseLong(category));
//        } else {
//            products = customerService.getProducts();
//        }
//        List<Product> searchProduct = new ArrayList<>();
//        if (!productName.equals("")) {
//            for (Product p : products) {
//                if (p.getName().equalsIgnoreCase(productName)) {
//                    searchProduct.add(p);
//                }
//            }
//        } else {
//            searchProduct = customerService.getProducts();
//        }
        List<Product> products = customerService.searchProduct(productName);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
