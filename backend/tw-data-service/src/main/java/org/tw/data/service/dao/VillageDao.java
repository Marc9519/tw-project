package org.tw.data.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tw.data.service.model.Village;

@Repository
public interface VillageDao extends JpaRepository<Village, Long>{

	@Query("SELECT v FROM Village v WHERE v.xPos BETWEEN ?1 AND ?2 AND v.yPos BETWEEN ?3 AND ?4")
	public List<Village> findVillagesByRange(int fromX, int toX, int fromY, int toY);

}