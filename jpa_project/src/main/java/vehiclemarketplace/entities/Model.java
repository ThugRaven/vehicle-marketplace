package vehiclemarketplace.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the model database table.
 * 
 */
@Entity
@NamedQuery(name="Model.findAll", query="SELECT m FROM Model m")
public class Model implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_model")
	private int idModel;

	private String name;

	//bi-directional many-to-one association to Generation
	@OneToMany(mappedBy="model")
	private List<Generation> generations;

	//bi-directional many-to-one association to Brand
	@ManyToOne
	@JoinColumn(name="id_brand")
	private Brand brand;

	//bi-directional many-to-one association to Offer
	@OneToMany(mappedBy="model")
	private List<Offer> offers;

	public Model() {
	}

	public int getIdModel() {
		return this.idModel;
	}

	public void setIdModel(int idModel) {
		this.idModel = idModel;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Generation> getGenerations() {
		return this.generations;
	}

	public void setGenerations(List<Generation> generations) {
		this.generations = generations;
	}

	public Generation addGeneration(Generation generation) {
		getGenerations().add(generation);
		generation.setModel(this);

		return generation;
	}

	public Generation removeGeneration(Generation generation) {
		getGenerations().remove(generation);
		generation.setModel(null);

		return generation;
	}

	public Brand getBrand() {
		return this.brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public List<Offer> getOffers() {
		return this.offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}

	public Offer addOffer(Offer offer) {
		getOffers().add(offer);
		offer.setModel(this);

		return offer;
	}

	public Offer removeOffer(Offer offer) {
		getOffers().remove(offer);
		offer.setModel(null);

		return offer;
	}

}