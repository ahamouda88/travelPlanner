package com.travelPlanner.model.request;

import java.util.Date;

/**
 * A class that contains all the parameters needed to perform search on the Trip model/entity
 */
public class TripSearchRequest {

	private String destination;
	private Date startDate;
	private Date endDate;
	private Long userId;

	private TripSearchRequest(Builder builder) {
		this.destination = builder.destination;
		this.startDate = builder.startDate;
		this.endDate = builder.endDate;
		this.userId = builder.userId;
	}

	public String getDestination() {
		return destination;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Long getUserId() {
		return userId;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String destination;
		private Date startDate;
		private Date endDate;
		private Long userId;

		public Builder destination(String destination) {
			this.destination = destination;
			return this;
		}

		public Builder startDate(Date startDate) {
			this.startDate = startDate;
			return this;
		}

		public Builder endDate(Date endDate) {
			this.endDate = endDate;
			return this;
		}

		public Builder userId(Long userId) {
			this.userId = userId;
			return this;
		}

		public TripSearchRequest build() {
			return new TripSearchRequest(this);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TripSearchRequest other = (TripSearchRequest) obj;
		if (destination == null) {
			if (other.destination != null) return false;
		} else if (!destination.equals(other.destination)) return false;
		if (endDate == null) {
			if (other.endDate != null) return false;
		} else if (!endDate.equals(other.endDate)) return false;
		if (startDate == null) {
			if (other.startDate != null) return false;
		} else if (!startDate.equals(other.startDate)) return false;
		if (userId == null) {
			if (other.userId != null) return false;
		} else if (!userId.equals(other.userId)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "TripSearchRequest [destination=" + destination + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", userId=" + userId + "]";
	}
}
