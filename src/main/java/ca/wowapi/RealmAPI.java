package ca.wowapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.wowapi.entities.Auction;
import ca.wowapi.entities.Realm;

public class RealmAPI extends AbstractAPI {

	private final Logger log = Logger.getLogger(this.getClass());

	public static final String REALM_API_URL = "http://%region.battle.net/api/wow/realm/status";

	public RealmAPI() {

	}

	public RealmAPI(String publicKey, String privateKey) {
		super(publicKey, privateKey);
	}

	public Realm getRealm(String name, String region) {
		String finalURL = REALM_API_URL.replace("%region", region);
		finalURL += "?realm=" + name;

		Realm realm = null;
		try {
			JSONObject jsonobject = getJSONFromRequest(finalURL);
			JSONArray jarray = jsonobject.getJSONArray("realms");
			jsonobject = jarray.getJSONObject(0);

			realm = new Realm();
			realm = this.getRealm(jsonobject);
			/*
			realm.setName(jsonobject.getString("name"));
			realm.setPopulation(jsonobject.getString("population"));
			realm.setType(jsonobject.getString("type"));
			realm.setSlug(jsonobject.getString("slug"));
			realm.setStatus(jsonobject.getBoolean("status"));
			realm.setQueue(jsonobject.getBoolean("queue"));
			*/
			
			
		} catch (Exception e) {
			log.error("Error retrieving realm.", e);
			e.printStackTrace();
		}
		return realm;
	}

	public List<Realm> getRealmList(String region) {
		String finalURL = REALM_API_URL.replace("%region", region);

		ArrayList<Realm> list = null;
		try {
			JSONObject jsonobject = getJSONFromRequest(finalURL);
			JSONArray jarray = jsonobject.getJSONArray("realms");

			list = new ArrayList<Realm>();
			for (int i = 0; i < jarray.length(); i++) {

				jsonobject = jarray.getJSONObject(i);

				Realm realm = new Realm();
				realm = this.getRealm(jsonobject);
				/*
				realm.setName(jsonobject.getString("name"));
				realm.setPopulation(jsonobject.getString("population"));
				realm.setType(jsonobject.getString("type"));
				realm.setSlug(jsonobject.getString("slug"));
				realm.setStatus(jsonobject.getBoolean("status"));
				realm.setQueue(jsonobject.getBoolean("queue"));
				*/

				list.add(realm);
			}
		} catch (Exception e) {
			log.error("Error retrieving realm list.", e);
			e.printStackTrace();
		}
		return list;
	}

	public List<String> getRealmNamesList(String region) {
		String finalURL = REALM_API_URL.replace("%region", region);

		ArrayList<String> names = null;
		try {
			log.debug("Attempting to retrieve realm name list.");
			JSONObject jsonobject = getJSONFromRequest(finalURL);
			if (null != jsonobject) {
				names = new ArrayList<String>();

				JSONArray jarray = jsonobject.getJSONArray("realms");
				for (int i = 0; i < jarray.length(); i++) {
					jsonobject = jarray.getJSONObject(i);
					names.add(this.getRealm(jsonobject).getName());
				}
			}
			log.debug("Success retreiving realm name list.");
		} catch (Exception e) {
			log.error("Error retreiving realm name list.", e);
			e.printStackTrace();
		}
		return names;
	}
	
	private Realm getRealm(JSONObject jsonobject) throws JsonParseException, JsonMappingException, IOException{
		Realm realm = new Realm();
	    ObjectMapper mapper = new ObjectMapper();  
	    realm = mapper.readValue(jsonobject.toString(), Realm.class);
	    return realm;
	}
	

}
