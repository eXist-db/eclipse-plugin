/**
 * 
 */
package org.exist.eclipse.auto.internal.control;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.exist.eclipse.auto.internal.exception.AutoException;
import org.exist.eclipse.auto.internal.model.AutoModelConverter;
import org.exist.eclipse.auto.internal.model.IAutoModel;

/**
 * The AutoContentProvider enables the access to the automation data.
 * 
 * @author Markus Tanner
 */
public class AutoContentProvider implements IStructuredContentProvider {

	private String _inputData;
	private IAutoModel _model = null;

	/**
	 * AutoContentProvider Constructor
	 */
	public AutoContentProvider(String inputData) {
		_inputData = inputData;
	}

	@Override
	public Object[] getElements(Object inputElement) {

		try {
			// get the data from the model - via text editor
			return getModel().getContents();
		} catch (AutoException e) {
			return null;
		}
	}

	/**
	 * Gets the model if it's not instantiated yet.
	 * 
	 * @return The actual IAutoModel
	 */
	public IAutoModel getModel() throws AutoException {
		if (_model == null) {
			try {
				_model = AutoModelConverter.getAutoModel(_inputData);
			} catch (AutoException e) {
				throw new AutoException(e);
			}
		}
		return _model;
	}

	@Override
	public void dispose() {
		_model = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing here
	}

}
