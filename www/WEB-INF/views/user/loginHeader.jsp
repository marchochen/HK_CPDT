<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div style="background:#ffffff;width:100%;z-index: 89;position: relative;">
	<div id="top" class="xyd-top">
	     <a class="xyd-brand" href="http://www.cyberwisdom.net" target="blank">
	        <%--   <img src="${ctx}/poster/${site}/<c:if test="${lang=='zh-hk'}" >${sitePoster.sp_logo_file_hk}</c:if><c:if test="${lang=='zh-cn'}" >${sitePoster.sp_logo_file_cn}</c:if><c:if test="${lang=='en-us'}" >${sitePoster.sp_logo_file_us}</c:if>" alt="wizbank" />  --%>
	        <img src="${ctx}/poster/cw/wizbang.png"/>
	     </a>
	  
	     <ul class="xyd-kit">
	         <li class="xyd-kit01"><a href="${cxt}/app/user/userRegister/$?lang=${lang}" ><lb:get key='login_new_user_register'/></a></li>
	         
	         <li class="xyd-kit02 xyd-toggle">
	             <lb:get key="login_contact_us"/>
	             <div class="xyd-erwm xyd-our">
	                  <div class="xyd-our-bg"></div>
	                  <div class="xyd-our-desc">
	                      <h2 class="xyd-toggle-title"><lb:get key="login_contact_us"/></h2>
	                      <dl class="xyd-our-info">
	                            <dt class="login-adress-01"><lb:get key="contact_us_city_hk"/></dt>
	                            <dd>
	                                 <lb:get key="contact_us_adress_hk"/>
	                            </dd>
	                      </dl>
	                      	
	                      <dl class="xyd-our-info">
	                            <dt class="login-adress-02"><lb:get key="contact_us_city_bj"/></dt>
	                            <dd>
	                                 <lb:get key="contact_us_adress_bj"/>
	                            </dd>
	                      </dl>	
	                      
	                      <dl class="xyd-our-info">
	                            <dt class="login-adress-03"><lb:get key="contact_us_city_gz"/></dt>
	                            <dd>
	                                 <lb:get key="contact_us_adress_gz"/>
	                            </dd>
	                      </dl>	
	                      
	                      <dl class="xyd-our-info">
	                            <dt class="login-adress-04"><lb:get key="contact_us_city_sz"/></dt>
	                            <dd>
	                                 <lb:get key="contact_us_adress_sz"/>
	                            </dd>      
	                      </dl>	
	                      
	                      <dl class="xyd-our-info">
	                            <dt class="login-adress-05"><lb:get key="contact_us_city_sh"/></dt>
	                            <dd>
	                                 <lb:get key="contact_us_adress_sh"/>
	                            </dd>
	                      </dl>	
	                 </div>
	             </div>
	         </li>
	         
	         <li class="xyd-toggle">
	             <lb:get key="login_attention_us"/>
	             <div class="xyd-erwm xyd-code">
	                  <div class="xyd-our-bg"></div>
	                  
	                  <div class="xyd-code-desc clearfix">
	                       <div class="xyd-code-left"><lb:get key="contact_us_weixin_cn"/></div> 
	                       <div class="xyd-code-center"><lb:get key="contact_us_weixin_hk"/></div>
	                       <div class="xyd-code-right"><lb:get key="contact_us_weixin_cp"/></div>
	                  </div>
	             </div>
	         </li>
	         <li class="xyd-kit04 xyd-toggle" style="margin-right: 0px;">
	             <lb:get key="login_about_cyberwisdom"/>
	             <div class="xyd-erwm xyd-about">
	                  <div class="xyd-our-bg"></div>
	                  <div class="xyd-about-desc" style="height: 535px">
	                          <h2 class="xyd-toggle-title"><lb:get key="login_about_cyberwisdom"/></h2>
	                          <p><lb:get key="about_cyberwisdom_1"/></p>
	                         
	                          <dl class="xyd-about-info" style='margin-top:10px'>
	                                <dt><img width="186" height="134" src="${ctx}/static/images/lg_our.jpg" alt="" /></dt>
	                                <dd>
	                                     <lb:get key="about_cyberwisdom_2"/>
	                                </dd>
	                          </dl>  
	                          <br>
	                           <p><img width="475" height="60" src="${ctx}/static/images/lg_office.jpg" alt="" /></p> 
	                          <div class="xyd-erwm-link" >
	                               <div class="pull-left">
	                                    <p><lb:get key="login_web_hk"/></p>
	                                    <p><a title='<lb:get key="login_web_hk"/>' target="_blank" href="http://www.cyberwisdom.net" class="erwm_color">http://www.cyberwisdom.net</a></p>
	                               </div>
	                               
	                               <div class="pull-right">
	                                    <p>大陆官网：</p>
	                                    <p><a title='<lb:get key="login_web_ch"/>' target="_blank" href="http://www.cyberwisdom.net.cn" class="erwm_color">http://www.cyberwisdom.net.cn</a></p>
	                               </div>
	                          </div>
	                  </div>
	             </div>
	         </li>
	         <%-- <li class="xyd-kit05 xyd-toggle">
	             <lb:get key="login_app_download"/>
	             <div class="xyd-erwm xyd-app">
	                  <div class="xyd-our-bg"></div>
	                  
	                  <div class="xyd-app-desc">
	                       <h2 class="xyd-toggle-title"><lb:get key="app_title"/><!-- APP客户端下载 --></h2>
	                       
							<dl class="xyd-app-list clearfix">
							   <dd style="height:240px;"><img src="${cxt}/static/images/lg_all.png" alt="" /></dd>
							   <dt style="margin:0 0 14px 0;">
									<strong class="xyd-app-version"><lb:get key="app_android"/><!-- Android版下载 --></strong>
									<a class="xyd-app-down" href="${cxt}/download/wizmobile3.6.apk" title='<lb:get key="app_android"/>'><lb:get key="app_download"/><!-- 马上下载 --></a>
									<p><lb:get key="app_android_version"/><!-- 版本V3.6 大小	17.6M --></p>
									<p><lb:get key="app_android_desc"/><!-- 支持Android 4.3及以上版本 --></p>
							   </dt>
							   <dt>
									<strong class="xyd-app-version"><lb:get key="app_ios"/><!-- iPhone/ipad版下载 --></strong>
									<a class="xyd-app-down" href="${cxt}/download/wizmobile3.6.ipa" title='<lb:get key="app_ios"/>'><lb:get key="app_download"/><!-- 马上下载 --></a>
									<p><lb:get key="app_ios_version"/><!-- 版本V3.6 大小	34.5M --></p>
									<p><lb:get key="app_ios_desc"/><!-- 支持IOS 7.0及以上版本 --></p>
							   </dt>
							</dl>
	                  </div>
	             </div>
	         </li> --%>
	     </ul>     
	</div> <!-- xyd-top End-->
</div>