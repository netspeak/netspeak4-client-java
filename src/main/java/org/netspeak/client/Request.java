package org.netspeak.client;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A class representing a Netspeak request able to hold parameters described by
 * Netspeak's <a href="http://www.netspeak.org/#developer">REST interface</a>.
 * For this reason this class actually derives from {@link HashMap}.
 * Furthermore, it provides the names of all REST interface parameters in form
 * of string constants that should be used as keys when setting up some request
 * instance. Note that there might be some keys, e.g. {@link #FORMAT}, which
 * are protected and cannot be set explicitly by the user.
 * However, they are used internally.
 */
public class Request extends HashMap<String, String> {

	private static final long serialVersionUID = -2846853760086017819L;

	// Note: All keys have to be lower case.

	// Protected parameters for internal usage only
	protected static final String FORMAT = "format";
	protected static final String PHIGH = "phigh";
	protected static final String PLOW = "plow";

	public static final String CORPUS = "corpus";
	public static final String MAXFREQ = "maxfreq";
	public static final String NMAX = "nmax";
	public static final String NMIN = "nmin";
	public static final String QUERY = "query";
	public static final String TOPK = "topk";

	protected static final Set<String> protectedParams = new HashSet<>(asList(FORMAT, PHIGH, PLOW));
}
