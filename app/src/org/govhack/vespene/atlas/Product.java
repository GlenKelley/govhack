package org.govhack.vespene.atlas;

import java.util.Date;
import java.util.List;

public class Product {
	  public final String id;
	  public final String name;
	  public final String description;
	  public final String phoneNumber;
	  public final String emailAddress;
	  public final String url;
	  public final Category categoryId;
	  public final String imageUrl;
	  // TODO: perhaps also use "boundary" field also. Warning, sometimes is MULTIPOINT(..., ... ) etc.
	  
	  public final LatLng location;
	  public final double locationKms;
	  
	  public final Date startDate;
	  public final Date endDate;
	  public final String openTimes;
	  public final Address address;
	  public final List<String> multimedia;
	  
	public Product(ProductHeader header, ProductDetail detail) {
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
		this.multimedia = detail.multimedia;
		this.phoneNumber = detail.phone;
		this.emailAddress = detail.email;
		this.url = detail.url;
	}

  public String toString() {
    return name;
  }
	  
}
