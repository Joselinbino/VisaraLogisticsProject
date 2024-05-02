package com.visara.transportationweb.Repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visara.transportationweb.Model.OrderBooking;

@Repository
public interface OrderBookingRepository extends JpaRepository<OrderBooking, String> {

	// Assuming 'orderDate' is your datetime field
    @Query("SELECT o FROM OrderBooking o WHERE o.driver.driverId = :driverId ORDER BY o.orderDate DESC")
    List<OrderBooking> findLatestOrderByDriverDriverId(@Param("driverId") String driverId, Pageable pageable);

	List<OrderBooking> findByDriverDriverIdAndStatus(String driverId, String string);

	List<OrderBooking> findByCfsCfsId(String cfsId);

	OrderBooking findByOrderId(String orderId);
	
	List<OrderBooking> findByTransportTransportId(String transportId); // Author SURYA

	 
	 

}
