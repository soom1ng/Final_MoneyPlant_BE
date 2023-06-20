package com.MoneyPlant.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="budget")
@Setter
@Getter
@ToString
public class Budget {
    @Id
    @Column(name = "budget_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long budgetId; // 예산 Id

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; //카테고리 Id

    @Column(name = "budget_money")
    private int budgetMoney; //예산 돈

}
