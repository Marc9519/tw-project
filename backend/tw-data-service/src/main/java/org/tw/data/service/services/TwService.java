package org.tw.data.service.services;

import java.util.List;

import org.tw.data.service.model.Bonus;
import org.tw.data.service.model.MapComponent;
import org.tw.data.service.model.Player;
import org.tw.data.service.model.Tribe;
import org.tw.data.service.model.Village;

/**
 * The interface for the service
 */
public interface TwService {

	public List<MapComponent> findMapComponentsByRange(int fromX, int toX, int fromY, int toY);

	public List<Village> findAllVillages();

	public List<Tribe> findAllTribes();

	public List<Player> findAllPlayers();

	public List<Bonus> findAllBonus();

	public List<MapComponent> findAllMapComponents();

	public Object[][] findMapComponentsByRangeSql(String from, String to);

	public List<Village> findVillagesByRange(int fromX, int toX, int fromY, int toY);

}