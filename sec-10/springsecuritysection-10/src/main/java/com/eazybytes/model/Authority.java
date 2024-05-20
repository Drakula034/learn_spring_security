package com.eazybytes.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "authorities")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


}