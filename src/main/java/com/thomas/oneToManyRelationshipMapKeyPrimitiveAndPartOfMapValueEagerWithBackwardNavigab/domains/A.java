package com.thomas.oneToManyRelationshipMapKeyPrimitiveAndPartOfMapValueEagerWithBackwardNavigab.domains;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
//import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "A")
@NoArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of = {"id"})
public class A implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String myString;

    @OneToMany(mappedBy="a", fetch = FetchType.EAGER, 
    		cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name="b")
    public Map<String, B> bMap;

    public A(String myString) {
        this.myString = myString;
    }

    @Override
    public String toString() {
        String toReturn = "A{" +
                "id=" + id +
                ", myString='" + myString + "' Bs : ";
        for(B b : bMap.values()) {
        	toReturn += b.getB()+" ";
        }
        toReturn += '}';
        return toReturn;
    }
}