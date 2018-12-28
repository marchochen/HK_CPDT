/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cwn.wizbank.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of {@link org.springframework.http.converter.HttpMessageConverter HttpMessageConverter} that
 * can read and write JSON using <a href="http://jackson.codehaus.org/">Jackson 2.x's</a> {@link ObjectMapper}.
 *
 * <p>This converter can be used to bind to typed beans, or untyped {@link java.util.HashMap HashMap} instances.
 *
 * <p>By default, this converter supports {@code application/json} and {@code application/*+json}.
 * This can be overridden by setting the {@link #setSupportedMediaTypes supportedMediaTypes} property.
 *
 * <p>The default constructor uses the default configuration provided by {@link Jackson2ObjectMapperBuilder}.
 *
 * <p>Compatible with Jackson 2.1 and higher.
 *
 * @author Arjen Poutsma
 * @author Keith Donald
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @author Sebastien Deleuze
 * @since 3.1.2
 */
public class WzbMappingJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

	private String jsonPrefix;


	/**
	 * Construct a new {@link MappingJackson2HttpMessageConverter} using default configuration
	 * provided by {@link Jackson2ObjectMapperBuilder}.
	 */
	public WzbMappingJackson2HttpMessageConverter() {
		this(Jackson2ObjectMapperBuilder.json().build());
	}

	/**
	 * Construct a new {@link MappingJackson2HttpMessageConverter} with a custom {@link ObjectMapper}.
	 * You can use {@link Jackson2ObjectMapperBuilder} to build it easily.
	 * @see Jackson2ObjectMapperBuilder#json()
	 */
	public WzbMappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
		super(objectMapper, new MediaType("application", "json", DEFAULT_CHARSET),
				new MediaType("application", "*+json", DEFAULT_CHARSET));
	}

	/**
	 * Specify a custom prefix to use for this view's JSON output.
	 * Default is none.
	 * @see #setPrefixJson
	 */
	public void setJsonPrefix(String jsonPrefix) {
		this.jsonPrefix = jsonPrefix;
	}

	/**
	 * Indicate whether the JSON output by this view should be prefixed with "{} &&". Default is false.
	 * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
	 * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
	 * This prefix does not affect the evaluation of JSON, but if JSON validation is performed on the
	 * string, the prefix would need to be ignored.
	 * @see #setJsonPrefix
	 */
	public void setPrefixJson(boolean prefixJson) {
		this.jsonPrefix = (prefixJson ? "{} && " : null);
	}


	@Override
	protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
		if (this.jsonPrefix != null) {
			generator.writeRaw(this.jsonPrefix);
		}
		String jsonpFunction =
				(object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
		if (jsonpFunction != null) {
			generator.writeRaw(jsonpFunction + "(");
		}
	}

	@Override
	protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
		String jsonpFunction =
				(object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
		if (jsonpFunction != null) {
			generator.writeRaw(");");
		}
	}
	
	
	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
		JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
		try {		
			writePrefix(generator, object);
			Class<?> serializationView = null;
			Object value = object;
			if (value instanceof MappingJacksonValue) {
				MappingJacksonValue container = (MappingJacksonValue) object;
				value = container.getValue();
				serializationView = container.getSerializationView();
				//过滤掉字符
				if(value != null && value instanceof String){
					value = adaptorString(value.toString());
				}
			}
			if (serializationView != null) {
				this.objectMapper.writerWithView(serializationView).writeValue(generator, value);
			}
			else {
				this.objectMapper.writeValue(generator, value);
			}
			writeSuffix(generator, object);
			generator.flush();

		}
		catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * 给a标签加上  _target="_blank"
	 * @param responseText
	 * @return
	 */
	public String adaptorString(String responseText){
		Pattern patt = Pattern.compile("(((&amp;|&)lt;)|<)a.*?(href=\\\\\"(?!javascript:;|javascript:void(0);|#+)([^(((&amp;|&)gt;)|>)]+)).*?(((&amp;|&)lt;|<)/a((&amp;|&)gt;)|>)");
		Matcher mat = patt.matcher(responseText);
		
		Pattern pathref = Pattern.compile("(?<=href=(\\\\\"|\"))(?!javascript:;|javascript:void(0);|#+)([^\\\\\"|\"]+)");
		Matcher mathref = null;
		
		String aText;
		String preffix = " onclick=\\\\\"openOtherSiteUrl('"; 
		String suffix = "')\\\\\"";
		while(mat.find()) {
			aText = mat.group();
			//int index = -1;
			/*			
			index = responseText.indexOf(href);
			if(index > -1) {
				responseText = responseText.substring(0, index + 10) + addText + responseText.substring(index + 10);
			}*/
			mathref = pathref.matcher(aText);	
			String href = "";
			if(mathref.find()){
				href = mathref.group();
				if(href !=null && href.indexOf("javascript:")>=0){//链接中自己处理了
					continue;
				}
			}
			String hrefTemp = aText.replaceAll("(((&amp;|&)lt;)|<)a", "&amp;lt;a"+ preffix + href + suffix).replaceAll(pathref.pattern(), "javascript:;").replaceAll("target=\\\\\"_blank\\\\\"", "");
			responseText = responseText.replace(aText, hrefTemp);
		}
		return responseText;
	}

}
