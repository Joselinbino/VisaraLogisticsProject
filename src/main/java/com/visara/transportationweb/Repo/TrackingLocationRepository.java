package com.visara.transportationweb.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visara.transportationweb.Model.TrackingLocationsModel;

@Repository
public interface TrackingLocationRepository extends JpaRepository<TrackingLocationsModel, Long> {

	TrackingLocationsModel findByCheckpointName(String checkpointName);

}
