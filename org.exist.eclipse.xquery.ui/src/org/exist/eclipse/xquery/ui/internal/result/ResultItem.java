/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.result;

/**
 * Represents a result item.
 * 
 * @author Pascal Schmidiger
 */
public class ResultItem {
	private final int _index;
	private final String _content;
	private final String _group;
	private final int _uniqueNr;

	/**
	 * Construct a new unique item.
	 * 
	 * @param uniqueNr
	 *            number of query running, which is set by {@link ResultView}.
	 * @param group
	 *            the filename of the xquery.
	 * @param index
	 *            the position in the table.
	 * @param content
	 *            the content of the result.
	 */
	public ResultItem(int uniqueNr, String group, int index, String content) {
		_uniqueNr = uniqueNr;
		_group = group;
		_index = index;
		_content = content;
	}

	public final int getIndex() {
		return _index;
	}

	public final String getContent() {
		return _content;
	}

	public final String getGroup() {
		return _group;
	}

	public final int getUniqueNr() {
		return _uniqueNr;
	}

	@Override
	public String toString() {
		StringBuilder msg = new StringBuilder();
		msg.append("UniqueNr=").append(getUniqueNr());
		msg.append(", Group=").append(getGroup());
		msg.append(", Index=").append(getIndex());
		msg.append(", Content=").append(getContent());
		return msg.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_content == null) ? 0 : _content.hashCode());
		result = prime * result + ((_group == null) ? 0 : _group.hashCode());
		result = prime * result + _index;
		result = prime * result + _uniqueNr;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultItem other = (ResultItem) obj;
		if (_content == null) {
			if (other._content != null)
				return false;
		} else if (!_content.equals(other._content))
			return false;
		if (_group == null) {
			if (other._group != null)
				return false;
		} else if (!_group.equals(other._group))
			return false;
		if (_index != other._index)
			return false;
		if (_uniqueNr != other._uniqueNr)
			return false;
		return true;
	}

}
