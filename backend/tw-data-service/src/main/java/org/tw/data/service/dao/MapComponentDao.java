package org.tw.data.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tw.data.service.model.MapComponent;
import org.tw.data.service.model.MapComponentId;

@Repository
public interface MapComponentDao extends JpaRepository<MapComponent, MapComponentId> {

	@Query("SELECT m FROM MapComponent m WHERE m.id.xPos BETWEEN ?1 AND ?2")
	public List<MapComponent> findMapComponentsByXRange(int fromX, int toX);

	@Query("SELECT m FROM MapComponent m WHERE m.id.xPos BETWEEN ?1 AND ?2 AND m.id.yPos BETWEEN ?3 AND ?4")
	public List<MapComponent> findMapComponentsByRange(int fromX, int toX, int fromY, int toY);

}