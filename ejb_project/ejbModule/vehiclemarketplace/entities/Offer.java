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

	private byte archived;

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
	private byte isAccidentFree;

	@Column(name="is_damaged")
	private byte isDamaged;

	@Column(name="is_first_owner")
	private byte isFirstOwner;

	@Column(name="is_new")
	private byte isNew;

	@Column(name="is_registered")
	private byte isRegistered;

	@Column(name="is_right_hand_drive")
	private byte isRightHandDrive;

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

	public Offer() {
	}

	public int getIdOffer() {
		return this.idOffer;
	}

	public void setIdOffer(int idOffer) {
		this.idOffer = idOffer;
	}

	public byte getArchived() {
		return this.archived;
	}

	public void setArchived(byte archived) {
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

	public byte getIsAccidentFree() {
		return this.isAccidentFree;
	}

	public void setIsAccidentFree(byte isAccidentFree) {
		this.isAccidentFree = isAccidentFree;
	}

	public byte getIsDamaged() {
		return this.isDamaged;
	}

	public void setIsDamaged(byte isDamaged) {
		this.isDamaged = isDamaged;
	}

	public byte getIsFirstOwner() {
		return this.isFirstOwner;
	}

	public void setIsFirstOwner(byte isFirstOwner) {
		this.isFirstOwner = isFirstOwner;
	}

	public byte getIsNew() {
		return this.isNew;
	}

	public void setIsNew(byte isNew) {
		this.isNew = isNew;
	}

	public byte getIsRegistered() {
		return this.isRegistered;
	}

	public void setIsRegistered(byte isRegistered) {
		this.isRegistered = isRegistered;
	}

	public byte getIsRightHandDrive() {
		return this.isRightHandDrive;
	}

	public void setIsRightHandDrive(byte isRightHandDrive) {
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

	public List<Equipment> getEquipments() {
		return this.equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}

}