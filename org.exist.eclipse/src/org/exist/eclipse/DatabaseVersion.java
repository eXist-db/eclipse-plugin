package org.exist.eclipse;

import static java.lang.Integer.*;
import java.util.StringJoiner;

/**
 * @author Patrick Reinhart
 */
public final class DatabaseVersion implements Comparable<DatabaseVersion> {
	private final int major;
	private final int minor;
	private final int hotfix;

	public static DatabaseVersion valueOf(String versionString) {
		String[] values = versionString.split("[^0-9]+", 3);
		return new DatabaseVersion(parseInt(values[0]), parseInt(values[1]), parseInt(values[2]));
	}

	public static DatabaseVersion create(int major, int minor, int hotfix) {
		return new DatabaseVersion(major, minor, hotfix);
	}

	private DatabaseVersion(int major, int minor, int hotfix) {
		this.major = major;
		this.minor = minor;
		this.hotfix = hotfix;
	}

	/**
	 * @return the major version number
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * @return the minor version number
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * @return the hotfix version number
	 */
	public int getHotfix() {
		return hotfix;
	}

	@Override
	public int compareTo(DatabaseVersion other) {
		int rc = compare(major, other.major);
		if (rc == 0) {
			rc = compare(minor, other.minor);
			if (rc == 0) {
				rc = compare(hotfix, other.hotfix);
			}
		}
		return rc;
	}

	@Override
	public int hashCode() {
		return major;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof DatabaseVersion)) {
			return false;
		}
		return compareTo((DatabaseVersion) obj) == 0;
	}

	@Override
	public String toString() {
		return new StringJoiner(".").add(Integer.toString(major)).add(Integer.toString(minor))
				.add(Integer.toString(hotfix)).toString();
	}
}
