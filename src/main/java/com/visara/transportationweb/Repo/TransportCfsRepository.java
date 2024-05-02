package com.visara.transportationweb.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visara.transportationweb.Model.CfsModel;
import com.visara.transportationweb.Model.TransportCfs;

@Repository
public interface TransportCfsRepository extends JpaRepository<TransportCfs, Long> {

	boolean existsByCfs_CfsIdAndTransport_TransportId(String cfsId, String transportId);

	List<TransportCfs> findByCfs_CfsId(String cfsId);

	List<String> findTransportIdsByCfsCfsId(String cfsId);

	boolean existsByTransport_TransportIdAndCfs(String transportId, CfsModel cfsModel);

}
