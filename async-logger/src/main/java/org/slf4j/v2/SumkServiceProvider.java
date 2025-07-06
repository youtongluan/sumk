package org.slf4j.v2;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;
import org.yx.log.impl.SumkLoggerFactory;

public class SumkServiceProvider implements SLF4JServiceProvider {

	private ILoggerFactory loggerFactory;

	private IMarkerFactory markerFactory;

	private MDCAdapter mdcAdapter;

	@Override
	public ILoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

	@Override
	public IMarkerFactory getMarkerFactory() {
		return markerFactory;
	}

	@Override
	public MDCAdapter getMDCAdapter() {
		return mdcAdapter;
	}

	@Override
	public String getRequestedApiVersion() {
		return "2.0.1";
	}

	@Override
	public void initialize() {
		this.loggerFactory = new SumkLoggerFactory();
		this.markerFactory = new BasicMarkerFactory();
		this.mdcAdapter = new BasicMDCAdapter();
	}

}
