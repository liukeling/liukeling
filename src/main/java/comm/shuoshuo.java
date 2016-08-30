package comm;

import java.io.Serializable;

public class shuoshuo implements Serializable {
	private int form_ssid = -1;
	private String neirong="";
	private String fabutime;
	private String ssid;
	private user ssuser;
	private String dianzanshu;

	public boolean isDz() {
		return dz;
	}

	public void setDz(boolean dz) {
		this.dz = dz;
	}

	private boolean dz;
	public void setDianzanshu(String dianzanshu) {
		this.dianzanshu = dianzanshu;
	}
	public String getDianzanshu() {
		return dianzanshu;
	}
	public void setFabutime(String fabutime) {
		this.fabutime = fabutime;
	}
	public String getFabutime() {
		return fabutime;
	}
	public void setNeirong(String neirong) {
		this.neirong = neirong;
	}
	public String getNeirong() {
		return neirong;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getSsid() {
		return ssid;
	}
	public void setForm_ssid(int form_ssid) {
		this.form_ssid = form_ssid;
	}
	public int getForm_ssid() {
		return form_ssid;
	}
	public void setSsuser(user ssuser) {
		this.ssuser = ssuser;
	}
	public user getSsuser() {
		return ssuser;
	}
}
