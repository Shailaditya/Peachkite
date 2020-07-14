package com.peachkite.util;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONUtils {
	
	public static ArrayList<Object> convertJsonToArrayList(String json)
	{
		ArrayList<Object> list = new ArrayList<Object>();
		ObjectMapper mapper = new ObjectMapper();

		try {

			//convert JSON string to Map
			list = mapper.readValue(json, 
					new TypeReference<ArrayList<Object>>(){});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String convertObjectToJSON(Object object){
		ObjectMapper mapper = new ObjectMapper(new Factory());
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

		ObjectWriter ow = mapper.writer();
		try {
			return ow.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			return e.toString();
		}
	}

	public static Map<String, Object> convertJsonToMap(String json)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		ObjectMapper mapper = new ObjectMapper();

		try {

			//convert JSON string to Map
			map = mapper.readValue(json, 
					new TypeReference<HashMap<String,Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static <T> T convertJSONToObject(final TypeReference<T> type,final String jsonPacket) throws IOException{
	   T data = null;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.readValue(jsonPacket, type);
	} 
	
	public static <T> T convertJSONToObject(Class<T> clazz,String json) throws IOException{
		return new ObjectMapper().readValue(json, clazz);
	}

	private static class OneJsonObjPerLinePrinter extends DefaultPrettyPrinter 
	{	        
		private static final long serialVersionUID = 2494199497001233655L;

		@Override
		public void writeStartObject(JsonGenerator jg)
				throws IOException, JsonGenerationException
		{
			if(_nesting<1)
				_objectIndenter.writeIndentation(jg, _nesting);
			jg.writeRaw('{');
			if (!_objectIndenter.isInline()) {
				++_nesting;
			}
		}

		@Override
		public void beforeObjectEntries(JsonGenerator jg)
				throws IOException, JsonGenerationException
		{
			if(_nesting<1)
				_objectIndenter.writeIndentation(jg, _nesting);
		}

		@Override
		public void writeObjectEntrySeparator(JsonGenerator jg)
				throws IOException, JsonGenerationException
		{
			jg.writeRaw(',');
			if(_nesting<1)
				_objectIndenter.writeIndentation(jg, _nesting);
		}

		@Override
		public void writeEndObject(JsonGenerator jg, int nrOfEntries)
				throws IOException, JsonGenerationException
		{
			if (!_objectIndenter.isInline()) {
				--_nesting;
			}
			jg.writeRaw(' ');
			jg.writeRaw('}');
		}
	}

	private static class Factory extends JsonFactory {
		private static final long serialVersionUID = 1989338069010089887L;

		@Override
		protected JsonGenerator _createGenerator(Writer out, IOContext ctxt) throws IOException {
			return super._createGenerator(out, ctxt).setPrettyPrinter(new OneJsonObjPerLinePrinter());
		}
	}
}
