package vehiclemarketplace.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the generation database table.
 * 
 */
@Entity
@NamedQuery(name="Generation.findAll", query="SELECT g FROM Generation g")
public class Generation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_generation")
	private int idGeneration;

	private String name;

	@Temporal(TemporalType.DATE)
	@Column(name="production_end")
	private Date productionEnd;

	@Temporal(TemporalType.DATE)
	@Column(name="production_start")
	private Date productionStart;

	//bi-directional many-to-one association to Model
	@ManyToOne
	@JoinColumn(name="id_model")
	private Model model;

	//bi-directional many-to-one association to Offer
	@OneToMany(mappedBy="generation")
	private List<Offer> offers;

	public Generation() {
	}

	public int getIdGeneration() {
		return this.idGeneration;
	}

	public void setIdGeneration(int idGeneration) {
		this.idGeneration = idGeneration;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getProductionEnd() {
		return this.productionEnd;
	}

	public void setProductionEnd(Date productionEnd) {
		this.productionEnd = productionEnd;
	}

	public Date getProductionStart() {
		return this.productionStart;
	}

	public void setProductionStart(Date productionStart) {
		this.productionStart = productionStart;
	}

	public Model getModel() {
		return this.model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public List<Offer> getOffers() {
		return this.offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
	}

	public Offer addOffer(Offer offer) {
		getOffers().add(offer);
		offer.setGeneration(this);

		return offer;
	}

	public Offer removeOffer(Offer offer) {
		getOffers().remove(offer);
		offer.setGeneration(null);

		return offer;
	}

}