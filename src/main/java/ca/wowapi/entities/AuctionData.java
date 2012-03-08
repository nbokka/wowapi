package ca.wowapi.entities;

import java.util.List;

public class AuctionData {

	private List<Auction> allianceAuctions;

	private List<Auction> hordeAuctions;

	private List<Auction> neutralAuctions;

	public AuctionData() {

	}

	public List<Auction> getAllianceAuctions() {
		return allianceAuctions;
	}

	public List<Auction> getHordeAuctions() {
		return hordeAuctions;
	}

	public List<Auction> getNeutralAuctions() {
		return neutralAuctions;
	}

	public void setAllianceAuctions(List<Auction> allianceAuctions) {
		this.allianceAuctions = allianceAuctions;
	}

	public void setHordeAuctions(List<Auction> hordeAuctions) {
		this.hordeAuctions = hordeAuctions;
	}

	public void setNeutralAuctions(List<Auction> neutralAuctions) {
		this.neutralAuctions = neutralAuctions;
	}

}
