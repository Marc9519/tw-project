package org.tw.intface.bl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.tw.intface.util.UrlConnector;

@RunWith(MockitoJUnitRunner.class)
public class ExportMapDataBlTest {

	@Mock
	private UrlConnector urlConnector;

	@InjectMocks
	private ExportMapDataBl testInstance;

	/**
	 * Test case which uses mocks on urlConnector<br>
	 * Useful if there is no network environment available
	 */
	@Test
	public void testGenerateInsertSqlStatements() {
		// Given
		List<InputStream> ins = getConnectionResponses();
		String worldPrefix = "de197";
		when(urlConnector.getMapDataRequestInputStreams(worldPrefix)).thenReturn(ins);
		// When
		String result = testInstance.generateInsertSqlStatements(worldPrefix);
		// Then
		assertNotNull(result);
	}

	/**
	 * Test case which uses calls real urlConnector instead of mocking its<br>
	 * Requires suitable network environment. Currently used as import mechanism
	 * @throws IOException
	 */
	@Test
	public void testGenerateInsertSqlStatementsNoMocks() throws IOException {
		// Given
		String worldPrefix = "de197";
		when(urlConnector.getMapDataRequestInputStreams(worldPrefix)).thenCallRealMethod();
		when(urlConnector.getInputStreamByUrl(anyString())).thenCallRealMethod();
		// When
		String inserSql = testInstance.generateInsertSqlStatements(worldPrefix);
		// Then
		assertNotNull(inserSql);
		String fileName = "import-map.sql";
		exportToFile(inserSql, fileName);
	}

	private void exportToFile(String content, String fileName) throws IOException {
		Path path = Paths.get("./target/test-classes/" + fileName);
		Files.write(path, content.getBytes());
	}

	/*
	 * Data
	 */

	private List<InputStream> getConnectionResponses() {
		List<InputStream> result = new ArrayList<>();
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream in01 = classLoader.getResourceAsStream("responses/response01.json");
		InputStream in02 = classLoader.getResourceAsStream("responses/response02.json");
		InputStream in03 = classLoader.getResourceAsStream("responses/response03.json");
		InputStream in04 = classLoader.getResourceAsStream("responses/response04.json");
		InputStream in05 = classLoader.getResourceAsStream("responses/response05.json");
		InputStream in06 = classLoader.getResourceAsStream("responses/response06.json");
		InputStream in07 = classLoader.getResourceAsStream("responses/response07.json");
		InputStream in08 = classLoader.getResourceAsStream("responses/response08.json");
		InputStream in09 = classLoader.getResourceAsStream("responses/response09.json");
		InputStream in10 = classLoader.getResourceAsStream("responses/response10.json");
		InputStream in11 = classLoader.getResourceAsStream("responses/response11.json");
		InputStream in12 = classLoader.getResourceAsStream("responses/response12.json");
		InputStream in13 = classLoader.getResourceAsStream("responses/response13.json");
		InputStream in14 = classLoader.getResourceAsStream("responses/response14.json");
		InputStream in15 = classLoader.getResourceAsStream("responses/response15.json");
		InputStream in16 = classLoader.getResourceAsStream("responses/response16.json");
		InputStream in17 = classLoader.getResourceAsStream("responses/response17.json");
		InputStream in18 = classLoader.getResourceAsStream("responses/response18.json");
		InputStream in19 = classLoader.getResourceAsStream("responses/response19.json");
		InputStream in20 = classLoader.getResourceAsStream("responses/response20.json");
		InputStream in21 = classLoader.getResourceAsStream("responses/response21.json");
		InputStream in22 = classLoader.getResourceAsStream("responses/response22.json");
		InputStream in23 = classLoader.getResourceAsStream("responses/response23.json");
		InputStream in24 = classLoader.getResourceAsStream("responses/response24.json");
		InputStream in25 = classLoader.getResourceAsStream("responses/response25.json");
		InputStream in26 = classLoader.getResourceAsStream("responses/response26.json");
		InputStream in27 = classLoader.getResourceAsStream("responses/response27.json");
		InputStream in28 = classLoader.getResourceAsStream("responses/response28.json");
		InputStream in29 = classLoader.getResourceAsStream("responses/response29.json");
		InputStream in30 = classLoader.getResourceAsStream("responses/response30.json");
		InputStream in31 = classLoader.getResourceAsStream("responses/response31.json");
		InputStream in32 = classLoader.getResourceAsStream("responses/response32.json");
		InputStream in33 = classLoader.getResourceAsStream("responses/response33.json");
		InputStream in34 = classLoader.getResourceAsStream("responses/response34.json");
		InputStream in35 = classLoader.getResourceAsStream("responses/response35.json");
		InputStream in36 = classLoader.getResourceAsStream("responses/response36.json");
		InputStream in37 = classLoader.getResourceAsStream("responses/response37.json");
		InputStream in38 = classLoader.getResourceAsStream("responses/response38.json");
		InputStream in39 = classLoader.getResourceAsStream("responses/response39.json");
		InputStream in40 = classLoader.getResourceAsStream("responses/response40.json");
		InputStream in41 = classLoader.getResourceAsStream("responses/response41.json");
		InputStream in42 = classLoader.getResourceAsStream("responses/response42.json");
		InputStream in43 = classLoader.getResourceAsStream("responses/response43.json");
		InputStream in44 = classLoader.getResourceAsStream("responses/response44.json");
		InputStream in45 = classLoader.getResourceAsStream("responses/response45.json");
		InputStream in46 = classLoader.getResourceAsStream("responses/response46.json");
		InputStream in47 = classLoader.getResourceAsStream("responses/response47.json");
		InputStream in48 = classLoader.getResourceAsStream("responses/response48.json");
		InputStream in49 = classLoader.getResourceAsStream("responses/response49.json");
		InputStream in50 = classLoader.getResourceAsStream("responses/response50.json");
		result.add(in01);
		result.add(in02);
		result.add(in03);
		result.add(in04);
		result.add(in05);
		result.add(in06);
		result.add(in07);
		result.add(in08);
		result.add(in09);
		result.add(in10);
		result.add(in11);
		result.add(in12);
		result.add(in13);
		result.add(in14);
		result.add(in15);
		result.add(in16);
		result.add(in17);
		result.add(in18);
		result.add(in19);
		result.add(in20);
		result.add(in21);
		result.add(in22);
		result.add(in23);
		result.add(in24);
		result.add(in25);
		result.add(in26);
		result.add(in27);
		result.add(in28);
		result.add(in29);
		result.add(in30);
		result.add(in31);
		result.add(in32);
		result.add(in33);
		result.add(in34);
		result.add(in35);
		result.add(in36);
		result.add(in37);
		result.add(in38);
		result.add(in39);
		result.add(in40);
		result.add(in41);
		result.add(in42);
		result.add(in43);
		result.add(in44);
		result.add(in45);
		result.add(in46);
		result.add(in47);
		result.add(in48);
		result.add(in49);
		result.add(in50);
		return result;
	}

}