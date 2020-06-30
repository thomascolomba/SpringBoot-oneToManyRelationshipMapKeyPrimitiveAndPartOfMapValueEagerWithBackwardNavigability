package com.thomas.oneToManyRelationshipMapKeyPrimitiveAndPartOfMapValueEagerWithBackwardNavigab.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.thomas.oneToManyRelationshipMapKeyPrimitiveAndPartOfMapValueEagerWithBackwardNavigab.domains.A;

public interface ARepository extends CrudRepository<A, Long> {
	public List<A> findByMyString(String myString);
}