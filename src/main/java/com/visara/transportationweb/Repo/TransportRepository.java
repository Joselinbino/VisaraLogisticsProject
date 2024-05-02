package com.visara.transportationweb.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visara.transportationweb.Model.TransportsModel;

@Repository
public interface TransportRepository extends JpaRepository<TransportsModel, String> {

	TransportsModel findByTransportEmail(String email);

	TransportsModel findByTransportId(String transportId);

	 

}
