package com.visara.transportationweb.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visara.transportationweb.Model.CfsModel;
import com.visara.transportationweb.Model.OrderBooking;
import com.visara.transportationweb.Model.TransportsModel;
import com.visara.transportationweb.Repo.CfsRepository;
import com.visara.transportationweb.Repo.OrderBookingRepository;
import com.visara.transportationweb.Repo.TransportRepository;
import com.visara.transportationweb.Service.CfsService;

@Service
public class CfsServiceImpl implements CfsService {

	@Autowired
	private CfsRepository cfsRepo;

	@Autowired
	private TransportRepository transportRepo;
	
	@Autowired
	private OrderBookingRepository orderBookingRepository;

	@Override
	public String getCfsIdByEmail(String email) {
		CfsModel cfs = cfsRepo.findByCfsemail(email);
		if (cfs != null) {
			return cfs.getCfsId();
		} else {
			return null;
		}
	}

	@Override
	public List<TransportsModel> getAssociatedTransports(String cfsemail) { // Find CFs by email
		CfsModel cfs = cfsRepo.findByCfsemail(cfsemail);
		if (cfs == null) { // CFs not found return
			Collections.emptyList();
		}

		// Find associated transports for the CFs
		Set<TransportsModel> associatedTransports = cfs.getTransports();
		return new ArrayList<>(associatedTransports);
	}

	@Override
	public List<TransportsModel> getUnassociatedTransports(String cfsemail) { // Find CFs by email
		CfsModel cfs = cfsRepo.findByCfsemail(cfsemail);
		if (cfs == null) { // CFs not found return
			Collections.emptyList();
		}

		// Find all transports
		List<TransportsModel> allTransports = transportRepo.findAll();

		// Find associated transports
		Set<TransportsModel> associatedTransports = cfs.getTransports();

		// Filter out associated transports to get unassociated ones
		List<TransportsModel> unassociatedTransports = new ArrayList<>();
		for (TransportsModel transport : allTransports) {
			if (!associatedTransports.contains(transport)) {
				unassociatedTransports.add(transport);
			}
		}

		return unassociatedTransports;
	}

	@Override
	public List<CfsModel> findAll() {
		return cfsRepo.findAll();
	}
	
	@Override
    public String getCfsNameByEmail(String cfsEmail) {
		CfsModel cfs = cfsRepo.findByCfsemail(cfsEmail);
        return (cfs != null) ? cfs.getCfsName() : null;
    }

	@Override
	public List<OrderBooking> getOrdersByEmail(String email) {
		CfsModel cfs = cfsRepo.findByCfsemail(email);
        if (cfs == null) {
            return Collections.emptyList();
        }
        List<OrderBooking> orders = orderBookingRepository.findByCfsCfsId(cfs.getCfsId());
        return orders;
	}
	
	 
	 


}
