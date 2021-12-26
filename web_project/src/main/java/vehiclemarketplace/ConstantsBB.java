package vehiclemarketplace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

@Named
@ApplicationScoped
public class ConstantsBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<String> fuels = new ArrayList<>();
	private List<String> transmissions = new ArrayList<>();
	private List<SelectItem> drives = new ArrayList<>();
	private List<Byte> doors = new ArrayList<>();
	private List<Byte> seats = new ArrayList<>();
	private List<String> colors = new ArrayList<>();
	private List<String> colorTypes = new ArrayList<>();

	public List<String> getFuels() {
		return fuels;
	}

	public List<String> getTransmissions() {
		return transmissions;
	}

	public List<SelectItem> getDrives() {
		return drives;
	}

	public List<Byte> getDoors() {
		return doors;
	}

	public List<Byte> getSeats() {
		return seats;
	}

	public List<String> getColors() {
		return colors;
	}

	public List<String> getColorTypes() {
		return colorTypes;
	}

	@PostConstruct
	public void init() {
		fuels.add("Benzyna");
		fuels.add("Benzyna + LPG");
		fuels.add("Diesel");
		fuels.add("Elektryczny");
		fuels.add("Hybryda");

		transmissions.add("Manualna");
		transmissions.add("Automatyczna");

		drives.add(new SelectItem("FWD", "Na przednie koła"));
		drives.add(new SelectItem("RWD", "Na tylne koła"));
		drives.add(new SelectItem("AWD", "Na wszystkie koła 4x4"));

		for (int i = 2; i <= 6; i++) {
			doors.add((byte) i);
		}

		for (int i = 1; i <= 9; i++) {
			seats.add((byte) i);
		}

		colors.add("Beżowy");
		colors.add("Biały");
		colors.add("Bordowy");
		colors.add("Brązowy");
		colors.add("Czarny");
		colors.add("Czerwony");
		colors.add("Fioletowy");
		colors.add("Niebieski");
		colors.add("Srebrny");
		colors.add("Szary");
		colors.add("Zielony");
		colors.add("Złoty");
		colors.add("Żółty");

		colorTypes.add("Matowy");
		colorTypes.add("Metalik");
		colorTypes.add("Perłowy");
	}
}
