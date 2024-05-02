package com.visara.transportationweb.Service;

import java.util.List;

import com.visara.transportationweb.Model.CfsModel;
import com.visara.transportationweb.Model.OrderBooking;
import com.visara.transportationweb.Model.TransportsModel;

public interface CfsService {

	String getCfsIdByEmail(String cfsemail);

	List<TransportsModel> getAssociatedTransports(String cfsemail);

	List<TransportsModel> getUnassociatedTransports(String cfsemail);

	List<CfsModel> findAll();
    
	List<OrderBooking> getOrdersByEmail(String email);

	String getCfsNameByEmail(String cfsEmail);
}
