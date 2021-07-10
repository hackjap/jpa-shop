package jpabook.jpashop.domain;


import lombok.Getter;

import javax.persistence.Embeddable;

// = 어딘가의 내장이 될 수 있음.
@Embeddable
@Getter
public class Address {


    private String city;
    private String street;
    private String zipcode;
}
