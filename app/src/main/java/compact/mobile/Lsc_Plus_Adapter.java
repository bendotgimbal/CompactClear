package compact.mobile;

public class Lsc_Plus_Adapter {

	private String airwaybill;
	private String nilai_setoran;
	boolean selected = false;

	public Lsc_Plus_Adapter() {
		// TODO Auto-generated constructor stub
	}

	public Lsc_Plus_Adapter(String airwaybill, String nilai_setoran, boolean selected) {
		super();
		this.airwaybill = airwaybill;
		this.nilai_setoran = nilai_setoran;
	}

	public String getAirWaybill() {
		return airwaybill;
	}

	public void setAirWaybill(String airwaybill) {
		this.airwaybill = airwaybill;
	}

	public String getNilai_Setoran() {
		return nilai_setoran;
	}

	public void setNilai_Setoran(String nilai_setoran) {
		this.nilai_setoran = nilai_setoran;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
