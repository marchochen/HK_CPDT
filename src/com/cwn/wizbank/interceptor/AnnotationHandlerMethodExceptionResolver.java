/**
 * 
 */
package com.cwn.wizbank.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ProtocolException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.cw.wizbank.Application;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cwn.wizbank.entity.APIToken;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.exception.EncryptException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.exception.MultipleLoginException;
import com.cwn.wizbank.exception.SessionTimeOutException;
import com.cwn.wizbank.exception.UploadException;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.RequestStatus;

/**
 * 需要与AnnotationMethodHandlerAdapter使用同一个messageConverters<br>
 * Controller中需要有专门处理异常的方法。
 * */
public class AnnotationHandlerMethodExceptionResolver extends ExceptionHandlerExceptionResolver {
	
	private String defaultErrorView;
	
	private Properties exceptionMappings;
	
	public Properties getExceptionMappings() {
		return exceptionMappings;
	}

	public void setExceptionMappings(Properties exceptionMappings) {
		this.exceptionMappings = exceptionMappings;
	}
	
	public String getDefaultErrorView() {
		return defaultErrorView;
	}

	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
		
		if (handlerMethod == null) {
			return null;
		}
		
		Method method = handlerMethod.getMethod();

		if (method == null) {
			return null;
		}
		
		ModelAndView returnValue = super.doResolveHandlerMethodException(request, response, handlerMethod, exception);

		ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
		if (responseBodyAnn != null) {
			try {
					if(exception instanceof MessageException && "ERROR_TOKEN_INVALID".equals(exception.getMessage())){
					}else{
						exception.printStackTrace();
					}
				String callback = request.getParameter("callback");
				String developer = request.getParameter("developer");
				String uploadType = request.getParameter("uploadType");
				if(APIToken.API_DEVELOPER_API.equals(developer)) {
					String code = "";
					if(exception instanceof AuthorityException) {
						code = "403";
					} else if(exception instanceof MessageException && "ERROR_TOKEN_INVALID".equals(exception.getMessage())) {
						code = "-1";
					} else {
						code = "500";
					}
					returnValue = new ModelAndView();
					returnValue.addObject(RequestStatus.STATUS, RequestStatus.ERROR);
					returnValue.addObject(RequestStatus.MSG, exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage());
					returnValue.addObject(RequestStatus.CODE, code);
					return handleResponseBody(returnValue, request, response, callback);
				} else {
					if(exception instanceof MessageException || exception instanceof DataNotFoundException){
						returnValue = new ModelAndView();
						return handleResponseBody(handleExceptionLevel(exception, returnValue), request, response, callback);
					}else {
						//由于手机端是jsonp， 所以手机端不存在返回500的情况，只存在404的情况
						if(StringUtils.isEmpty(callback)){						
							if(exception instanceof ProtocolException){//解决weblogic”Didn't meet stated Content-Length, wrote: '24' bytes instead of stated: '12' bytes.“
								exception.printStackTrace();
								return null;
							}
							
							if(exception instanceof SessionTimeOutException || exception instanceof UploadException
									|| exception instanceof EncryptException || exception instanceof AuthorityException){
								ModelAndView mv = new ModelAndView(exceptionMappings.getProperty(exception.getClass().getSimpleName()));
								if(exception instanceof UploadException  ){
									if(null!=uploadType && uploadType.equals("ajax")){
										loginProfile prof = (loginProfile)request.getSession().getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
										String lang = "en-us";
										if (prof != null) {
											lang = prof.cur_lan;
										}
										StringBuilder sb = new StringBuilder();
										sb.append(LabelContent.get(lang, exception.getMessage()));
										sb.append(Application.UPLOAD_FORBIDDEN);
										mv.addObject("errorMsg",sb.toString());
										mv.addObject("status", "fail");
										return handleResponseBody(mv, request, response, callback);
									}else{
										mv.addObject("exception", (UploadException)exception);
										mv.addObject("forbidden",Application.UPLOAD_FORBIDDEN);
									}
								}else if(exception instanceof EncryptException){
									mv.addObject("exception", (EncryptException)exception);
								}else {
									return handleExceptionLevel(exception, mv);
								}
								
								return mv;
							} else{
								response.sendError(500, exception.getCause() == null ? exception.getClass().getName() : exception.getCause().getMessage());
								return handleExceptionLevel(exception, returnValue);
							}
						}
						return handleResponseBody(handleExceptionLevel(exception, returnValue), request, response, callback);
					}
				}
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
				return null;
			}
		} else {
			
			if(exception instanceof UploadException){
				ModelAndView mv = new ModelAndView(exceptionMappings.getProperty(exception.getClass().getSimpleName()));
				mv.addObject("exception", (UploadException)exception);
				mv.addObject("forbidden",Application.UPLOAD_FORBIDDEN);
				return mv;
			}else if(exception instanceof MultipleLoginException){
				ModelAndView mv = new ModelAndView(exceptionMappings.getProperty(exception.getClass().getSimpleName()));
				mv.addObject("hideHeaderInd",true);
				mv.addObject("sitemesh_parameter","excludes");
				return mv;
			}
			
			if(exceptionMappings.containsKey(exception.getClass().getSimpleName())){
				String mappingUrl = exceptionMappings.getProperty(exception.getClass().getSimpleName());
				if(!StringUtils.isEmpty(mappingUrl)){
					returnValue = new ModelAndView(mappingUrl);
					returnValue.addObject(RequestStatus.EXCEPTION, exception);
					return handleExceptionLevel(exception, returnValue);
				}
			}
		}
		
		if(returnValue != null && returnValue.getViewName() == null){
			returnValue.addObject(RequestStatus.EXCEPTION, exception);
			returnValue.setViewName(defaultErrorView);
		}
		
		return returnValue;
		
	}
	
	/**
	 * 区分错误等级
	 * @param exception
	 * @param returnValue
	 * @return
	 */
	private ModelAndView handleExceptionLevel(Exception exception, ModelAndView returnValue){
		if(exception instanceof MessageException){
			return getValue(returnValue, exception, 0);
		} else if(exception instanceof AuthorityException){
			return getValue(returnValue, exception, -1);
		} else if(exception instanceof DataNotFoundException){
			return getValue(returnValue, exception, -1);
		} else {
			return getValue(returnValue, exception, -1);
		}
	}
	
	/**
	 * 
	 * @param exception
	 * @param level  消息等级，-1，表示系统异常，0，消息提示
	 * @return
	 */
	private ModelAndView getValue(ModelAndView value, Exception exception, int level){
		if(value == null){
			value = new ModelAndView();
		}
		//value.addObject(RequestStatus.EXCEPTION, exception);
		value.addObject(RequestStatus.STATUS, RequestStatus.ERROR);
		value.addObject(RequestStatus.MSG, exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage());
		value.addObject(RequestStatus.MSG_LEVEL, level);
		return value;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	private ModelAndView handleResponseBody(ModelAndView returnValue, HttpServletRequest request, HttpServletResponse response, String callback) throws ServletException, IOException {
		Object value = returnValue.getModelMap();
		HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
		if (acceptedMediaTypes.isEmpty()) {
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		}
		MediaType.sortByQualityValue(acceptedMediaTypes);
		//如果是jsonp请求
		if(!StringUtils.isEmpty(callback)){
			MappingJacksonValue mjv = new MappingJacksonValue(value);
			mjv.setJsonpFunction(callback);
			value = mjv;
		}
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
		Class<?> returnValueType = value.getClass();
		List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
		if (messageConverters != null) {
			for (MediaType acceptedMediaType : acceptedMediaTypes) {
				for (HttpMessageConverter messageConverter : messageConverters) {
					if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
						messageConverter.write(value, acceptedMediaType, outputMessage);
						return new ModelAndView();
					}
				}
			}
		}
		if (logger.isWarnEnabled()) {
			logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + acceptedMediaTypes);
		}
		return null;
	}


}
