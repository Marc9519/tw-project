package org.tw.data.service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tw.data.service.dao.BonusDao;
import org.tw.data.service.dao.MapComponentDao;
import org.tw.data.service.dao.PlayerDao;
import org.tw.data.service.dao.TribeDao;
import org.tw.data.service.dao.VillageDao;
import org.tw.data.service.db.DBTool;
import org.tw.data.service.model.Bonus;
import org.tw.data.service.model.MapComponent;
import org.tw.data.service.model.Player;
import org.tw.data.service.model.Tribe;
import org.tw.data.service.model.Village;

/**
 * The implementation of the TW-Service interface
 */
@Service
public class TwServiceImpl implements TwService {

	/*
	 * The dbTool
	 */
	@Autowired
	private DBTool dbTool;

	/*
	 * DAOs
	 */
	@Autowired
	private BonusDao bonusDao;

	@Autowired
	private MapComponentDao mapComponentDao;

	@Autowired
	private PlayerDao playerDao;

	@Autowired
	private TribeDao tribeDao;

	@Autowired
	private VillageDao villageDao;

	/*
	 * Public Service-API
	 */

	@Override
	public Object[][] findMapComponentsByRangeSql(String from, String to) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("mc.xPos, ");
		sql.append("mc.yPos, ");
		sql.append("ma.name, ");
		sql.append("ma.isObstacle ");
		sql.append("FROM ");
		sql.append("TW.MAPCOMPONENT mc ");
		sql.append("LEFT JOIN TW.MapAsset ma ON mc.mapAsset_id = ma.mapAsset_id ");
		sql.append("WHERE ");
		sql.append("mc.xPos BETWEEN " + from + " AND " + to + " ");
		sql.append("AND mc.yPos BETWEEN " + from + " AND " + to + " ");
		return dbTool.getResultMatrixFromSql(sql.toString());
	}

	@Override
	public List<MapComponent> findMapComponentsByRange(int fromX, int toX, int fromY, int toY) {
		return mapComponentDao.findMapComponentsByRange(fromX, toX, fromY, toY);
	}

	@Override
	public List<Village> findVillagesByRange(int fromX, int toX, int fromY, int toY) {
		return villageDao.findVillagesByRange(fromX,toX, fromY, toY);
	}

	@Override
	public List<MapComponent> findAllMapComponents() {
		return mapComponentDao.findMapComponentsByRange(500, 505, 500, 505);
	}

	@Override
	public List<Village> findAllVillages() {
		return villageDao.findAll();
	}

	@Override
	public List<Tribe> findAllTribes() {
		return tribeDao.findAll();
	}

	@Override
	public List<Player> findAllPlayers() {
		return playerDao.findAll();
	}

	@Override
	public List<Bonus> findAllBonus() {
		return bonusDao.findAll();
	}

}