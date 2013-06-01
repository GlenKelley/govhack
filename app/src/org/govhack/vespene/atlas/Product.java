package org.govhack.vespene.atlas;

import java.util.Date;

public class Product {
	  public final String id;
	  public final String name;
	  public final String description;
	  public final String phoneNumber = null;
	  public final String emailAddress = null;
	  public final Category categoryId;
	  public final String imageUrl;
	  // TODO: perhaps also use "boundary" field also. Warning, sometimes is MULTIPOINT(..., ... ) etc.
	  
	  public final LatLng location;
	  public final double locationKms;
	  
	  public final Date startDate;
	  public final Date endDate;
	  public final String openTimes;
	  public final Address address;
	  
	public Product(String id, String name, String description,
			Category categoryId, String imageUrl, LatLng location,
			double locationKms, Date startDate, Date endDate, String openTimes,
			Address address) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.categoryId = categoryId;
		this.imageUrl = imageUrl;
		this.location = location;
		this.locationKms = locationKms;
		this.startDate = startDate;
		this.endDate = endDate;
		this.openTimes = openTimes;
		this.address = address;
	}
	
	public Product(ProductHeader header, ProductDetail detail) {
		super();
		this.id = header.id;
		this.name = header.name;
		this.description = header.description;
		this.categoryId = header.categoryId;
		this.imageUrl = header.imageUrl;
		this.location = header.location;
		this.locationKms = header.locationKms;
		this.startDate = detail.startDate;
		this.endDate = detail.endDate;
		this.openTimes = detail.openTimes;
		this.address = detail.address;
	}

  public String toString() {
    return name;
  }
	  
}
