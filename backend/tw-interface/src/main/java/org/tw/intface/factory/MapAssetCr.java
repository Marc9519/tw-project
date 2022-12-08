package org.tw.intface.factory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tw.intface.model.MapAsset;

/**
 * Factory class for Map-assets
 */
public class MapAssetCr {

	/*
	 * The asset name map
	 */
	private Map<Long, String> assetNameMap;


	public MapAssetCr() {
		this.assetNameMap = getAssetMap();
	}

	/**
	 * Gets all available MapAssets
	 * @return The map assets
	 */
	public List<MapAsset> getAllMapAssets(){
		List<MapAsset> result = new ArrayList<>();
		for (Entry<Long, String> e : assetNameMap.entrySet()) {
			Long id = e.getKey();
			MapAsset currentAsset = generateMapAsssetById(id);
			result.add(currentAsset);
		}
		return result;
	}

	private MapAsset generateMapAsssetById(Long id) {
		MapAsset asset = new MapAsset();
		asset.setId(id);
		asset.setName(assetNameMap.get(id));
		asset.setIsObstacle(id > 3);
		return asset;
	}

	/*
	 * Private methods
	 */

	private Map<Long, String> getAssetMap() {
		Map<Long, String> result = new LinkedHashMap<>();
		result.put(0L, "gras1");
		result.put(1L, "gras2");
		result.put(2L, "gras3");
		result.put(3L, "gras4");
		result.put(4L, "v1_left");
		result.put(5L, "v1");
		result.put(6L, "v2_left");
		result.put(7L, "v2");
		result.put(8L, "v3_left");
		result.put(9L, "v3");
		result.put(10L, "v4_left");
		result.put(11L, "v4");
		result.put(12L, "v5_left");
		result.put(13L, "v5");
		result.put(14L, "v6_left");
		result.put(15L, "v6");
		result.put(16L, "b1_left");
		result.put(17L, "b1");
		result.put(18L, "b2_left");
		result.put(19L, "b2");
		result.put(20L, "b3_left");
		result.put(21L, "b3");
		result.put(22L, "b4_left");
		result.put(23L, "b4");
		result.put(24L, "b5_left");
		result.put(25L, "b5");
		result.put(26L, "b6_left");
		result.put(27L, "b6");
		result.put(28L, "berg1");
		result.put(29L, "berg2");
		result.put(30L, "berg3");
		result.put(31L, "berg4");
		result.put(32L, "forest0000");
		result.put(33L, "forest0001");
		result.put(34L, "forest0010");
		result.put(35L, "forest0011");
		result.put(36L, "forest0100");
		result.put(37L, "forest0101");
		result.put(38L, "forest0110");
		result.put(39L, "forest0111");
		result.put(40L, "forest1000");
		result.put(41L, "forest1001");
		result.put(42L, "forest1010");
		result.put(43L, "forest1011");
		result.put(44L, "forest1100");
		result.put(45L, "forest1101");
		result.put(46L, "forest1110");
		result.put(47L, "forest1111");
		result.put(48L, "see");
		result.put(49L, "event_xmas");
		result.put(50L, "event_easter");
		result.put(51L, "ghost");
		result.put(52L, "event_merchant");
		result.put(53L, "event_wizard");
		result.put(54L, "event_easter2014");
		result.put(55L, "event_fall2014");
		result.put(56L, "rune_village");
		result.put(57L, "citynw");
		result.put(58L, "cityne");
		result.put(59L, "citysw");
		result.put(60L, "cityse");
		result.put(61L, "cityn1");
		result.put(62L, "cityn2");
		result.put(63L, "citye1");
		result.put(64L, "citye2");
		result.put(65L, "citys1");
		result.put(66L, "citys2");
		result.put(67L, "cityw1");
		result.put(68L, "cityw2");
		result.put(69L, "citym1");
		result.put(70L, "citym2");
		result.put(71L, "citym3");
		result.put(72L, "citym4");
		result.put(73L, "citye3");
		result.put(74L, "citye4");
		result.put(75L, "cityw3");
		result.put(76L, "cityw4");
		result.put(77L, "citym5");
		result.put(78L, "citym6");
		result.put(79L, "citym7");
		result.put(80L, "citym8");
		result.put(81L, "citym9");
		result.put(82L, "citym10");
		result.put(83L, "stronghold0");
		result.put(84L, "stronghold1");
		result.put(85L, "stronghold2");
		result.put(86L, "stronghold3");
		result.put(87L, "stronghold4");
		result.put(88L, "university");
		return result;
	}

}