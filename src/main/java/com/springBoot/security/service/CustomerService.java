package com.springBoot.security.service;

import com.springBoot.security.model.*;
import com.springBoot.security.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CityRepository cityRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final CountOfProductRepository countOfProductRepository;

    public void addCity(City city) {
        cityRepository.save(city);
    }

    public City getCity(Long id) {
        return cityRepository.findById(id).orElse(null);
    }

    public List<City> getCities() {
        return cityRepository.findAll();
    }

    public void deleteCity(City city) {
        cityRepository.delete(city);
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category getCategory(Long id) {
        return categoryRepository.getById(id);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.getById(id);
    }

    public List<Product> getProducts() {
        return productRepository.findAllByIdNotNullOrderById();
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer getCustomer(Long id) {
        return customerRepository.getById(id);
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }

    public void addOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrder(Long id) {
        return orderRepository.getById(id);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    public void addCountOfProduct(CountOfProduct countOfProduct) {
        countOfProductRepository.save(countOfProduct);
    }

    public CountOfProduct getCountOfProduct(Long id) {
        return countOfProductRepository.getById(id);
    }

    public List<CountOfProduct> countOfProducts() {
        return countOfProductRepository.findAll();
    }

    public void deleteCountOfProduct(CountOfProduct countOfProduct) {
        countOfProductRepository.delete(countOfProduct);
    }

    public List<Product> sortCategoryById(Long id) {
        return productRepository.findAllByCategory_Id(id);
    }

    public List<Order> sortOrderByStatus(boolean status) {
        return orderRepository.findAllByStatus(status);
    }
    public List<Product> searchProduct(String keyword){
        return productRepository.findByNameIsContainingIgnoreCase(keyword);
    }
}
