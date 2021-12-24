package vehiclemarketplace.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the offer database table.
 * 
 */
@Entity
@Table(name="offer")
@NamedQuery(name="Offer.findAll", query="SELECT o FROM Offer o")
public class Offer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_offer", unique=true, nullable=false)
	private int idOffer;

	@Column(nullable=false)
	private Boolean archived;

	@Column(nullable=false, length=255)
	private String city;

	@Column(nullable=false, length=20)
	private String color;

	@Column(name="color_type", length=20)
	private String colorType;

	@Column(name="create_time", nullable=false)
	private Timestamp createTime;

	@Lob
	private String description;

	@Column(nullable=false)
	private Float displacement;

	@Column(nullable=false)
	private Byte doors;

	@Column(nullable=false, length=3)
	private String drive;

	@Temporal(TemporalType.DATE)
	@Column(name="first_registration")
	private Date firstRegistration;

	@Column(nullable=false, length=20)
	private String fuel;

	@Column(length=100)
	private String image;

	@Column(name="is_accident_free")
	private Boolean isAccidentFree;

	@Column(name="is_damaged")
	private Boolean isDamaged;

	@Column(name="is_first_owner")
	private Boolean isFirstOwner;

	@Column(name="is_new", nullable=false)
	private Boolean isNew;

	@Column(name="is_registered")
	private Boolean isRegistered;

	@Column(name="is_right_hand_drive")
	private Boolean isRightHandDrive;

	@Column(name="license_plate", length=15)
	private String licensePlate;

	@Column(nullable=false)
	private Integer mileage;

	@Column(nullable=false)
	private Short power;

	@Column(nullable=false)
	private Integer price;

	@Column(name="production_year", nullable=false)
	private Integer productionYear;

	@Column(nullable=false)
	private Byte seats;

	@Column(length=70)
	private String title;

	@Column(nullable=false, length=15)
	private String transmission;

	@Column(name="update_time", updatable=false, nullable=false)
	private Timestamp updateTime;

	@Column(nullable=false, length=17)
	private String vin;

	//bi-directional many-to-one association to BodyStyle
	@ManyToOne
	@JoinColumn(name="id_body_style", nullable=false)
	private BodyStyle bodyStyle;

	//bi-directional many-to-one association to Brand
	@ManyToOne
	@JoinColumn(name="id_brand", nullable=false)
	private Brand brand;

	//bi-directional many-to-many association to Equipment
	@ManyToMany
	@JoinTable(
		name="offer_equipment"
		, joinColumns={
			@JoinColumn(name="id_offer", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_equipment", nullable=false)
			}
		)
	private List<Equipment> equipments;

	//bi-directional many-to-one association to Generation
	@ManyToOne
	@JoinColumn(name="id_generation", nullable=false)
	private Generation generation;

	//bi-directional many-to-one association to Model
	@ManyToOne
	@JoinColumn(name="id_model", nullable=false)
	private Model model;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="id_user", nullable=false)
	private User user;

	public Offer() {
	}

	public int getIdOffer() {
		return this.idOffer;
	}

	public void setIdOffer(int idOffer) {
		this.idOffer = idOffer;
	}

	public Boolean getArchived() {
		return this.archived;
	}

	public void setArchived(Boolean archived) {
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

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getDisplacement() {
		return this.displacement;
	}

	public void setDisplacement(Float displacement) {
		this.displacement = displacement;
	}

	public Byte getDoors() {
		return this.doors;
	}

	public void setDoors(Byte doors) {
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

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getIsAccidentFree() {
		return this.isAccidentFree;
	}

	public void setIsAccidentFree(Boolean isAccidentFree) {
		this.isAccidentFree = isAccidentFree;
	}

	public Boolean getIsDamaged() {
		return this.isDamaged;
	}

	public void setIsDamaged(Boolean isDamaged) {
		this.isDamaged = isDamaged;
	}

	public Boolean getIsFirstOwner() {
		return this.isFirstOwner;
	}

	public void setIsFirstOwner(Boolean isFirstOwner) {
		this.isFirstOwner = isFirstOwner;
	}

	public Boolean getIsNew() {
		return this.isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public Boolean getIsRegistered() {
		return this.isRegistered;
	}

	public void setIsRegistered(Boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public Boolean getIsRightHandDrive() {
		return this.isRightHandDrive;
	}

	public void setIsRightHandDrive(Boolean isRightHandDrive) {
		this.isRightHandDrive = isRightHandDrive;
	}

	public String getLicensePlate() {
		return this.licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Integer getMileage() {
		return this.mileage;
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}

	public Short getPower() {
		return this.power;
	}

	public void setPower(Short power) {
		this.power = power;
	}

	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getProductionYear() {
		return this.productionYear;
	}

	public void setProductionYear(Integer productionYear) {
		this.productionYear = productionYear;
	}

	public Byte getSeats() {
		return this.seats;
	}

	public void setSeats(Byte seats) {
		this.seats = seats;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTransmission() {
		return this.transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
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