package com.sist.web.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sist.web.entity.categoryEntity;


import java.util.*;

@Repository
public interface CategoryDAO extends JpaRepository<categoryEntity, Integer>  {
	public List<categoryEntity> findAll();
}
