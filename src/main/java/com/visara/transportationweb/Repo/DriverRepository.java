package com.visara.transportationweb.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visara.transportationweb.Model.DriverModel;

@Repository
public interface DriverRepository extends JpaRepository<DriverModel, String> {

	DriverModel findBydriverMobile(String mobileNumber);

	List<DriverModel> findBydriverStatus(String string);

	DriverModel findByDriverId(DriverModel driver);

}
