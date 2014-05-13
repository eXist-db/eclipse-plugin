/**
 * 
 */
package org.exist.eclipse.auto.internal.result.control;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.exist.eclipse.auto.internal.exception.AutoException;
import org.exist.eclipse.auto.internal.result.model.IResultModel;
import org.exist.eclipse.auto.internal.result.model.ResultModelConverter;

/**
 * Provides the data for the table in the automation result.
 * 
 * @author Markus Tanner
 */
public class ResultContentProvider implements IStructuredContentProvider {

	private String _inputData;
	private IResultModel _model;

	/**
	 * ResultContentProvider Constructor
	 * 
	 * @param inputData
	 */
	public ResultContentProvider(String inputData) {
		_inputData = inputData;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		try {
			// get the data from the model - via text editor
			return getModel().getContents();
		} catch (AutoException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the IResultModel if it's not instantiated yet.
	 * 
	 * @return the result model object
	 * @throws AutoException
	 */
	public IResultModel getModel() throws AutoException {
		if (_model == null) {
			_model = ResultModelConverter.getResultModel(_inputData);
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
