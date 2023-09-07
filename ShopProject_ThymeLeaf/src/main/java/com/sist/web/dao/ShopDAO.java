package com.sist.web.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.sist.web.entity.*;

@Repository
public interface ShopDAO extends JpaRepository<ShopEntity, Integer> {
	@Query(value = "SELECT * "
				 + "FROM goods "
				 + "ORDER BY hit DESC "
				 + "LIMIT 0,3", nativeQuery = true)
	public List<ShopEntity> shopTop3Data();
	
	@Query(value = "SELECT * "
				 + "FROM goods "
				 + "ORDER BY rand() "
				 + "LIMIT 3", nativeQuery = true)
	public List<ShopEntity> shopRandData();
	
	@Query(value = "SELECT * "
				 + "FROM goods "
				 + "WHERE goods_name LIKE CONCAT('%',:fd,'%') "
				 + "AND cno=:cno "
				 + "ORDER BY gno ASC "
				 + "LIMIT :start,9", nativeQuery = true)
	public List<ShopEntity> shopListData(@Param("fd") String fd, @Param("cno") Integer cno, @Param("start") Integer start);
	
	@Query(value = "SELECT CEIL(COUNT(*)/9.0) "
				 + "FROM goods "
				 + "WHERE goods_name LIKE CONCAT('%', :fd, '%') "
				 + "AND cno=:cno", nativeQuery = true)
	public int shopFindTotalPage(@Param("fd") String fd, @Param("cno") Integer cno); 
	
	public ShopEntity findByGno(int gno);
	
	
}
  