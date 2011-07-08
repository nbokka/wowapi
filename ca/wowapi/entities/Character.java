package ca.wowapi.entities;

import java.util.ArrayList;
import java.util.List;

import ca.wowapi.Achievement;

public class Character {
	
	String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCclass() {
		return cclass;
	}

	public void setCclass(String cclass) {
		this.cclass = cclass;
	}

	public String getFaction() {
		return faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getGuildname() {
		return guildname;
	}

	public void setGuildname(String guildname) {
		this.guildname = guildname;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public java.sql.Date getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(java.sql.Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public int getIlvl() {
		return ilvl;
	}

	public void setIlvl(int ilvl) {
		this.ilvl = ilvl;
	}

	public List<Achievement> getAchievements() {
		return achievements;
	}

	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
	}

	String cclass;
	String faction;
	String gender;
	String race;
	int points;
	int level;
	String guildname;
	String realm;
	String region;
	java.sql.Date lastmodified;
	int ilvl;
	List<Achievement> achievements;
	
	
	public Character ()
	{
		name = "";
		cclass = "";
		faction = "";
		gender = "";
		race = "";
		points = 0;
		level = 0;
		guildname = "";	
		realm = "";
		region = "";
		lastmodified = null;
		ilvl = 0;
		achievements = null;
	}
	
	public String toString ()
	{
		return  name + "," + cclass + "," + faction + "," +gender +"," + race +"," + points +"," + level +"," + guildname +"," + realm +"," + region + "," + "," + lastmodified + "," + ilvl + ", " + faction;
	}
	
}