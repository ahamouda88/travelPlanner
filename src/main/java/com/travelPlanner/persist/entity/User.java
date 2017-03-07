package com.travelPlanner.persist.entity;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Model class that represents a User.
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Version
	private long version;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String username;
	@NotNull
	private String password;
	@NotNull
	private UserRole role;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnoreProperties(value = "user", allowSetters = true)
	private List<Trip> trips;

	public User() {
	}

	public User(String firstName, String lastName, String username, String password, UserRole role) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public List<Trip> getTrips() {
		return trips;
	}

	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((trips == null) ? 0 : trips.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		User other = (User) obj;
		if (firstName == null) {
			if (other.firstName != null) return false;
		} else if (!firstName.equals(other.firstName)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (lastName == null) {
			if (other.lastName != null) return false;
		} else if (!lastName.equals(other.lastName)) return false;
		if (password == null) {
			if (other.password != null) return false;
		} else if (!password.equals(other.password)) return false;
		if (role != other.role) return false;
		if (trips == null) {
			if (other.trips != null) return false;
		} else if (!trips.equals(other.trips)) return false;
		if (username == null) {
			if (other.username != null) return false;
		} else if (!username.equals(other.username)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", password=" + password + ", role=" + role + ", trips=" + trips + "]";
	}

	public static User copyUser(User user) {
		User newUser = new User(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(),
				user.getRole());
		newUser.setTrips(user.getTrips());
		newUser.setId(user.getId());
		return newUser;
	}

	public static boolean isValid(User user) {
		return user != null && isNotEmpty(user.getFirstName()) && isNotEmpty(user.getLastName())
				&& user.getRole() != null && isNotEmpty(user.getUsername()) && isNotEmpty(user.getPassword());
	}

}
