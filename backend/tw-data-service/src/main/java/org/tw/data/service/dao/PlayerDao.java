package org.tw.data.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tw.data.service.model.Player;

@Repository
public interface PlayerDao extends JpaRepository<Player, Long>{
	//TBD
}