package com.visara.transportationweb.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visara.transportationweb.Model.CfsModel;

public interface CfsRepository extends JpaRepository<CfsModel, String> {

	CfsModel findByCfsemail(String cfsemail);

	String getCfsIdByCfsemail(String cfsemail);

	 

}
