package vehiclemarketplace.offer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

import vehiclemarketplace.dao.BodyStyleDAO;
import vehiclemarketplace.dao.BrandDAO;
import vehiclemarketplace.dao.EquipmentDAO;
import vehiclemarketplace.dao.GenerationDAO;
import vehiclemarketplace.dao.ModelDAO;
import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.entities.BodyStyle;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.Equipment;
import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;
import vehiclemarketplace.entities.Offer;
import vehiclemarketplace.entities.User;

@Named
@ViewScoped
public class OfferNewBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_OFFER = "/pages/public/offer?faces-redirect=true";

	private Offer offer = new Offer();

	private List<Brand> brands;
	private List<Model> models;
	private List<Generation> generations;
	private List<BodyStyle> bodyStyles;
	private List<Equipment> equipments;

	private UploadedFile image;
	private Path filePath;

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public List<Brand> getBrands() {
		return brands;
	}

	public List<Model> getModels() {
		return models;
	}

	public List<Generation> getGenerations() {
		return generations;
	}

	public List<BodyStyle> getBodyStyles() {
		return bodyStyles;
	}

	public List<Equipment> getEquipments() {
		return equipments;
	}

	public UploadedFile getImage() {
		return image;
	}

	public void setImage(UploadedFile image) {
		this.image = image;
	}

	public Path getFilePath() {
		return filePath;
	}

	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	@Inject
	ExternalContext extcontext;

	@EJB
	OfferDAO offerDAO;

	@EJB
	BrandDAO brandDAO;

	@EJB
	ModelDAO modelDAO;

	@EJB
	GenerationDAO generationDAO;

	@EJB
	BodyStyleDAO bodyStyleDAO;

	@EJB
	EquipmentDAO equipmentDAO;

	@EJB
	UserDAO userDAO;

	@Inject
	FacesContext ctx;

	@Inject
	UserFileManager userFileManager;

	@PostConstruct
	public void init() {
		offer.setBrand(new Brand());
		offer.setModel(new Model());
		offer.setGeneration(new Generation());
		offer.setBodyStyle(new BodyStyle());

		brands = getBrandList();

		bodyStyles = getBodyStyleList();

		equipments = getEquipmentList();
		System.out.println(equipments.toString());

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		RemoteClient<User> client = (RemoteClient<User>) RemoteClient.load(session);
		System.out.println("Client: " + client.getDetails().getLogin() + " " + client.getDetails().getIdUser());
		User user = userDAO.find(client.getDetails().getIdUser());
		offer.setUser(user);
	}

	public List<Brand> getBrandList() {
		return brandDAO.getFullList();
	}

	public void changeBrand() {
		if (offer.getBrand() != null && offer.getBrand().getIdBrand() != 0) {
			models = modelDAO.getModelsByBrandID(offer.getBrand().getIdBrand());
		} else {
			models = null;
			generations = null;
		}
	}

	public void changeModel() {
		if (offer.getModel() != null && offer.getModel().getIdModel() != 0) {
			generations = generationDAO.getGenerationsByModelID(offer.getModel().getIdModel());
		} else {
			generations = null;
		}
	}

	public List<BodyStyle> getBodyStyleList() {
		return bodyStyleDAO.getFullList();
	}

	public List<Equipment> getEquipmentList() {
		return equipmentDAO.getFullList();
	}

	public void handleFileUpload(FileUploadEvent event) {
		System.out.println(event.getFile().getFileName());
		image = event.getFile();

		Path folder = Paths.get(System.getProperty("jboss.server.data.dir"), "offer_images");

		try (InputStream input = image.getInputStream()) {
			String fileName = FilenameUtils.getBaseName(image.getFileName());
			String extension = FilenameUtils.getExtension(image.getFileName());

			filePath = Files.createTempFile(folder, fileName + "-", "." + extension);

			Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);

			userFileManager.destroy();
			userFileManager.addUnconfirmedUploadedFile(filePath.toFile());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String addOffer() {
		userFileManager.confirmUploadedFile(filePath.toFile());
		userFileManager.destroy();
		offer.setImage(filePath.getFileName().toString());
		if (offer.getLicensePlate().isEmpty()) {
			offer.setLicensePlate(null);
		}
		offer.setArchived(false);
		offerDAO.create(offer);
		String offerUrl = PAGE_OFFER + "c=" + offer.getBrand().getName() + " " + offer.getModel().getName() + " "
				+ offer.getTitle() + "&o=" + offer.getIdOffer();
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie dodano ogłoszenie!", null));
		extcontext.getFlash().setKeepMessages(true);

		return offerUrl;
	}
}
