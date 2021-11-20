package vehiclemarketplace.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the equipment database table.
 * 
 */
@Entity
@NamedQuery(name="Equipment.findAll", query="SELECT e FROM Equipment e")
public class Equipment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_equipment")
	private int idEquipment;

	private String name;

	//bi-directional many-to-many association to Offer
	@ManyToMany(mappedBy="equipments")
	private List<Offer> offers;

	public Equipment() {
	}

	public int getIdEquipment() {
		return this.idEquipment;
	}

	public void setIdEquipment(int idEquipment) {
		this.idEquipment = idEquipment;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Offer> getOffers() {
		return this.offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}

}