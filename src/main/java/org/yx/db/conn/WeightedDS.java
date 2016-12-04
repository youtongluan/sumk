package org.yx.db.conn;

import javax.sql.DataSource;

import org.yx.exception.SumkException;

class WeightedDS {

	public WeightedDS(DataSource ds) {
		super();
		this.ds = ds;
	}

	private DataSource ds;
	private int weight;

	public DataSource getDs() {
		return ds;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		if (weight < 1) {
			SumkException.throwException(2454335, "db weight must big than 0,but exact is " + weight);
		}
		this.weight = weight;
	}

}
