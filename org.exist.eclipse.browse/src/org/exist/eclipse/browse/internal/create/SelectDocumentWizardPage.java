package org.exist.eclipse.browse.internal.create;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * Select the document type, which you want create.<br />
 * The different type of documents gets from the extension point
 * "org.exist.eclipse.browse.createdocument".
 */
public class SelectDocumentWizardPage extends WizardPage {
	private Map<String, IConfigurationElement> _configurations;

	public SelectDocumentWizardPage() {
		super("selectdocumentwizardpage");
		setDescription("Select the document type");
		_configurations = new HashMap<>();
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;

		Button button = null;
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint exPoint = reg.getExtensionPoint(BrowsePlugin.getId(), "createdocument");
		IExtension[] documents = exPoint.getExtensions();
		for (IExtension extension : documents) {
			IConfigurationElement[] configurations = extension.getConfigurationElements();
			for (IConfigurationElement element : configurations) {
				String name = element.getAttribute("name");
				_configurations.put(name, element);
				button = new Button(container, SWT.RADIO);
				button.setText(name);
				button.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}

					@Override
					public void widgetSelected(SelectionEvent e) {
						EnterDocumentWizardPage nextPage = (EnterDocumentWizardPage) getNextPage();
						IConfigurationElement configurationElement = _configurations
								.get(Button.class.cast(e.getSource()).getText());
						nextPage.setDocumentProvider(configurationElement);
						setErrorMessage(null);
						setPageComplete(true);
					}
				});
			}
		}

		setControl(container);
		setErrorMessage("Select a type");
		setPageComplete(false);
	}
}
