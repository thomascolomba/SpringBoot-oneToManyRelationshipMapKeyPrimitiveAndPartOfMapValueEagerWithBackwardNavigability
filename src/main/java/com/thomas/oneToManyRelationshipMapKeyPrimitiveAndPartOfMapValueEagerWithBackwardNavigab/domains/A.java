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
import java.util.Set;

@Entity
@Table(name = "A")
@NoArgsConstructor
@Setter @Getter
//@ToString
@EqualsAndHashCode(of = {"id"})
public class A implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String myString;

//    @OneToMany(mappedBy = "a", fetch = FetchType.EAGER,
//            cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<B> bSet;
    @OneToMany(mappedBy="a", fetch = FetchType.EAGER, 
    		cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name="myInt")
    public Map<Integer, B> bMap;

    public A(String myString) {
        this.myString = myString;
    }

    @Override
    public String toString() {
        String toReturn = "A{" +
                "id=" + id +
                ", myString='" + myString + "' Bs : ";
        for(B b : bMap.values()) {
        	toReturn += b.getMyInt()+" ";
        }
        toReturn += '}';
        return toReturn;
    }
}