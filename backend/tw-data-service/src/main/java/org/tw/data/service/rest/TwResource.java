package org.tw.data.service.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tw.data.service.model.MapComponent;
import org.tw.data.service.model.Village;
import org.tw.data.service.services.TwService;

/**
 * The REST resource of the service
 */
@RestController
@RequestMapping("/twinfo")
public class TwResource {

	/**
	 * The TW service
	 */
	@Autowired
	private TwService twService;


	/**
	 * @param fromX
	 * @param toX
	 * @param fromY
	 * @param toY
	 * @return The components within the range
	 */
	@GetMapping("map/tiles/search")
	public List<MapComponent> findMapComponentsInRange(@RequestParam int fromX,@RequestParam int toX,@RequestParam int fromY,@RequestParam int toY) {
		return twService.findMapComponentsByRange(fromX, toX, fromY, toY);
	}

	/**
	 * @param fromX
	 * @param toX
	 * @param fromY
	 * @param toY
	 * @return The villages within the range
	 */
	@GetMapping("map/villages/search")
	public List<Village> findVillagesInRange(@RequestParam int fromX,@RequestParam int toX,@RequestParam int fromY,@RequestParam int toY) {
		return twService.findVillagesByRange(fromX, toX, fromY, toY);
	}


	/**
	 *
	 * @param from
	 * @param to
	 * @return
	 */
	@GetMapping("map2")
	public Object[][] findMapComponentsByRangeSql(@RequestParam String from, @RequestParam String to) {
		return twService.findMapComponentsByRangeSql(from, to);
	}

	@GetMapping
	public List<MapComponent> bl() {
		return twService.findAllMapComponents();
	}

}