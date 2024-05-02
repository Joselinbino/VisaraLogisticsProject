package com.visara.transportationweb.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visara.transportationweb.Model.DeliveryLocationModel;

@Repository
public interface DeliveryLocationRepository extends JpaRepository<DeliveryLocationModel, Long> {

	DeliveryLocationModel findByDeliveryLocationName(String deliveryLocationName);

}
