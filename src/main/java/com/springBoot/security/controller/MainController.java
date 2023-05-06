package com.springBoot.security.controller;

import com.springBoot.security.auth.AuthUserUtil;
import com.springBoot.security.model.*;
import com.springBoot.security.repository.AuthUserRepository;
import com.springBoot.security.service.AuthUserService;
import com.springBoot.security.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController extends AuthUserUtil {

    private final AuthUserService authUserService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthUserRepository userRepository;
    private final CustomerService customerService;

    @GetMapping(value = "/")
    public String mainPage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "signin";
    }

    @GetMapping(value = "/home")
    @PreAuthorize("isAuthenticated()")
    public String signInPage(Model model, HttpServletResponse response) {
        Cookie cookie = new Cookie("category", "0");
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.setContentType("text/plain");
        response.addCookie(cookie);
        model.addAttribute("currentUser", getCurrentUser());
        return "home";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "profile";
    }

    @GetMapping(value = "/adminpanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String adminPanelPage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "adminpanel";
    }

    @GetMapping(value = "/accessdenied")
    public String accessDeniedPage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "accessdenied";
    }

    @GetMapping(value = "/signuppage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String signUpPage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "/signup";
    }

    @GetMapping(value = "/citypage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addCityPage(Model model) {
        List<City> cities = customerService.getCities();
        model.addAttribute("cities", cities);
        model.addAttribute("currentUser", getCurrentUser());
        return "/citypage";
    }

    @GetMapping(value = "/citydetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String cityDetails(Model model, @PathVariable(name = "id") Long id) {
        City city = customerService.getCity(id);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("city", city);
        return "citydetails";
    }

    @GetMapping(value = "/customerpage")
    @PreAuthorize("isAuthenticated()")
    public String customerPage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        List<City> cities = customerService.getCities();
        List<Customer> customers = customerService.getCustomers();
        model.addAttribute("cities", cities);
        model.addAttribute("customers", customers);
        return "customerpage";
    }

    @GetMapping(value = "/customerdetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String customerDetails(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("currentUser", getCurrentUser());
        Customer customer = customerService.getCustomer(id);
        List<City> cities = customerService.getCities();
        model.addAttribute("customer", customer);
        model.addAttribute("cities", cities);
        return "customerdetails";
    }

    @GetMapping(value = "/categorypage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String categoryPage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        List<Category> categories = customerService.getCategories();
        model.addAttribute("categories", categories);
        return "categorypage";
    }

    @GetMapping(value = "/categorydetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String categoryDetails(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("currentUser", getCurrentUser());
        Category category = customerService.getCategory(id);
        model.addAttribute("category", category);
        return "categorydetails";
    }

    @GetMapping(value = "/productpage")
    @PreAuthorize("isAuthenticated()")
    public String productPage(Model model,
                              @CookieValue(value = "category") String category) {
        model.addAttribute("currentUser", getCurrentUser());
        List<Category> categories = customerService.getCategories();
        List<Product> products = customerService.getProducts();
        if (category!=null && !category.equals("0")) {
            products = customerService.sortCategoryById(Long.parseLong(category));
        } else {
            products = customerService.getProducts();
        }
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "productpage";
    }

    @GetMapping(value = "/addproductpage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addProductPage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        List<Category> categories = customerService.getCategories();
        model.addAttribute("categories", categories);
        return "addproductpage";
    }

    @GetMapping(value = "/productdetails/{id}")
    @PreAuthorize("isAuthenticated()")
    public String productDetails(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("currentUser", getCurrentUser());
        Product product = customerService.getProduct(id);
        model.addAttribute("product", product);
        return "productdetails";
    }

    @GetMapping(value = "/editproductpage/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editProductPage(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("currentUser", getCurrentUser());
        Product product = customerService.getProduct(id);
        List<Category> categories = customerService.getCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "editproductpage";
    }

    @GetMapping(value = "/sortproductbycategory/category={categoryId}")
    @PreAuthorize("isAuthenticated()")
    public String SortProductByCategory(Model model,
                                        @PathVariable(name = "categoryId") Long categoryId,
                                        HttpServletResponse response) {
        model.addAttribute("currentUser", getCurrentUser());
        Cookie cookie = new Cookie("category", "0");
        cookie.setPath("/");
        cookie.setValue(categoryId.toString());
        cookie.setMaxAge(86400);
        response.setContentType("text/plain");
        response.addCookie(cookie);
        return "redirect:/productpage/";
    }

    @GetMapping(value = "/orderpage")
    @PreAuthorize("isAuthenticated()")
    public String orderPage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        List<Order> orders = customerService.sortOrderByStatus(false);
        model.addAttribute("orders", orders);
        return "orderpage";
    }

    @GetMapping(value = "/addorderpage")
    @PreAuthorize("isAuthenticated()")
    public String addOrderPage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        List<Customer> customers = customerService.getCustomers();
        model.addAttribute("customers", customers);
        return "addorderpage";
    }

    @GetMapping(value = "/orderdetails/{id}&page={page}")
    @PreAuthorize("isAuthenticated()")
    public String orderDetails(Model model,
                               @PathVariable(name = "id") Long id,
                               @PathVariable(name = "page") String page,
                               @CookieValue(value = "category") String categoryId) {
        model.addAttribute("currentUser", getCurrentUser());
        Order order = customerService.getOrder(id);
        List<Category> categories = customerService.getCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("order", order);
        model.addAttribute("page", page);
        double overAllPrice = 0;
        List<CountOfProduct> products = order.getCountOfProducts();
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                overAllPrice += products.get(i).getProduct().getPrice() * products.get(i).getCount();
            }
        }
        model.addAttribute("overAllPrice", overAllPrice);
        return "orderdetails";
    }

    @GetMapping(value = "/addproductinorderpage/{id}")
    @PreAuthorize("isAuthenticated()")
    public String addProductInOrderPage(Model model,
                                        @PathVariable(name = "id") Long id,
                                        HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        model.addAttribute("currentUser", getCurrentUser());
        Order order = customerService.getOrder(id);
        List<Product> products = customerService.getProducts();
        List<Category> categories = customerService.getCategories();
        for (Cookie c : cookies) {
            if (c.getName().equals("category")) {
                if (!c.getValue().equals("0")) {
                    products = customerService.sortCategoryById(Long.parseLong(c.getValue()));
                }
            }
        }
        model.addAttribute("order", order);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "/addproductinorderpage";
    }

    @GetMapping(value = "/sortproductinorderpagebycategory/{id}&category={categoryId}&page={page}")
    @PreAuthorize("isAuthenticated()")
    public String addBySortByCategory(Model model,
                                      @PathVariable(name = "id") Long id,
                                      @PathVariable(name = "categoryId") Long categoryId,
                                      @PathVariable(name = "page") String page,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies){
            if(c.getName().equals("category")){
                c.setValue(categoryId.toString());
                c.setPath("/");
                response.addCookie(c);
            }
        }
        return "redirect:/addproductinorderpage/" + id;
    }

    @GetMapping(value = "/reports")
    @PreAuthorize("isAuthenticated()")
    public String reportsPage(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        List<Order> orders = customerService.sortOrderByStatus(true);
        model.addAttribute("orders", orders);
        return "/reportspage";
    }

    @PostMapping(value = "/addcity")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addCity(@RequestParam(name = "city_name") String name) {
        City city = new City();
        city.setName(name);
        customerService.addCity(city);
        return "redirect:/citypage";
    }

    @PostMapping(value = "/editcity")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editCity(@RequestParam(name = "id") Long id,
                           @RequestParam(name = "city_name") String name) {
        City city = new City();
        city.setId(id);
        city.setName(name);
        customerService.addCity(city);
        return "redirect:/citypage";
    }

    @PostMapping(value = "/deleteCity")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteCity(@RequestParam(name = "id") Long id) {
        City city = customerService.getCity(id);
        if (city != null) {
            customerService.deleteCity(city);
        }
        return "redirect:/citypage";
    }

    @PostMapping(value = "/addcustomer")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addcustomer(@RequestParam(name = "name") String name,
                              @RequestParam(name = "city") Long cityId) {
        City city = customerService.getCity(cityId);
        Customer customer = new Customer();
        customer.setName(name);
        customer.setCity(city);
        customerService.addCustomer(customer);
        return "redirect:/customerpage";
    }

    @PostMapping(value = "/editcustomer")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editCustomer(@RequestParam(name = "id") Long id,
                               @RequestParam(name = "customer_name") String name,
                               @RequestParam(name = "city") Long cityId) {
        Customer customer = customerService.getCustomer(id);
        City city = customerService.getCity(cityId);
        customer.setName(name);
        customer.setCity(city);
        customerService.addCustomer(customer);
        return "redirect:/customerpage";
    }

    @PostMapping(value = "/deletecustomer")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteCustomer(@RequestParam(name = "id") Long id) {
        Customer customer = customerService.getCustomer(id);
        if (customer != null) {
            customerService.deleteCustomer(customer);
        }
        return "redirect:/customerpage";
    }

    @PostMapping(value = "/addcategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addCategory(@RequestParam(name = "category_name") String name) {
        Category category = new Category();
        category.setName(name);
        customerService.addCategory(category);
        return "redirect:/categorypage";
    }

    @PostMapping(value = "/editcategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editCategory(@RequestParam(name = "id") Long id,
                               @RequestParam(name = "category_name") String name) {
        Category category = customerService.getCategory(id);
        category.setName(name);
        customerService.addCategory(category);
        return "redirect:/categorypage";
    }

    @PostMapping(value = "/deletecategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteCategory(@RequestParam(name = "id") Long id) {
        Category category = customerService.getCategory(id);
        customerService.deleteCategory(category);
        return "redirect:/categorypage";
    }

    @PostMapping(value = "/addproduct")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addProduct(@RequestParam(name = "product_name") String name,
                             @RequestParam(name = "category") Long categoryId,
                             @RequestParam(name = "description") String description,
                             @RequestParam(name = "product_price") double price) {
        Product product = new Product();
        Category category = customerService.getCategory(categoryId);
        product.setName(name);
        product.setCategory(category);
        product.setDescription(description);
        product.setPrice(price);
        customerService.addProduct(product);
        return "redirect:/productpage";
    }

    @PostMapping(value = "/editproduct")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editProduct(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "product_name") String name,
                              @RequestParam(name = "category") Long categoryId,
                              @RequestParam(name = "description") String description,
                              @RequestParam(name = "product_price") double price) {
        Product product = new Product();
        Category category = customerService.getCategory(categoryId);
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setDescription(description);
        product.setPrice(price);
        customerService.addProduct(product);
        return "redirect:/productpage";
    }

    @PostMapping(value = "/deleteproduct")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteProduct(@RequestParam(name = "id") Long id) {
        Product product = customerService.getProduct(id);
        customerService.deleteProduct(product);
        return "redirect:/productpage";
    }

    @PostMapping(value = "/addorder")
    @PreAuthorize("isAuthenticated()")
    public String addOrder(@RequestParam(name = "customer") Long customerId) {
        Customer customer = customerService.getCustomer(customerId);
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderTime(new Date());
        customerService.addOrder(order);
        return "redirect:/orderpage";
    }

    @PostMapping(value = "/editorder")
    @PreAuthorize("isAuthenticated()")
    public String editOrder(@RequestParam(name = "order_id") Long id,
                            @RequestParam(name = "product_id") Long productId,
                            @RequestParam(name = "count") int count) {
        Order order = customerService.getOrder(id);
        CountOfProduct countOfProduct = new CountOfProduct();
        Product product = customerService.getProduct(productId);
        countOfProduct.setProduct(product);
        countOfProduct.setCount(count);
        if (product != null && count>0) {
            List<CountOfProduct> products = order.getCountOfProducts();
            if (products == null) {
                products = new ArrayList<>();
            }
            for (CountOfProduct cop : products) {
                if (cop.getProduct().getId().equals(product.getId())) {
                    int productCount = cop.getCount();
                    productCount += count;
                    cop.setCount(productCount);
                    order.setCountOfProducts(products);
                    customerService.addOrder(order);
                    return "redirect:/addproductinorderpage/" + order.getId() +"?success";
                }
            }
            products.add(countOfProduct);
            order.setCountOfProducts(products);
            customerService.addCountOfProduct(countOfProduct);
            customerService.addOrder(order);
        }
        return "redirect:/addproductinorderpage/" + order.getId() + "?error";
    }

    @PostMapping(value = "/undoproduct")
    @PreAuthorize("isAuthenticated()")
    public String undoProduct(@RequestParam(name = "order_id") Long id,
                              @RequestParam(name = "product_id") Long productId) {
        Order order = customerService.getOrder(id);
        CountOfProduct product = customerService.getCountOfProduct(productId);
        if (product != null) {
            List<CountOfProduct> products = order.getCountOfProducts();
            if (products == null) {
                products = new ArrayList<>();
            }
            products.remove(product);
            order.setCountOfProducts(products);
            customerService.deleteCountOfProduct(product);
            customerService.addOrder(order);
        }
        return "redirect:/orderdetails/" + order.getId() + "&page=orders";
    }

    @PostMapping(value = "/setorderstatus")
    @PreAuthorize("isAuthenticated()")
    public String setOrderStatus(@RequestParam(name = "id") Long id,
                                 @RequestParam(name = "status") String status,
                                 @RequestParam(name = "page") String page) {
        Order order = customerService.getOrder(id);
        if (status != null) {
            if (status.equals("Done")) {
                order.setStatus(true);
            } else {
                order.setStatus(false);
            }
            customerService.addOrder(order);
        }
        if (page.equals("reports")) {
            return "redirect:/reports";
        } else {
            return "redirect:/orderpage";
        }
    }

    @PostMapping(value = "/deleteorder")
    @PreAuthorize("isAuthenticated()")
    public String deleteOrder(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "page") String page) {
        Order order = customerService.getOrder(id);
        customerService.deleteOrder(order);
        if (page.equals("reports")) {
            return "redirect:/reports";
        } else {
            return "redirect:/orderpage";
        }
    }

    @PostMapping(value = "/updateprofile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@RequestParam(name = "user_old_password") String oldPassword,
                                @RequestParam(name = "user_new_password") String newPassword,
                                @RequestParam(name = "user_conf_password") String confPassword) {
        AuthUser currentUser = getCurrentUser();
        if (newPassword.equals(confPassword)) {
            if (passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
                authUserService.saveUser(currentUser);
            }
        }
        return "redirect:/profile";
    }

    @PostMapping(value = "/signup")
    public String signUp(@RequestParam(name = "user_full_name") String fullName,
                         @RequestParam(name = "user_email") String email,
                         @RequestParam(name = "user_password") String password,
                         @RequestParam(name = "user_conf_password") String confPass) {
        AuthUser checkUser = userRepository.findByEmail(email);
        if (checkUser == null) {
            if (confPass.equals(password)) {
                AuthUser user = new AuthUser();
                user.setFullName(fullName);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(password));

                AuthUser newUser = authUserService.registerUser(user);
                if (newUser != null) {
                    return "redirect:/signuppage?regSuccess";
                }
            }
            return "redirect:/signuppage?regPassError";
        }
        return "redirect:/signuppage?regEmailError";
    }
}
