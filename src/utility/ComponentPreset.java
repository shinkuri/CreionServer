package utility;

import java.util.HashSet;

public class ComponentPreset {
	
	private final String componentName;
	private final HashSet<ColumnDataObject<?>> columnData = new HashSet<>();
	
	public ComponentPreset(String componentName) {
		this.componentName = componentName;
	}
	
	public <T> void addColumnData(String columnName, T data) {
		columnData.add(new ColumnDataObject<T>(columnName, data));
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Component Name: " + componentName + "\n");
		for(ColumnDataObject<?> column : columnData) {
			sb.append("Column Name: " + column.getColumnName() + "\n");
			sb.append("Data: " + column.getData().toString() + "\n");
		}
		
		return sb.toString();
	}
	
	private class ColumnDataObject<T> {
		
		private final String columnName;
		private final T data;
		
		public ColumnDataObject(String columnName, T data){
			this.columnName = columnName;
			this.data = data;
		}
		
		public String getColumnName() {
			return columnName;
		}
		
		/**
		 * Retrieve column value as instance of the type it 
		 * originally had. 
		 */
		public T getData() {
			return data;
		}
	}
}
