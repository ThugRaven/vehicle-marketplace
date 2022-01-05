package vehiclemarketplace.dashboard.equipment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import vehiclemarketplace.classes.SelectFilter;
import vehiclemarketplace.classes.SelectType;
import vehiclemarketplace.dao.EquipmentDAO;
import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.entities.Equipment;

@Named("dashEquipmentBB")
@ViewScoped
public class DashboardEquipmentBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;

	private Equipment equipment = new Equipment();

	private LazyDataModel<Equipment> lazyEquipment;

	private Equipment selectedEquipment;

	private Equipment equipmentFilter = new Equipment();

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public LazyDataModel<Equipment> getLazyEquipment() {
		return lazyEquipment;
	}

	public Equipment getSelectedEquipment() {
		return selectedEquipment;
	}

	public void setSelectedEquipment(Equipment selectedEquipment) {
		this.selectedEquipment = selectedEquipment;
	}

	public Equipment getEquipmentFilter() {
		return equipmentFilter;
	}

	@Inject
	ExternalContext extcontext;

	@EJB
	EquipmentDAO equipmentDAO;

	@EJB
	OfferDAO offerDAO;

	@Inject
	FacesContext ctx;

	@PostConstruct
	public void init() {
		lazyEquipment = new LazyDataModel<Equipment>() {
			private static final long serialVersionUID = 1L;

			private List<Equipment> equipments;

			@Override
			public Equipment getRowData(String rowKey) {
				for (Equipment equipment : equipments) {
					if (equipment.getIdEquipment() == Integer.parseInt(rowKey)) {
						System.out.println(equipment.getName());
						return equipment;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Equipment equipment) {
				return String.valueOf(equipment.getIdEquipment());
			}

			@Override
			public List<Equipment> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Map<String, String> sortMap = new HashMap<String, String>();
				for (SortMeta sortMeta : sortBy.values()) {
					sortMap.put(sortMeta.getField(), sortMeta.getOrder().toString());
				}

				List<SelectFilter> filter = new ArrayList<>();
				if (equipmentFilter.getIdEquipment() != 0) {
					filter.add(new SelectFilter("idEquipment", equipmentFilter.getIdEquipment(), SelectType.NORMAL));
				}
				if (equipmentFilter.getName() != null && !equipmentFilter.getName().isEmpty()) {
					filter.add(new SelectFilter("name", equipmentFilter.getName(), SelectType.LIKE_FULL));
				}

				equipments = equipmentDAO.getLazyList(sortMap, filter, offset, pageSize);

				int rowCount = (int) equipmentDAO.countLazyList(filter);
				setRowCount(rowCount);

				return equipments;
			}
		};
	}

	public String addEquipment() {
		if (equipmentDAO.getEquipmentByName(equipment.getName()) != null) {
			ctx.addMessage("equipmentForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyposażenie o podanej nazwie już istnieje!", null));
			return PAGE_STAY_AT_THE_SAME;
		}
		equipmentDAO.create(equipment);
		equipment = new Equipment();
		return PAGE_STAY_AT_THE_SAME;
	}

	public String editEquipment() {
		Equipment equipmentOld = equipmentDAO.find(selectedEquipment.getIdEquipment());
		Equipment equipmentDB = equipmentDAO.getEquipmentByName(equipmentOld.getName());
		System.out.println("edit: " + selectedEquipment.getName() + " old: " + equipmentOld.getName() + " db: "
				+ equipmentDB.getName() + " " + selectedEquipment.getIdEquipment());
		if (equipmentDB != null && selectedEquipment.getName().equals(equipmentDB.getName())) {
			System.out.println("Same, no changes");
			return PAGE_STAY_AT_THE_SAME;
		}

		Equipment equipmentCheck = equipmentDAO.getEquipmentByName(selectedEquipment.getName());
		if (equipmentCheck != null) {
			ctx.addMessage("equipmentDialogForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wyposażenie o podanej nazwie już istnieje!", null));
			selectedEquipment = equipmentOld;
			return PAGE_STAY_AT_THE_SAME;
		}
		equipmentDAO.merge(selectedEquipment);
		PrimeFaces.current().executeScript("PF('equipmentEditDialog').hide()");
		return PAGE_STAY_AT_THE_SAME;
	}

	public String deleteEquipment() {
		if (equipmentCountOffers() > 0) {
			ctx.addMessage("equipmentTable",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas usuwania rekordu",
							"Wyposażenie nie może zostać usunięte ponieważ conajmniej jedna oferta go zawiera!"));
			return PAGE_STAY_AT_THE_SAME;
		}

		equipmentDAO.remove(selectedEquipment);
		selectedEquipment = null;
		ctx.addMessage("equipmentTable",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie usunięto rekord!", null));
		return PAGE_STAY_AT_THE_SAME;
	}

	public int equipmentCountOffers() {
		return offerDAO.getOffersByEquipmentID(selectedEquipment.getIdEquipment()).size();
	}
}
