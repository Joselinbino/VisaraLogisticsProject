package com.visara.transportationweb.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visara.transportationweb.Model.VehiclesModel;

@Repository
public interface VehicleRepository extends JpaRepository<VehiclesModel, Integer> {

	boolean existsByLicensePlateNumber(String licensePlateNumber);

	List<VehiclesModel> findByTransportTransportId(String transportId);

	 List<VehiclesModel> findByDriverDriverId(String driverId);

	 VehiclesModel findFirstByDriverDriverId(String driverId);


	 
}
