package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Pattern;

import nomm.Template;

public class TemplateParser {
	
	private static final Pattern isNumber = Pattern.compile("-?\\d+(\\.\\d+)?");
	private static final Pattern hasDecimals = Pattern.compile("-?\\d+(\\.\\d+)");
	
	public static HashSet<Template> parse(String filePath){
		
		Logger.INFO.log("Loading template definition file from: " + filePath);
		final StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new File(filePath).getReader()){
			String line;
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (FileNotFoundException e) {
			Logger.ERROR.log("Template file not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Logger.INFO.log("Parsing template definitions");
		
		final String[] templates = sb.toString().split(",");
		Logger.INFO.log("Found " + templates.length + " templates");
		
		for(int i = 0; i < templates.length; i++) {
			// Remove all new-line characters for any OS (\r or \n)
			// Remove all tabs and spaces (\t and \s)
			// Remove curly brackets around template blocks ([{}])
			templates[i] = templates[i].replaceAll("\\r|\\n|\\t|\\s|[{}]", "");
		}
		
		final HashSet<Template> templatesAggregator = new HashSet<>();
		
		for(String template : templates) {
			final String[] elements = template.split(";");
			
			String templateName = "";
			HashSet<ComponentPreset> componentPresets = new HashSet<>();
			// flag to check if we're still in the same component definition block
			ComponentPreset current = null;
			for(String element : elements) {
				
				if(element.startsWith("template:")) {
					if(templateName.equals("")) {
						templateName = element.substring("template:".length());
						Logger.INFO.log("Found template specifier: " + templateName);
					} else {
						Logger.INFO.log("Skipping duplicate template specifier. Error will be logged.");
						Logger.ERROR.log("Found duplicate template specifier! Please corrrect the file.");
					}
				} else if(element.startsWith("component:")) {
					if(current == null) {
						final String componentName = element.substring("component:".length());
						current = new ComponentPreset(componentName);
						Logger.INFO.log("Initializing new component data for: " + componentName);
					} else {
						componentPresets.add(current);
						Logger.INFO.log("Ended component initialization for: " + current.getComponentName());
						current = null;
						
						final String componentName = element.substring("component:".length());
						current = new ComponentPreset(componentName);
						Logger.INFO.log("Initializing new component data for: " + componentName);
					}
				} else if(element.startsWith("column:")) {
					if(current != null) {
						final String[] pair = element.substring("column:".length()).split("=");
						Logger.INFO.log("Found data for column: " + pair[0]);
						
						
						
						if(isNumber.matcher(pair[1]).matches()) {
							if(hasDecimals.matcher(pair[1]).matches()) {
								current.addColumnData(pair[0], Float.parseFloat(pair[1]));
								Logger.INFO.log("Added value as Float");
							} else {
								current.addColumnData(pair[0], Integer.parseInt(pair[1]));
								Logger.INFO.log("Added value as Integer");
							}
						} else {
							current.addColumnData(pair[0], new String(pair[1]));
							Logger.INFO.log("Added value as String");
						}
					}
				}
			}
			
			if(current != null) {
				componentPresets.add(current);
				Logger.INFO.log("Ended component initialization for: " + current.getComponentName());
			}
			
			if(!templateName.equals("") && componentPresets.size() > 0) {
				templatesAggregator.add(new Template(templateName, componentPresets));
				Logger.INFO.log("Finished parsing template '" + templateName + "' with " + componentPresets.size() + " components");
			}
		}
		
		Logger.INFO.log("Parsed " + templatesAggregator.size() + " as proper templates");
		return templatesAggregator;
	}
}
