package vehiclemarketplace.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the brand database table.
 * 
 */
@Entity
@Table(name="brand")
@NamedQuery(name="Brand.findAll", query="SELECT b FROM Brand b")
public class Brand implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "Brand [idBrand=" + idBrand + ", name=" + name + ", models=" + models + ", offers=" + offers + "]";
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_brand", unique=true, nullable=false)
	private int idBrand;

	@Column(nullable=false, length=45)
	private String name;

	//bi-directional many-to-one association to Model
	@OneToMany(mappedBy="brand")
	private List<Model> models;

	//bi-directional many-to-one association to Offer
	@OneToMany(mappedBy="brand")
	private List<Offer> offers;

	public Brand() {
	}

	public int getIdBrand() {
		return this.idBrand;
	}

	public void setIdBrand(int idBrand) {
		this.idBrand = idBrand;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Model> getModels() {
		return this.models;
	}

	public void setModels(List<Model> models) {
		this.models = models;
	}

	public Model addModel(Model model) {
		getModels().add(model);
		model.setBrand(this);

		return model;
	}

	public Model removeModel(Model model) {
		getModels().remove(model);
		model.setBrand(null);

		return model;
	}

	public List<Offer> getOffers() {
		return this.offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}

	public Offer addOffer(Offer offer) {
		getOffers().add(offer);
		offer.setBrand(this);

		return offer;
	}

	public Offer removeOffer(Offer offer) {
		getOffers().remove(offer);
		offer.setBrand(null);

		return offer;
	}

}