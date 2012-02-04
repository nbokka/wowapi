package ca.wowapi;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.wowapi.entities.Auction;
import ca.wowapi.entities.AuctionData;

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
				jsonobject = getJSONFromRequest(auctionUrl);
			}

			if (null != jsonobject) {
				auctionData = new AuctionData();

				Auction[] allianceAuctions = this.loadAuctionArray(jsonobject.getJSONObject(FACTION_ALLIANCE).getJSONArray("auctions"));
				auctionData.setAllianceAuctions(allianceAuctions);
				log.debug("Number of auctions found for Faction: " + FACTION_ALLIANCE + " Count: " + allianceAuctions.length);

				Auction[] hordeAuctions = this.loadAuctionArray(jsonobject.getJSONObject(FACTION_HORDE).getJSONArray("auctions"));
				auctionData.setHordeAuctions(hordeAuctions);
				log.debug("Number of auctions found for Faction: " + FACTION_HORDE + " Count: " + hordeAuctions.length);

				Auction[] neutralAuctions = this.loadAuctionArray(jsonobject.getJSONObject(FACTION_NEUTRAL).getJSONArray("auctions"));
				auctionData.setNeutralAuctions(neutralAuctions);
				log.debug("Number of auctions found for Faction: " + FACTION_NEUTRAL + " Count: " + neutralAuctions.length);

			} else {
				log.error("Unable to retrive auction data for Realm: " + realm + " Region: " + region);
			}
			log.debug("Finished retriving auction data for Realm: " + realm + " Region: " + region);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return auctionData;
	}

	private Auction[] loadAuctionArray(JSONArray jAuctionList) throws JSONException {
		Auction[] auctionArray = new Auction[jAuctionList.length()];
		for (int j = 0; j < jAuctionList.length(); j++) {
			Auction auctionItem = new Auction();
			auctionItem.setOwner(jAuctionList.getJSONObject(j).getString("owner"));
			auctionItem.setBid(jAuctionList.getJSONObject(j).getLong("bid"));
			auctionItem.setId(jAuctionList.getJSONObject(j).getLong("auc"));
			auctionItem.setItem(jAuctionList.getJSONObject(j).getInt("item"));
			auctionItem.setBuyout(jAuctionList.getJSONObject(j).getLong("buyout"));
			auctionItem.setQuantity(jAuctionList.getJSONObject(j).getInt("quantity"));
			auctionItem.setTimeLeft(jAuctionList.getJSONObject(j).getString("timeLeft"));
			auctionArray[j] = auctionItem;
		}
		return auctionArray;
	}

}
