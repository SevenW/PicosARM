package com.sevenwatt.picos.wizards;

import java.util.List;

import org.eclipse.cdt.ui.wizards.CDTMainWizardPage;
import org.eclipse.cdt.ui.wizards.EntryDescriptor;

public class PicosMainWizardPage  extends CDTMainWizardPage {
	/**
	 * Creates a new project creation wizard page.
	 *
	 * @param pageName the name of this page
	 */
	public PicosMainWizardPage(String pageName) {
		super(pageName);
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List filterItems(List items) {
		//first pass: filter on templates
		for (int i = items.size()-1; i >= 0; --i) {
			EntryDescriptor ed = (EntryDescriptor) items.get(i);
			if (!ed.isCategory() && !ed.getId().contains(Messages.PicosMainWizardPage_templatePathKeyword)) {
				items.remove(i);
			}
		}
		//second pass: remove empty categories
		EntryDescriptor makefileCat = null;
		for (int i = items.size()-1; i >= 0; --i) {
			EntryDescriptor ed = (EntryDescriptor) items.get(i);
			if (ed.isCategory()) {
				if (ed.getId()=="org.eclipse.cdt.build.makefile.projectType") //$NON-NLS-1$
					makefileCat = ed;
				boolean hasTemplate = false;
				String id = ed.getId();
				for (int j = items.size()-1; j>= 0; --j) {
					EntryDescriptor leaf = (EntryDescriptor) items.get(j);
					if (!leaf.isCategory() && leaf.getParentId().equals(id)) {
						hasTemplate = true;
						break;
					}
				}
				if (!hasTemplate) {
					items.remove(i);
				}
			}
		}
		//in case list is empty, add empty makefile category, to prevent exceptions
		if (items.size()==0 && makefileCat != null)
			items.add(makefileCat);
		return items;
	}
}
