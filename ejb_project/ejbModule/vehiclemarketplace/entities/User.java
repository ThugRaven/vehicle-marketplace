package vehiclemarketplace.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "User [idUser=" + idUser + ", archived=" + archived + ", createTime=" + createTime + ", email=" + email
				+ ", login=" + login + ", name=" + name + ", password=" + password + ", phoneNumber=" + phoneNumber
				+ ", surname=" + surname + ", offers=" + offers + ", userRole=" + userRole + "]";
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_user", unique=true, nullable=false)
	private int idUser;

	@Column(nullable=false)
	private Boolean archived;

	@Column(name="create_time", nullable=false)
	private Timestamp createTime;

	@Column(nullable=false, length=255)
	private String email;

	@Column(nullable=false, length=45)
	private String login;

	@Column(nullable=false, length=45)
	private String name;

	@Column(nullable=false, length=73)
	private String password;

	@Column(name="phone_number", nullable=false, length=15)
	private String phoneNumber;

	@Column(nullable=false, length=45)
	private String surname;

	//bi-directional many-to-one association to Offer
	@OneToMany(mappedBy="user")
	private List<Offer> offers;

	//bi-directional many-to-one association to UserRole
	@ManyToOne
	@JoinColumn(name="id_user_role", nullable=false)
	private UserRole userRole;

	public User() {
	}

	public int getIdUser() {
		return this.idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public Boolean getArchived() {
		return this.archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Offer> getOffers() {
		return this.offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}

	public Offer addOffer(Offer offer) {
		getOffers().add(offer);
		offer.setUser(this);

		return offer;
	}

	public Offer removeOffer(Offer offer) {
		getOffers().remove(offer);
		offer.setUser(null);

		return offer;
	}

	public UserRole getUserRole() {
		return this.userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

}