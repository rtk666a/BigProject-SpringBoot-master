package com.springBoot.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<CountOfProduct> countOfProducts;
    @Column(name = "order_time")
    private Date orderTime;
    private boolean status;

    public String getStatus() {
        if (status) {
            return "Done";
        } else {
            return "In progress";
        }
    }

    public double getOverAllPrice() {
        double overall = 0;
        if (countOfProducts != null) {
            for (CountOfProduct p : countOfProducts) {
                overall += p.getProduct().getPrice() * p.getCount();
            }
        }
        return overall;
    }
}
