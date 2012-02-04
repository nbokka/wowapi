package ca.wowapi.entities;

public class AuctionData {

	private Auction[] allianceAuctions;

	private Auction[] hordeAuctions;

	private Auction[] neutralAuctions;

	public AuctionData() {

	}

	public Auction[] getAllianceAuctions() {
		return allianceAuctions;
	}

	public Auction[] getHordeAuctions() {
		return hordeAuctions;
	}

	public Auction[] getNeutralAuctions() {
		return neutralAuctions;
	}

	public void setAllianceAuctions(Auction[] allianceAuctions) {
		this.allianceAuctions = allianceAuctions;
	}

	public void setHordeAuctions(Auction[] hordeAuctions) {
		this.hordeAuctions = hordeAuctions;
	}

	public void setNeutralAuctions(Auction[] neutralAuctions) {
		this.neutralAuctions = neutralAuctions;
	}

}
