package ca.wowapi.entities;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class Realm implements Serializable{

	private static final long serialVersionUID = 1L;

	private String name;

	private String population;

	private boolean queue;

	private String slug;

	private boolean status;

	private String type;

	public String getName() {
		return name;
	}

	public String getPopulation() {
		return population;
	}

	public String getSlug() {
		return slug;
	}

	public String getType() {
		return type;
	}

	public boolean isQueue() {
		return queue;
	}

	public boolean isStatus() {
		return status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPopulation(String population) {
		this.population = population;
	}
	public void setQueue(boolean queue) {
		this.queue = queue;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return name + ", " + slug + ", " + population + ", " + type + ", " + status + ", " + queue;
	}

}

enum RealmPopulation {
	FULL, HIGH, LOW, MEDIUM, UNKNOWN
}

enum RealmType {
	PVE, PVP, RPPVE, RPPVP, UNKNOWN
}
