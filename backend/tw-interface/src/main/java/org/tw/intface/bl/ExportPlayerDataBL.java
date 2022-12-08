
package org.tw.intface.bl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tw.intface.model.Player;
import org.tw.intface.model.Tribe;
import org.tw.intface.model.Village;
import org.tw.intface.util.UrlConnector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * BL for exporting tribe, player and village data.
 * Prepares a mapping of each Village to its points, which can be used by the ExportVillageBuildingDataBL
 */
public class ExportPlayerDataBL {

	/*
	 * Constants
	 */
	private static final String LINE_SEPARATOR = System.lineSeparator();
	private static final String NULL = "NULL";

	/*
	 * The logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExportPlayerDataBL.class);

	/*
	 * The urlConnector
	 */
	private UrlConnector urlConnector;


	private Set<Player> playerSet;
	private Set<Tribe> tribeSet;
	private Set<Village> villageSet;

	/*
	 * The maps
	 */

	private Map<String, Long> tribeTranslationMap;
	private Map<String, Long> playerTranslationMap;
	private Map<Village, Integer> villagePointsMap;

	/*
	 * The SQL Statements
	 */
	private Object bonusInsertSql;
	private String tribeInsertSql;
	private String playerInsertSql;
	private String villageInsertSql;
	private String insertScript;

	/**
	 * Instantiates the BL
	 *
	 * @param urlConnector
	 * @param worldPrefix
	 */
	public ExportPlayerDataBL(UrlConnector urlConnector, String worldPrefix) {
		this.urlConnector = urlConnector;
		this.playerSet = new HashSet<>();
		this.tribeSet = new HashSet<>();
		this.villageSet = new HashSet<>();
		this.villagePointsMap = new HashMap<>();
		this.tribeTranslationMap = new HashMap<>();
		this.playerTranslationMap = new HashMap<>();
		executeBl(worldPrefix);
	}

	/**
	 * Executes the BL
	 * @param worldPrefix the world prefix to search for
	 * @return the insert script
	 */
	private void executeBl(String worldPrefix) {
		fetchAndProcessTWData(worldPrefix);
		this.bonusInsertSql = generateBonusInsertSql();
		this.tribeInsertSql = generateTribeInsertSql();
		this.playerInsertSql = generatePlayerInsertSql();
		this.villageInsertSql = generateVillageInsertSql();
		this.insertScript = buildInsertScript();
	}

	private String buildInsertScript() {
		StringBuilder script = new StringBuilder();
		script.append("-- GENERAL.BONUS" + LINE_SEPARATOR);
		script.append(bonusInsertSql + LINE_SEPARATOR);
		script.append("-- TW.TRIBE" + LINE_SEPARATOR);
		script.append(tribeInsertSql + LINE_SEPARATOR);
		script.append("-- TW.PLAYER" + LINE_SEPARATOR);
		script.append(playerInsertSql +LINE_SEPARATOR);
		script.append("-- TW.VILLAGE" + LINE_SEPARATOR);
		script.append(villageInsertSql);
		return script.toString();
	}


	private String generateBonusInsertSql() {
		StringBuilder sql = new StringBuilder();
		String[] ids = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		List<String> descriptions = new ArrayList<>();
		descriptions.add("100% mehr Holzproduktion");
		descriptions.add("100% mehr Lehmproduktion");
		descriptions.add("100% mehr Eisenproduktion");
		descriptions.add("10% mehr Bevölkerung");
		descriptions.add("33% schnellere Rekrutierung in der Kaserne");
		descriptions.add("33% schnellere Rekrutierung im Stall");
		descriptions.add("50% schnellere Rekrutierung in der Werkstatt");
		descriptions.add("30% mehr Rohstoffproduktion (alle Rohstoffe)");
		descriptions.add("100% mehr Holzproduktion");
		descriptions.add("50% mehr Speicherkapazität und Händler");
		List<String> assetNames = new ArrayList<>();
		assetNames.add("bonus/wood.png");
		assetNames.add("bonus/stone.png");
		assetNames.add("bonus/iron.png");
		assetNames.add("bonus/farm.png");
		assetNames.add("bonus/barracks.png");
		assetNames.add("bonus/stable.png");
		assetNames.add("bonus/garage.png");
		assetNames.add("bonus/all.png");
		assetNames.add("bonus/storage.png");
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			String description = descriptions.get(i);
			String assetName = assetNames.get(i);
			int start = assetName.indexOf('/')+1;
			int end = assetName.indexOf('.');
			String bonusKey = assetName.substring(start, end).toUpperCase();
			sql.append("INSERT INTO GENERAL.BONUS (BONUS_ID, BONUS_KEY, DESCRIPTION, ASSET_NAME) VALUES(");
			sql.append(id+", ");
			sql.append(convertStringToSqlInsertableValue(bonusKey)+", ");
			sql.append(convertStringToSqlInsertableValue(description)+", ");
			sql.append(convertStringToSqlInsertableValue(assetName)+");");
			sql.append(LINE_SEPARATOR);
		}
		return sql.toString();
	}

	private String generateTribeInsertSql() {
		StringBuilder sql = new StringBuilder();
		Long id = 0L;
		for (Tribe tribe : tribeSet) {
			sql.append("INSERT INTO TW.TRIBE (TRIBE_ID, TRIBE_NR, NAME, TAG, DESCRIPTION) VALUES(");
			sql.append(id+", ");
			sql.append(tribe.getTribeNr()+", ");
			sql.append(convertStringToSqlInsertableValue(tribe.getName())+", ");
			sql.append(convertStringToSqlInsertableValue(tribe.getTag())+", ");
			sql.append(NULL + ");");
			sql.append(LINE_SEPARATOR);
			tribeTranslationMap.put(tribe.getTribeNr(), id);
			++id;
		}
		return sql.toString();
	}

	private String generatePlayerInsertSql() {
		StringBuilder sql = new StringBuilder();
		Long id = 0L;
		for (Player player : playerSet) {
			sql.append("INSERT INTO TW.PLAYER (PLAYER_ID, PLAYER_NR, EMAIL, PASS, NAME, PROFILE_TEXT, TRIBE_ID) VALUES(");
			sql.append(id+", ");
			sql.append(player.getPlayerNr()+", ");
			sql.append(NULL+", ");
			sql.append(NULL+", ");
			sql.append(convertStringToSqlInsertableValue(player.getName())+", ");
			sql.append(NULL+", ");
			Long playerTribe = player.getTribeId();
			boolean hasTribe = playerTribe != null;
			String insertTribeVal = hasTribe ? tribeTranslationMap.get(playerTribe.toString()).toString() : NULL;
			sql.append(insertTribeVal + ");");
			playerTranslationMap.put(player.getPlayerNr(), id);
			sql.append(LINE_SEPARATOR);
			++id;
		}
		return sql.toString();
	}

	private String generateVillageInsertSql() {
		StringBuilder sql = new StringBuilder();
		Long id = 0L;
		String insertHeader = "INSERT INTO TW.VILLAGE (VILLAGE_ID, VILLAGE_NR, NAME, XPOS, YPOS, POINTS, CONTINENT, CURRENT_WOOD, CURRENT_CLAY, CURRENT_IRON, PLAYER_ID, BONUS_ID) VALUES";
		sql.append(insertHeader);
		sql.append(LINE_SEPARATOR);
		for (Village village : villageSet) {
			Long bonusId = village.getBonusId();
			boolean hasBonus = bonusId != null;
			if (hasBonus && bonusId > 9) {
				// TBD Resolve special cases (e.g. University, ...)
				continue;
			}
			sql.append("(");
			sql.append(id + ", ");
			sql.append(convertStringToSqlInsertableValue(village.getVillageNr()) + ", ");
			sql.append(convertStringToSqlInsertableValue(village.getName()) + ", ");
			sql.append(village.getxPos() + ", ");
			sql.append(village.getyPos() + ", ");
			sql.append(village.getPoints() + ", ");
			sql.append(village.getContinent() + ", ");
			sql.append("150, ");
			sql.append("130, ");
			sql.append("125, ");
			Long villagePlayerId = village.getPlayerId();
			boolean hasPlayer = villagePlayerId != null;
			String insertPlayerVal = hasPlayer ? playerTranslationMap.get(villagePlayerId.toString()).toString() : NULL;
			sql.append(insertPlayerVal + ", ");
			String bonusVal = hasBonus ? bonusId.toString() : NULL;
			sql.append(bonusVal + ")");
			if (id % 1000 == 0) {
				sql.append(";");
				sql.append(LINE_SEPARATOR);
				sql.append(insertHeader);
			}else {
				sql.append(",");
			}
			sql.append(LINE_SEPARATOR);
			village.setId(id);
			++id;
		}
		String substring = sql.substring(0, sql.length()-3);
		return substring.concat(";");
	}

	/*
	 * Private methods
	 */

	private void fetchAndProcessTWData(String worldPrefix) {
		List<InputStream> ins = urlConnector.getMapDataRequestInputStreams(worldPrefix);
		ObjectMapper mapper = new ObjectMapper();

		for (InputStream inputStream : ins) {
			try {
				JsonNode currentNode = mapper.readTree(inputStream);
				resolveJSonNode(currentNode);
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}
	}

	private void resolveJSonNode(JsonNode node) {
		for (int i = 0; i < node.size(); i++) {
			JsonNode currentNode = node.get(i);
			JsonNode dataNode = currentNode.get("data");
			JsonNode playerNode = dataNode.get("players");
			resolvePlayerNode(playerNode);
			JsonNode allyNode = dataNode.get("allies");
			resolveTribeNode(allyNode);
			JsonNode villageNode = dataNode.get("villages");
			int xOffSet = currentNode.get("x").intValue();
			int yOffSet = currentNode.get("y").intValue();
			resolveVillageNode(villageNode, xOffSet, yOffSet);
		}
	}

	private void resolveVillageNode(JsonNode villageNode, int xOffSet, int yOffSet) {
		if (villageNode instanceof ArrayNode) {
			// ArrayNode
			for (int xVal = 0; xVal < villageNode.size(); xVal++) {
				JsonNode innerNode = villageNode.get(xVal);
				processInnerVillageNode(xOffSet + xVal, yOffSet, innerNode);
			}
		} else {
			// ObjectNode
			Iterator<Entry<String, JsonNode>> outerFields = villageNode.fields();
			while (outerFields.hasNext()) {
				Entry<String, JsonNode> entry = outerFields.next();
				int xVal = Integer.parseInt(entry.getKey());
				JsonNode innerNode = entry.getValue();
				processInnerVillageNode(xOffSet + xVal, yOffSet, innerNode);
			}
		}
	}

	private void processInnerVillageNode(int xPos, int yOffSet, JsonNode innerNode) {
		if (innerNode instanceof ArrayNode) {
			// ArrayNode
			for (int yVal = 0; yVal < innerNode.size(); yVal++) {
				JsonNode dataNode = innerNode.get(yVal);
				processVillageDataNode(xPos, yOffSet +yVal, dataNode);
			}
		} else {
			// ObjectNode
			Iterator<Entry<String, JsonNode>> innerFields = innerNode.fields();
			while (innerFields.hasNext()) {
				Entry<String, JsonNode> next = innerFields.next();
				int yVal = Integer.parseInt(next.getKey());
				JsonNode dataNode = next.getValue();
				processVillageDataNode(xPos, yOffSet +yVal, dataNode);
			}
		}

	}

	private void processVillageDataNode(int xPos, int yPos, JsonNode dataNode) {
		JsonNode idNode = dataNode.get(0);
		JsonNode nameNode = dataNode.get(2);
		JsonNode pointsNode = dataNode.get(3);
		JsonNode playerIdNode = dataNode.get(4);
		JsonNode bonusIdNode = dataNode.get(8);
		String villageNr = idNode.asText();
		String name = nameNode instanceof TextNode ? nameNode.asText() : "Barbarendorf";
		Long playerId = "0".equals(playerIdNode.asText()) ? null : Long.parseLong(playerIdNode.asText());
		Long bonusId = bonusIdNode instanceof IntNode ? bonusIdNode.asLong() : null;
		Village currentVillage = new Village();
		currentVillage.setVillageNr(villageNr);
		currentVillage.setxPos(xPos);
		currentVillage.setyPos(yPos);
		currentVillage.setName(name);
		currentVillage.setPlayerId(playerId);
		currentVillage.setBonusId(bonusId);
		villageSet.add(currentVillage);
		String pointsStr = StringUtils.remove(pointsNode.asText(), '.');
		int points = Integer.parseInt(pointsStr);
		currentVillage.setPoints(points);
		currentVillage.setContinent(calculateContinent(xPos, yPos));
		villagePointsMap.put(currentVillage, points);
	}

	private void resolveTribeNode(JsonNode allyNode) {
		// AllyNode --> ObjectNode
		Iterator<Entry<String, JsonNode>> allyFields = allyNode.fields();
		while (allyFields.hasNext()) {
			Entry<String, JsonNode> entry = allyFields.next();
			String tribeNr = entry.getKey();
			JsonNode innerNode = entry.getValue();
			JsonNode nameNode = innerNode.get(0);
			JsonNode tagNode= innerNode.get(2);
			Tribe currentTribe = new Tribe();
			currentTribe.setTribeNr(tribeNr);
			currentTribe.setName(nameNode.asText());
			currentTribe.setTag(tagNode.asText());
			tribeSet.add(currentTribe);
		}
	}

	private void resolvePlayerNode(JsonNode playerNode) {
		// PlayerNode --> ObjectNode
		Iterator<Entry<String, JsonNode>> playerFields = playerNode.fields();
		while (playerFields.hasNext()) {
			Entry<String, JsonNode> entry =playerFields.next();
			String playerNr = entry.getKey();
			// ArrayNode
			JsonNode innerNode = entry.getValue();
			JsonNode nameNode = innerNode.get(0);
			JsonNode tribeNode = innerNode.get(2);
			String tribeText = tribeNode.asText();
			Long tribeId = Long.parseLong(tribeText);
			Player currentPlayer = new Player();
			currentPlayer.setPlayerNr(playerNr);
			currentPlayer.setName(nameNode.asText());
			currentPlayer.setTribeId(tribeId.equals(0L) ? null : tribeId);
			playerSet.add(currentPlayer);
		}
	}

	private String convertStringToSqlInsertableValue(String value) {
		if (value == null) {
			return "null";
		}
		return "'" + value.replace("'", "''") + "'";
	}

	private Integer calculateContinent(int xPos, int yPos) {
		int yOffset = yPos / 100;
		int xOffset = xPos / 100;
		return yOffset * 10 + xOffset;
	}

	/*
	 * Getters and Setters
	 */

	public Map<Village, Integer> getVillagePointsMap() {
		return villagePointsMap;
	}

	public Set<Village> getVillageSet() {
		return villageSet;
	}

	public Object getBonusInsertSql() {
		return bonusInsertSql;
	}

	public String getTribeInsertSql() {
		return tribeInsertSql;
	}

	public String getInsertScript() {
		return insertScript;
	}

	public String getPlayerInsertSql() {
		return playerInsertSql;
	}

	public String getVillageInsertSql() {
		return villageInsertSql;
	}

}