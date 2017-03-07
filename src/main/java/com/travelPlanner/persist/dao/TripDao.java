package com.travelPlanner.persist.dao;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.travelPlanner.model.request.TripSearchRequest;
import com.travelPlanner.persist.entity.Trip;

/**
 * A class that extends {@link AbstractDao}, and it defines the persistence operations to be performed on {@link Trip}
 */
@Repository
public class TripDao extends AbstractDao<Trip, Long> {

	public TripDao() {
		super(Trip.class);
	}

	/**
	 * This method searches through the trips given a {@link TripSearchRequest} having all the parameters needed to
	 * perform the search
	 * 
	 * @param searchRequest
	 *            a {@link TripSearchRequest}
	 * @return list of trips if the selection is successful, otherwise it will return <b>null</b>
	 */
	public List<Trip> searchTrips(TripSearchRequest searchRequest) {
		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Trip> cq = cb.createQuery(Trip.class);
			Root<Trip> from = cq.from(Trip.class);
			cq.select(from);

			// List of predicates
			List<Predicate> predicates = new ArrayList<>();
			if (isNotBlank(searchRequest.getDestination())) {
				predicates.add(cb.equal(from.get("destination"), searchRequest.getDestination()));
			}
			if (searchRequest.getStartDate() != null && searchRequest.getEndDate() != null) {
				predicates.add(cb.greaterThanOrEqualTo(from.get("startDate"), searchRequest.getStartDate()));
				predicates.add(cb.lessThanOrEqualTo(from.get("startDate"), searchRequest.getEndDate()));
			}
			if (searchRequest.getUserId() != null) {
				predicates.add(cb.equal(from.get("user"), searchRequest.getUserId()));
			}

			// If there is no predicates then get all trips
			if (!predicates.isEmpty()) {
				cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
			}
			TypedQuery<Trip> query = entityManager.createQuery(cq);
			return query.getResultList();
		} catch (Exception ex) {
			logger.error("Unable Search Trip given the following request: " + searchRequest, ex);
			return null;
		}
	}

}
