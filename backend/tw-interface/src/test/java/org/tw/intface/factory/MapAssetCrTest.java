package org.tw.intface.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.tw.intface.model.MapAsset;

public class MapAssetCrTest {

	@Test
	public void testGetMapAsssetById()  {
		//Given
		MapAssetCr testInstance = new MapAssetCr();
		// When
		List<MapAsset> result = testInstance.getAllMapAssets();
		//Then
		assertNotNull(result);
		assertEquals(89, result.size());
		for (MapAsset mapAsset : result) {
			assertNotNull(mapAsset.getId());
			assertNotNull(mapAsset.getName());
		}
	}

}
