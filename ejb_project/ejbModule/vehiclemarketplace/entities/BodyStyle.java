package vehiclemarketplace.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the body_style database table.
 * 
 */
@Entity
@Table(name="body_style")
@NamedQuery(name="BodyStyle.findAll", query="SELECT b FROM BodyStyle b")
public class BodyStyle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_body_style", unique=true, nullable=false)
	private int idBodyStyle;

	@Column(nullable=false, length=45)
	private String name;

	//bi-directional many-to-one association to Offer
	@OneToMany(mappedBy="bodyStyle")
	private List<Offer> offers;

	public BodyStyle() {
	}

	public int getIdBodyStyle() {
		return this.idBodyStyle;
	}

	public void setIdBodyStyle(int idBodyStyle) {
		this.idBodyStyle = idBodyStyle;
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

	public Offer addOffer(Offer offer) {
		getOffers().add(offer);
		offer.setBodyStyle(this);

		return offer;
	}

	public Offer removeOffer(Offer offer) {
		getOffers().remove(offer);
		offer.setBodyStyle(null);

		return offer;
	}

}