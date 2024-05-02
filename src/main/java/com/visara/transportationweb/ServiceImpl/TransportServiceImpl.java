package com.visara.transportationweb.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visara.transportationweb.Model.CfsModel;
import com.visara.transportationweb.Model.OrderBooking;
import com.visara.transportationweb.Model.TransportCfs;
import com.visara.transportationweb.Model.TransportDTO;
import com.visara.transportationweb.Model.TransportsModel;
import com.visara.transportationweb.Repo.CfsRepository;
import com.visara.transportationweb.Repo.OrderBookingRepository;
import com.visara.transportationweb.Repo.TransportCfsRepository;
import com.visara.transportationweb.Repo.TransportRepository;
import com.visara.transportationweb.Service.TransportService;

import javassist.NotFoundException;

@Service
public class TransportServiceImpl implements TransportService {

	@Autowired
	private TransportRepository transportRepo;

	@Autowired
	private CfsRepository cfsRepo;

	@Autowired
	private TransportCfsRepository transportCfsRepo;

	@Autowired
	private OrderBookingRepository orderBookingRepository;

	@Override
	public TransportsModel findByEmail(String email) {

		return transportRepo.findByTransportEmail(email);
	}

	@Override
	public List<TransportsModel> getAllTransports() {
		return transportRepo.findAll();
	}

	@Override
	public List<TransportDTO> getTransportsForCFS(String cfsemail) {
		// Find the CFS based on the provided email
		CfsModel cfs = cfsRepo.findByCfsemail(cfsemail);
		if (cfs == null) {
			throw new IllegalArgumentException("CFS with email " + cfsemail + " not found");
		}

		// Retrieve associated TransportCfs entities for the given CFS
		List<TransportCfs> associatedTransportCfsList = transportCfsRepo.findByCfs_CfsId(cfs.getCfsId());

		// Extract TransportModel objects from associated TransportCfs entities
		Set<String> associatedTransportIds = associatedTransportCfsList.stream()
				.map(transportCfs -> transportCfs.getTransport().getTransportId()).collect(Collectors.toSet());

		// Find all transports
		List<TransportsModel> allTransports = getAllTransports();

		// Transform transport entities into DTOs
		List<TransportDTO> transportDTOs = allTransports.stream().map(transport -> {
			boolean associated = associatedTransportIds.contains(transport.getTransportId());
			return new TransportDTO(transport.getTransportId(), transport.getTransportName(), associated);
		}).collect(Collectors.toList());

		return transportDTOs;
	}

	@Override
	public List<TransportsModel> getTransportsByCfsEmail(String cfsemail) throws NotFoundException {
		String cfsId = cfsRepo.getCfsIdByCfsemail(cfsemail);
		if (cfsId == null) {
			throw new NotFoundException("CFs not found");
		}

		// Find transport IDs associated with CFs ID
		List<String> transportIds = transportCfsRepo.findTransportIdsByCfsCfsId(cfsId);

		// Fetch transport details for each transport ID
		List<TransportsModel> transportInfos = new ArrayList<>();
		for (String transportId : transportIds) {
			TransportsModel transport = findTransportByTransportId(transportId);
			if (transport != null) {
				transportInfos.add(transport);
			}
		}

		return transportInfos;
	}

	@Override
	public TransportsModel findTransportByTransportId(String transportId) {
		// Implement logic to fetch transport details from database or any other source
		TransportsModel transportEntity = transportRepo.findByTransportId(transportId);
		if (transportEntity != null) {
			return new TransportsModel(transportEntity.getTransportId(), transportEntity.getTransportName());
		} else {
			return null; // Transport with given ID not found
		}
	}

	// Author SURYA
	@Override
	public List<OrderBooking> getOrdersByEmail(String email) {
		// System.out.println("Email: " + email); // Log the email for debugging
		TransportsModel transport = transportRepo.findByTransportEmail(email);
		if (transport == null) {
			// System.out.println("No transport found for email: " + email); // Log if
			// transport is not found
			return Collections.emptyList(); // Return an empty list if transport is not found
		}

		List<OrderBooking> orders = orderBookingRepository.findByTransportTransportId(transport.getTransportId());

		return orders;
	}
	
	@Override
    public String getTransportNameByEmail(String transportEmail) {
		TransportsModel transport = transportRepo.findByTransportEmail(transportEmail);
        return (transport != null) ? transport.getTransportName() : null;
    }

}
