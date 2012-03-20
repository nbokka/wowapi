package ca.wowapi;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.wowapi.entities.Damage;
import ca.wowapi.entities.Item;
import ca.wowapi.entities.ItemSource;
import ca.wowapi.entities.ItemSpell;
import ca.wowapi.entities.Spell;
import ca.wowapi.entities.WeaponInfo;
import ca.wowapi.exceptions.InternalServerErrorException;

public class ItemAPI extends AbstractAPI {

	private final Logger log = Logger.getLogger(this.getClass());

	public static final String ITEM_API_URL = "http://%region.battle.net/api/wow/item/%id";

	public ItemAPI() {

	}

	public ItemAPI(String publicKey, String privateKey) {
		super(publicKey, privateKey);
	}

	public Item getItem(String itemId, String region) throws InternalServerErrorException {
		Item item = new Item();;

		String finalURL = ITEM_API_URL.replace("%region", region).replace("%id", itemId);
		try {
			JSONObject jsonobject = getJSONFromRequest(finalURL);
		    ObjectMapper mapper = new ObjectMapper();  
		    item = mapper.readValue(jsonobject.toString(), Item.class);  
//			item = new Item();
/*
			if (jsonobject.has("id")) {
				item.setId(jsonobject.getInt("id"));
			} else {
				// Sometimes the server responds with nothing so return null
				log.warn("No item id returned... returning null.");
				return null;
			}
			if (jsonobject.has("disenchantingSkillRank")) {
				item.setDisenchantingSkillRank(jsonobject.getInt("disenchantingSkillRank"));
			}
			if (jsonobject.has("description")) {
				item.setDescription(jsonobject.getString("description"));
			}
			if (jsonobject.has("name")) {
				item.setName(jsonobject.getString("name"));
			}
			if (jsonobject.has("stackable")) {
				item.setSellPrice(jsonobject.getInt("stackable"));
			}
			if (jsonobject.has("itemBind")) {
				item.setItemBind(jsonobject.getInt("itemBind"));
			}
			item.setBonusStats(null);
			if (jsonobject.has("itemSpells")) {

				ArrayList<ItemSpell> itemSpells = new ArrayList<ItemSpell>();
				JSONArray spellArray = jsonobject.getJSONArray("itemSpells");
				for (int i = 0; i < spellArray.length(); i++) {

					JSONObject itemSpellObj = spellArray.getJSONObject(i);
					ItemSpell itemSpell = new ItemSpell();
					itemSpell.setSpellId(itemSpellObj.getInt("spellId"));

					JSONObject spellObj = itemSpellObj.getJSONObject("spell");
					Spell spell = new Spell();
					spell.setId(spellObj.getInt("id"));
					spell.setName(spellObj.getString("name"));
					spell.setDescription(spellObj.getString("description"));

					// TODO: need to figure out why this isn't working
					// spell.setCastTime(spellObj.getString("castTime"));

					itemSpell.setSpell(spell);
					itemSpell.setnCharges(itemSpellObj.getInt("nCharges"));
					itemSpell.setConsumable(itemSpellObj.getBoolean("consumable"));
					itemSpell.setCategoryId(itemSpellObj.getInt("categoryId"));

					itemSpells.add(itemSpell);
				}

				item.setItemSpells(itemSpells);
			}

			item.setItemSpells(null);
			if (jsonobject.has("buyPrice")) {
				item.setBuyPrice(jsonobject.getLong("buyPrice"));
			}
			if (jsonobject.has("itemClass")) {
				item.setItemClass(jsonobject.getInt("itemClass"));
			}
			if (jsonobject.has("itemSubClass")) {
				item.setItemSubClass(jsonobject.getInt("itemSubClass"));
			}
			if (jsonobject.has("containerSlots")) {
				item.setContainerSlots(jsonobject.getInt("containerSlots"));
			}
			if (jsonobject.has("weaponInfo")) {
				JSONObject weaponObj = jsonobject.getJSONObject("weaponInfo");
				JSONObject damageObj = weaponObj.getJSONObject("damage");

				Damage damage = new Damage();
				damage.setMinDamage(damageObj.getInt("min")); // minDamage
				damage.setMaxDamage(damageObj.getInt("max")); // maxDamage

				WeaponInfo weaponInfo = new WeaponInfo();
				weaponInfo.setDamage(damage);
				weaponInfo.setWeaponSpeed(weaponObj.getDouble("weaponSpeed"));
				weaponInfo.setDps(weaponObj.getDouble("dps"));

				item.setWeaponInfo(weaponInfo);
			}

			if (jsonobject.has("inventoryType")) {
				item.setInventoryType(jsonobject.getInt("inventoryType"));
			}
			if (jsonobject.has("equippable")) {
				item.setEquippable(jsonobject.getBoolean("equippable"));
			}
			if (jsonobject.has("itemLevel")) {
				item.setItemLevel(jsonobject.getInt("itemLevel"));
			}
			if (jsonobject.has("maxCount")) {
				item.setMaxCount(jsonobject.getInt("maxCount"));
			}
			if (jsonobject.has("maxDurability")) {
				item.setMaxDurability(jsonobject.getInt("maxDurability"));
			}
			if (jsonobject.has("minFactionId")) {
				item.setMinFactionId(jsonobject.getInt("minFactionId"));
			}
			if (jsonobject.has("minReputation")) {
				item.setMinReputation(jsonobject.getInt("minReputation"));
			}
			if (jsonobject.has("quality")) {
				item.setQuality(jsonobject.getInt("quality"));
			}
			if (jsonobject.has("sellPrice")) {
				item.setSellPrice(jsonobject.getLong("sellPrice"));
			}
			if (jsonobject.has("requiredLevel")) {
				item.setRequiredLevel(jsonobject.getInt("requiredLevel"));
			}
			if (jsonobject.has("requiredSkill")) {
				item.setRequiredSkill(jsonobject.getInt("requiredSkill"));
			}
			if (jsonobject.has("requiredSkillRank")) {
				item.setRequiredSkillRank(jsonobject.getInt("requiredSkillRank"));
			}

			if (jsonobject.has("itemSource")) {
				JSONObject itemSourceObj = jsonobject.getJSONObject("itemSource");

				ItemSource itemSource = new ItemSource();
				itemSource.setSourceId(itemSourceObj.getInt("sourceId"));
				itemSource.setSourceType(itemSourceObj.getString("sourceType"));
			}

			if (jsonobject.has("baseArmor")) {
				item.setBaseArmor(jsonobject.getInt("baseArmor"));
			}
			if (jsonobject.has("hasSockets")) {
				item.setHasSockets(jsonobject.getBoolean("hasSockets"));
			}
			if (jsonobject.has("isAuctionable")) {
				item.setAuctionable(jsonobject.getBoolean("isAuctionable"));
			}
			*/
		} catch (InternalServerErrorException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error retrieving item.");
			e.printStackTrace();
		}
		return item;
	}
}
