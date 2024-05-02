package com.visara.transportationweb.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visara.transportationweb.Model.CfsModel;
import com.visara.transportationweb.Model.TransportCfs;
import com.visara.transportationweb.Model.TransportsModel;
import com.visara.transportationweb.Repo.CfsRepository;
import com.visara.transportationweb.Repo.TransportCfsRepository;
import com.visara.transportationweb.Repo.TransportRepository;

@RestController
public class TransportCfsController {

	@Autowired
	private CfsRepository cfsRepo;

	@Autowired
	private TransportRepository transportRepo;

	@Autowired
	private TransportCfsRepository transportCfsRepo;

	@PostMapping("/update-transport-cfs")
	public ResponseEntity<String> updateTransportCfsRelationship(@RequestBody Map<String, Object> request) {
		String cfsemail = (String) request.get("cfsemail");
		List<?> selectedTransportsObj = (List<?>) request.get("selectedTransports");

		if (!(selectedTransportsObj instanceof List<?>)) {
			throw new IllegalArgumentException("Invalid data format for selectedTransports");
		}

		List<String> selectedTransports = selectedTransportsObj.stream().map(Object::toString)
				.collect(Collectors.toList());

		// Find the user from the CFS table based on the provided email
		CfsModel cfs = cfsRepo.findByCfsemail(cfsemail);
		if (cfs == null) {
			throw new IllegalArgumentException("CFS with email " + cfsemail + " not found");
		}

		// Check if all selected transports are already associated with the CFS
		boolean allTransportsExist = selectedTransports.stream().allMatch(
				transportId -> transportCfsRepo.existsByCfs_CfsIdAndTransport_TransportId(cfs.getCfsId(), transportId));

		if (allTransportsExist) {
			return ResponseEntity.ok("Selected transports are already associated with the CFS.");
		}

		// Create and save new TransportCfs entities for selected transports that don't
		// already exist
		List<TransportCfs> transportCfsList = new ArrayList<>();
		for (String transportId : selectedTransports) {
			// Check if the TransportCfs entity already exists for the given CFS and
			// transport ID
			if (!transportCfsRepo.existsByCfs_CfsIdAndTransport_TransportId(cfs.getCfsId(), transportId)) {
				TransportsModel transport = transportRepo.findById(transportId).orElseThrow(
						() -> new IllegalArgumentException("Transport with ID " + transportId + " not found"));
				TransportCfs transportCfs = new TransportCfs();
				transportCfs.setTransport(transport);
				transportCfs.setCfs(cfs);
				transportCfsList.add(transportCfs);
			}
		}
		transportCfsRepo.saveAll(transportCfsList);

		// Return a success message to the frontend
		return ResponseEntity.ok("Selected transports have been successfully updated.");
	}

	@GetMapping("/checkTransportAssociation")
	public ResponseEntity<Boolean> checkTransportAssociation(@RequestParam String cfsemail,
			@RequestParam String transportId) {
		// Find the CFS by email
		CfsModel cfsModel = cfsRepo.findByCfsemail(cfsemail);

		if (cfsModel == null) {
			// CFS not found
			return ResponseEntity.ok(false);
		}

		// Check if the given transport is associated with the found CFS
		boolean isAssociated = transportCfsRepo.existsByTransport_TransportIdAndCfs(transportId, cfsModel);

		// Return whether the transport is associated with the CFS
		return ResponseEntity.ok(isAssociated);
	}

}
