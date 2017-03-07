package com.travelPlanner.persist.entity;

import static com.travelPlanner.utils.DateUtils.isValidDateRange;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travelPlanner.utils.DateUtils;

/**
 * A Model class the represents a User's Trip.
 */
@Entity
@Table(name = "trips")
public class Trip implements Serializable {

	private static final long serialVersionUID = 1L;

	@Version
	private long version;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	private String destination;
	@NotNull
	private Date startDate;
	@NotNull
	private Date endDate;
	@Lob
	@Column(length = 100000)
	private String comment;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnoreProperties(value = "trips", allowSetters = true)
	private User user;
	@Transient
	@JsonIgnoreProperties(allowGetters = true)
	private int dayCount;

	public Trip() {
	}

	public Trip(String destination, Date startDate, Date endDate) {
		this(destination, startDate, endDate, null);
	}

	public Trip(String destination, Date startDate, Date endDate, String comment) {
		this.destination = destination;
		this.startDate = startDate;
		this.endDate = endDate;
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public int getDayCount() {
		return DateUtils.nbrOfDaysFromCurrent(startDate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Trip other = (Trip) obj;
		if (comment == null) {
			if (other.comment != null) return false;
		} else if (!comment.equals(other.comment)) return false;
		if (destination == null) {
			if (other.destination != null) return false;
		} else if (!destination.equals(other.destination)) return false;
		if (endDate == null) {
			if (other.endDate != null) return false;
		} else if (!endDate.equals(other.endDate)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (startDate == null) {
			if (other.startDate != null) return false;
		} else if (!startDate.equals(other.startDate)) return false;
		if (user == null) {
			if (other.user != null) return false;
		} else if (!user.equals(other.user)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "Trip [id=" + id + ", destination=" + destination + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", comment=" + comment + ", user=" + user + "]";
	}

	public static Trip copyTrip(Trip trip) {
		Trip newTrip = new Trip(trip.getDestination(), trip.getStartDate(), trip.getEndDate(), trip.getComment());
		newTrip.setId(trip.getId());
		newTrip.setUser(trip.getUser());
		return newTrip;
	}

	public static boolean isValid(Trip trip) {
		return trip != null && isNotEmpty(trip.getDestination())
				&& isValidDateRange(trip.getStartDate(), trip.getEndDate()) && trip.getUser() != null;
	}
}
