package com.visara.transportationweb.Service;

import java.util.List;

import com.visara.transportationweb.Model.OrderBooking;
import com.visara.transportationweb.Model.TransportDTO;
import com.visara.transportationweb.Model.TransportsModel;

import javassist.NotFoundException;

public interface TransportService {

	TransportsModel findByEmail(String email);

	List<TransportsModel> getAllTransports();

	List<TransportDTO> getTransportsForCFS(String cfsemail);

	List<TransportsModel> getTransportsByCfsEmail(String cfsemail) throws NotFoundException;

	TransportsModel findTransportByTransportId(String transportId);

	List<OrderBooking> getOrdersByEmail(String email); //Author SURYA

	String getTransportNameByEmail(String transportEmail);

}
