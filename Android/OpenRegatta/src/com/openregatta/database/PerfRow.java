package com.openregatta.database;
/**
 * This class stores a row of the performance database
 */
public class PerfRow {
	private int id;
	private float tws;
	private float twa;
	private float v;
	private float vmg;
	private float heel;
	private float reef;
	private float flat;
	private float aws;
	private float awa;
	private float lee;
	private String sail;
	private boolean isBest;
	private int boatId;
	
	public PerfRow()
	{}
	
	public PerfRow(int id, float tws, float twa, float v, float vmg, float heel, float reef, float flat, float aws, float awa, float lee, String sail, boolean isBest, int boatId)
	{
		this.id = id;
		this.tws = tws;
		this.twa = twa;
		this.v = v;
		this.vmg = vmg;
		this.heel = heel;
		this.reef = reef;
		this.flat = flat;
		this.aws = aws;
		this.awa = awa;
		this.lee = lee;
		this.sail = sail;
		this.isBest = isBest;
		this.boatId = boatId;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the tws
	 */
	public float getTws() {
		return tws;
	}
	/**
	 * @param tws the tws to set
	 */
	public void setTws(float tws) {
		this.tws = tws;
	}
	/**
	 * @return the twa
	 */
	public float getTwa() {
		return twa;
	}
	/**
	 * @param twa the twa to set
	 */
	public void setTwa(float twa) {
		this.twa = twa;
	}
	/**
	 * @return the v
	 */
	public float getV() {
		return v;
	}
	/**
	 * @param v the v to set
	 */
	public void setV(float v) {
		this.v = v;
	}
	/**
	 * @return the vmg
	 */
	public float getVmg() {
		return vmg;
	}
	/**
	 * @param vmg the vmg to set
	 */
	public void setVmg(float vmg) {
		this.vmg = vmg;
	}
	/**
	 * @return the heel
	 */
	public float getHeel() {
		return heel;
	}
	/**
	 * @param heel the heel to set
	 */
	public void setHeel(float heel) {
		this.heel = heel;
	}
	/**
	 * @return the reef
	 */
	public float getReef() {
		return reef;
	}
	/**
	 * @param reef the reef to set
	 */
	public void setReef(float reef) {
		this.reef = reef;
	}
	/**
	 * @return the flat
	 */
	public float getFlat() {
		return flat;
	}
	/**
	 * @param flat the flat to set
	 */
	public void setFlat(float flat) {
		this.flat = flat;
	}
	/**
	 * @return the aws
	 */
	public float getAws() {
		return aws;
	}
	/**
	 * @param aws the aws to set
	 */
	public void setAws(float aws) {
		this.aws = aws;
	}
	/**
	 * @return the awa
	 */
	public float getAwa() {
		return awa;
	}
	/**
	 * @param awa the awa to set
	 */
	public void setAwa(float awa) {
		this.awa = awa;
	}
	/**
	 * @return the lee
	 */
	public float getLee() {
		return lee;
	}
	/**
	 * @param lee the lee to set
	 */
	public void setLee(float lee) {
		this.lee = lee;
	}
	/**
	 * @return the sail
	 */
	public String getSail() {
		return sail;
	}
	/**
	 * @param sail the sail to set
	 */
	public void setSail(String sail) {
		this.sail = sail;
	}
	/**
	 * @return the isBest
	 */
	public boolean isBest() {
		return isBest;
	}
	/**
	 * @param isBest the isBest to set
	 */
	public void setBest(boolean isBest) {
		this.isBest = isBest;
	}
	/**
	 * @return the boatId
	 */
	public int getBoatId() {
		return boatId;
	}
	/**
	 * @param boatId the boatId to set
	 */
	public void setBoatId(int boatId) {
		this.boatId = boatId;
	}
	
}
