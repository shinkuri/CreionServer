package utility;

import java.util.HashSet;

public class Template {
	
	private final String templateName;
	private final HashSet<ComponentPreset> componentPresets;
	
	public Template(String templateName, HashSet<ComponentPreset> componentPresets) {
		this.templateName = templateName;
		this.componentPresets = componentPresets;
	}

	public String getTemplateName() {
		return templateName;
	}

	public HashSet<ComponentPreset> getComponentPresets() {
		return componentPresets;
	}
	
}
