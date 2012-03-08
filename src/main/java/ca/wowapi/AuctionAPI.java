package ca.wowapi;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.wowapi.entities.Auction;
import ca.wowapi.entities.AuctionData;
import ca.wowapi.exceptions.NotModifiedException;

public class AuctionAPI extends AbstractAPI {

	private final Logger log = Logger.getLogger(this.getClass());

	public static final String AUCTION_API_URL = "http://%region.battle.net/api/wow/auction/data/%realm";

	public static final String FACTION_ALLIANCE = "alliance";

	public static final String FACTION_HORDE = "horde";

	public static final String FACTION_NEUTRAL = "neutral";

	public AuctionAPI() {

	}

	public AuctionAPI(String publicKey, String privateKey) {
		super(publicKey, privateKey);
	}

	public String getAuctionUrl(String realm, String region) {
		String finalURL = AUCTION_API_URL.replace("%region", region).replace("%realm", realm);

		String auctionUrl = null;
		try {

			JSONObject jsonobject = getJSONFromRequest(finalURL);
			if (null != jsonobject) {
				auctionUrl = jsonobject.getJSONArray("files").getJSONObject(0).getString("url");
				log.debug("Auction URL for Realm: " + realm + " is: " + auctionUrl);
			} else {
				log.error("Unable to retrieve auction URL for Realm: " + realm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return auctionUrl;
	}

	public long getAuctionLastModified(String realm, String region) {
		String finalURL = AUCTION_API_URL.replace("%region", region).replace("%realm", realm);

		long lastModified = 0;
		try {
			JSONObject jsonobject = getJSONFromRequest(finalURL);
			if (null != jsonobject) {
				lastModified = Long.parseLong(jsonobject.getJSONArray("files").getJSONObject(0).getString("lastModified"));
				log.debug("Auction lastModified for Realm: " + realm + " is: " + lastModified);
			} else {
				log.error("Unable to retrieve auction lastModified for Realm: " + realm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lastModified;
	}

	public AuctionData getAllAuctionData(String realm, String region) {
		AuctionData auctionData = null;
		try {
			log.debug("Beginning to retrive auction data for Realm: " + realm + " Region: " + region);

			JSONObject jsonobject = null;
			String auctionUrl = this.getAuctionUrl(realm, region);
			if (null != auctionUrl) {
				jsonobject = getJSONFromRequest(auctionUrl, 0);
			}

			try {
				auctionData = this.getAllAuctionData(auctionUrl, 0);

				if (null != auctionData) {
					List<Auction> allianceAuctions = this.loadAuctions(jsonobject.getJSONObject(FACTION_ALLIANCE).getJSONArray("auctions"));
					auctionData.setAllianceAuctions(allianceAuctions);
					log.debug("Number of auctions found for Faction: " + FACTION_ALLIANCE + " Count: " + allianceAuctions.size());

					List<Auction> hordeAuctions = this.loadAuctions(jsonobject.getJSONObject(FACTION_HORDE).getJSONArray("auctions"));
					auctionData.setHordeAuctions(hordeAuctions);
					log.debug("Number of auctions found for Faction: " + FACTION_HORDE + " Count: " + hordeAuctions.size());

					List<Auction> neutralAuctions = this.loadAuctions(jsonobject.getJSONObject(FACTION_NEUTRAL).getJSONArray("auctions"));
					auctionData.setNeutralAuctions(neutralAuctions);
					log.debug("Number of auctions found for Faction: " + FACTION_NEUTRAL + " Count: " + neutralAuctions.size());

				} else {
					log.error("Unable to retrive auction data for Realm: " + realm + " Region: " + region);
				}
				log.debug("Finished retriving auction data for Realm: " + realm + " Region: " + region);
			} catch (NotModifiedException e) {
				log.warn("Auctions not modified since last run!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return auctionData;
	}

	public AuctionData getAllAuctionData(String auctionUrl, long lastModified) throws NotModifiedException {
		AuctionData auctionData = null;
		try {

			JSONObject jsonobject = getJSONFromRequest(auctionUrl, lastModified);

			if (null != jsonobject) {
				auctionData = new AuctionData();

				List<Auction> allianceAuctions = this.loadAuctions(jsonobject.getJSONObject(FACTION_ALLIANCE).getJSONArray("auctions"));
				auctionData.setAllianceAuctions(allianceAuctions);
				log.debug("Number of auctions found for Faction: " + FACTION_ALLIANCE + " Count: " + allianceAuctions.size());

				List<Auction> hordeAuctions = this.loadAuctions(jsonobject.getJSONObject(FACTION_HORDE).getJSONArray("auctions"));
				auctionData.setHordeAuctions(hordeAuctions);
				log.debug("Number of auctions found for Faction: " + FACTION_HORDE + " Count: " + hordeAuctions.size());

				List<Auction> neutralAuctions = this.loadAuctions(jsonobject.getJSONObject(FACTION_NEUTRAL).getJSONArray("auctions"));
				auctionData.setNeutralAuctions(neutralAuctions);
				log.debug("Number of auctions found for Faction: " + FACTION_NEUTRAL + " Count: " + neutralAuctions.size());

			}
		} catch (NotModifiedException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return auctionData;
	}

	private List<Auction> loadAuctions(JSONArray jAuctionList) throws JSONException {
		List<Auction> auctions = new ArrayList<Auction>(jAuctionList.length());
		for (int j = 0; j < jAuctionList.length(); j++) {
			Auction auctionItem = new Auction();
			auctionItem.setOwner(jAuctionList.getJSONObject(j).getString("owner"));
			auctionItem.setBid(jAuctionList.getJSONObject(j).getLong("bid"));
			auctionItem.setId(jAuctionList.getJSONObject(j).getLong("auc"));
			auctionItem.setItem(jAuctionList.getJSONObject(j).getInt("item"));
			auctionItem.setBuyout(jAuctionList.getJSONObject(j).getLong("buyout"));
			auctionItem.setQuantity(jAuctionList.getJSONObject(j).getInt("quantity"));
			auctionItem.setTimeLeft(jAuctionList.getJSONObject(j).getString("timeLeft"));
			auctions.add(auctionItem);
		}
		return auctions;
	}

}
