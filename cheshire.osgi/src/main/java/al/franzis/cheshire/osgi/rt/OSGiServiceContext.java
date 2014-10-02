package al.franzis.cheshire.osgi.rt;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.ComponentContext;

import al.franzis.cheshire.api.service.IServiceContext;

public class OSGiServiceContext implements IServiceContext {
	private final ComponentContext context;
	private final Map<String,String> properties = new HashMap<>();
	
	public OSGiServiceContext( ComponentContext context )
	{
		this.context = context;
		parseProps();
	}
	
	@Override
	public Map<String, String> getProperties() {
		return properties;
	}
	
	private void parseProps()
	{
		Dictionary dict = context.getProperties();
		Enumeration en = dict.keys();
		while ( en.hasMoreElements() )
		{
			Object _key = en.nextElement();
			String key = (String)_key;
			Object _value = dict.get(key);
			if ( _value instanceof String)
			{
				String value = (String)_value;
				properties.put(key, value);
			}
			else
			{
				// ignore non-string props
			}
		}
	}

}
