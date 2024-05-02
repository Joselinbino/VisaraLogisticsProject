package com.visara.transportationweb.Service;

import java.util.List;

import com.visara.transportationweb.Model.TransportsAndDriversDTO;

public interface TransportAndDriversService {

	List<TransportsAndDriversDTO> getTransportsByLocation(String fromLocationName);

}
