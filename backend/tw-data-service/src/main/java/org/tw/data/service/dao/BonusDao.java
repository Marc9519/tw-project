package org.tw.data.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tw.data.service.model.Bonus;

@Repository
public interface BonusDao extends JpaRepository<Bonus, Long>{
	//TBD
}