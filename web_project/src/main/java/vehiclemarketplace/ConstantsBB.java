package vehiclemarketplace;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
	private List<String> prices = new ArrayList<>();
	private List<String> years = new ArrayList<>();
	private List<String> mileages = new ArrayList<>();

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

	public List<String> getPrices() {
		return prices;
	}

	public List<String> getYears() {
		return years;
	}

	public List<String> getMileages() {
		return mileages;
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

		prices.add("2000");
		prices.add("3000");
		prices.add("5000");
		prices.add("10000");
		prices.add("15000");
		prices.add("20000");
		prices.add("25000");
		prices.add("30000");
		prices.add("35000");
		prices.add("40000");
		prices.add("45000");
		prices.add("50000");
		prices.add("75000");
		prices.add("100000");
		prices.add("200000");
		prices.add("500000");
		prices.add("10000000");
		prices.add("50000000");

		int year = ZonedDateTime.now(ZoneId.of("Europe/Warsaw")).getYear();
		for (int i = year; i >= 1990; i--) {
			years.add(String.valueOf(i));
		}

		mileages.add("20000");
		mileages.add("35000");
		mileages.add("50000");
		mileages.add("75000");
		mileages.add("100000");
		mileages.add("125000");
		mileages.add("150000");
		mileages.add("200000");
		mileages.add("250000");
	}
}
