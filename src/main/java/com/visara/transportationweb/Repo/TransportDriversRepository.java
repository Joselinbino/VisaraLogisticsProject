package com.visara.transportationweb.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visara.transportationweb.Model.TransportDriverModel;

@Repository
public interface TransportDriversRepository extends JpaRepository<TransportDriverModel, Long> {

	List<TransportDriverModel> findByDriverDriverIdIn(List<String> driverIds);

}
