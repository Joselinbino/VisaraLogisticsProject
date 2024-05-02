package com.visara.transportationweb.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visara.transportationweb.Model.FromLocationModel;

@Repository
public interface FromLocationRepository extends JpaRepository<FromLocationModel, Long> {

	Object findByLocationName(String string);

	FromLocationModel findBylocationName(String departureLocationName);
	
	 

}
