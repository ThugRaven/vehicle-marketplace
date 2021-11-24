package vehiclemarketplace.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the offer database table.
 * 
 */
@Entity
@NamedQuery(name="Offer.findAll", query="SELECT o FROM Offer o")
public class Offer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_offer")
	private int idOffer;

	private boolean archived;

	private String city;

	private String color;

	@Column(name="color_type")
	private String colorType;

	@Lob
	private String description;

	private float displacement;

	private byte doors;

	private String drive;

	@Temporal(TemporalType.DATE)
	@Column(name="first_registration")
	private Date firstRegistration;

	private String fuel;

	@Column(name="is_accident_free")
	private boolean isAccidentFree;

	@Column(name="is_damaged")
	private boolean isDamaged;

	@Column(name="is_first_owner")
	private boolean isFirstOwner;

	@Column(name="is_new")
	private boolean isNew;

	@Column(name="is_registered")
	private boolean isRegistered;

	@Column(name="is_right_hand_drive")
	private boolean isRightHandDrive;

	@Column(name="license_plate")
	private String licensePlate;

	private int mileage;

	private short power;

	private int price;

	@Temporal(TemporalType.DATE)
	@Column(name="production_year")
	private Date productionYear;

	private byte seats;

	private String transmission;

	private String vin;

	//bi-directional many-to-one association to BodyStyle
	@ManyToOne
	@JoinColumn(name="id_body_style")
	private BodyStyle bodyStyle;

	//bi-directional many-to-one association to Brand
	@ManyToOne
	@JoinColumn(name="id_brand")
	private Brand brand;

	//bi-directional many-to-many association to Equipment
	@ManyToMany
	@JoinTable(
		name="offer_equipment"
		, joinColumns={
			@JoinColumn(name="id_offer")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_equipment")
			}
		)
	private List<Equipment> equipments;

	//bi-directional many-to-one association to Generation
	@ManyToOne
	@JoinColumn(name="id_generation")
	private Generation generation;

	//bi-directional many-to-one association to Model
	@ManyToOne
	@JoinColumn(name="id_model")
	private Model model;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="id_user")
	private User user;

	public Offer() {
	}

	public int getIdOffer() {
		return this.idOffer;
	}

	public void setIdOffer(int idOffer) {
		this.idOffer = idOffer;
	}

	public boolean getArchived() {
		return this.archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColorType() {
		return this.colorType;
	}

	public void setColorType(String colorType) {
		this.colorType = colorType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getDisplacement() {
		return this.displacement;
	}

	public void setDisplacement(float displacement) {
		this.displacement = displacement;
	}

	public byte getDoors() {
		return this.doors;
	}

	public void setDoors(byte doors) {
		this.doors = doors;
	}

	public String getDrive() {
		return this.drive;
	}

	public void setDrive(String drive) {
		this.drive = drive;
	}

	public Date getFirstRegistration() {
		return this.firstRegistration;
	}

	public void setFirstRegistration(Date firstRegistration) {
		this.firstRegistration = firstRegistration;
	}

	public String getFuel() {
		return this.fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public boolean getIsAccidentFree() {
		return this.isAccidentFree;
	}

	public void setIsAccidentFree(boolean isAccidentFree) {
		this.isAccidentFree = isAccidentFree;
	}

	public boolean getIsDamaged() {
		return this.isDamaged;
	}

	public void setIsDamaged(boolean isDamaged) {
		this.isDamaged = isDamaged;
	}

	public boolean getIsFirstOwner() {
		return this.isFirstOwner;
	}

	public void setIsFirstOwner(boolean isFirstOwner) {
		this.isFirstOwner = isFirstOwner;
	}

	public boolean getIsNew() {
		return this.isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean getIsRegistered() {
		return this.isRegistered;
	}

	public void setIsRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public boolean getIsRightHandDrive() {
		return this.isRightHandDrive;
	}

	public void setIsRightHandDrive(boolean isRightHandDrive) {
		this.isRightHandDrive = isRightHandDrive;
	}

	public String getLicensePlate() {
		return this.licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public int getMileage() {
		return this.mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public short getPower() {
		return this.power;
	}

	public void setPower(short power) {
		this.power = power;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Date getProductionYear() {
		return this.productionYear;
	}

	public void setProductionYear(Date productionYear) {
		this.productionYear = productionYear;
	}

	public byte getSeats() {
		return this.seats;
	}

	public void setSeats(byte seats) {
		this.seats = seats;
	}

	public String getTransmission() {
		return this.transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getVin() {
		return this.vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public BodyStyle getBodyStyle() {
		return this.bodyStyle;
	}

	public void setBodyStyle(BodyStyle bodyStyle) {
		this.bodyStyle = bodyStyle;
	}

	public Brand getBrand() {
		return this.brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public List<Equipment> getEquipments() {
		return this.equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}

	public Generation getGeneration() {
		return this.generation;
	}

	public void setGeneration(Generation generation) {
		this.generation = generation;
	}

	public Model getModel() {
		return this.model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}