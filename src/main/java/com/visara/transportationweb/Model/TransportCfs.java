package com.visara.transportationweb.Model;

import javax.persistence.*;

@Entity
@Table(name = "transport_Cfs")
public class TransportCfs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transport_cfs_id") // Specify the column name explicitly
	private Long transportCfsId; // Corrected field name

	@ManyToOne
	@JoinColumn(name = "transport_id")
	private TransportsModel transport;

	@ManyToOne
	@JoinColumn(name = "cfs_id")
	private CfsModel cfs;

	public Long getTransportCfsId() {
		return transportCfsId;
	}

	public void setTransportCfsId(Long transportCfsId) {
		this.transportCfsId = transportCfsId;
	}

	public TransportsModel getTransport() {
		return transport;
	}

	public void setTransport(TransportsModel transport) {
		this.transport = transport;
	}

	public CfsModel getCfs() {
		return cfs;
	}

	public void setCfs(CfsModel cfs) {
		this.cfs = cfs;
	}

}
