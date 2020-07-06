package com.thomas.oneToManyRelationshipMapKeyPrimitiveAndPartOfMapValueEagerWithBackwardNavigab.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.thomas.oneToManyRelationshipMapKeyPrimitiveAndPartOfMapValueEagerWithBackwardNavigab.domains.B;


public interface BRepository extends CrudRepository<B, Long> {
	List<B> findByB(String b);
}