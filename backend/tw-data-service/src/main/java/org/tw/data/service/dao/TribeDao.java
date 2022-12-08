package org.tw.data.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tw.data.service.model.Tribe;

@Repository
public interface TribeDao extends JpaRepository<Tribe, Long>{
	//TBD
}