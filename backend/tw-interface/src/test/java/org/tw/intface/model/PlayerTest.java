package org.tw.intface.model;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class PlayerTest {

	@Test
	public void testHashCode() throws Exception {
		//Given
		Map<Player, String> playerMap = new HashMap<>();
		Player p1 = new Player();
		p1.setPlayerNr("123456");
		Player p2 = new Player();
		p2.setPlayerNr("654321");
		Player p3 = new Player();
		p3.setPlayerNr("123456");
		//When
		playerMap.put(p1, p1.getPlayerNr());
		playerMap.put(p2, p2.getPlayerNr());
		playerMap.put(p3, p3.getPlayerNr());
		//Then
		assertEquals(2, playerMap.size());
	}

}
