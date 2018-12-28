<head>
	<script id="rank-template" type="text/x-jsrender">
		<span class="{{>type}}">{{>rank}}</span>
	</script>
	<script id="a-template" type="text/x-jsrender">
		<a class="{{>className}}" href="{{>href}}" onclick="{{>event}}">{{>title}}</a>
	</script>
	<script id="a-no-template" type="text/x-jsrender">
		<a>{{>title}}</a>
	</script>
	<script id="span-no-template" type="text/x-jsrender">
		<span>{{>title}}</span>
	</script>
	<script id="kbTagTitle-template" type="text/x-jsrender">
		<div class="{{>className}}">{{>title}}</div>
	</script>
	<script id="text-center-template" type="text/x-jsrender">
		<div class="">{{>text}}</div>
	</script>
	<script id="text-template" type="text/x-jsrender">
		{{>text}}
	</script>
	<script id="text-right-template" type="text/x-jsrender">
		<div class="text-right">{{>text}}</div>
	</script>
	<script id="checkbox-template" type="text/x-jsrender">
		<input type="checkbox" class="qzsel" onclick="{{>event}}" name='{{>name}}' value="{{>val}}"/>
	</script>
	<script id="attention-template" type="text/x-jsrender">		
		<div class="wzb-percent-3 wzb-display-01"> 
		   <dl class="wzb-list-7 clearfix">
			   <dd>
					 <div class="wzb-user wzb-user68">
                          <img class="wzb-pic" src="{{>image}}"/>
						  <!--<a href="{{>a}}"><img class="wzb-pic" src="{{>image}}"/></a>
						  <p class="companyInfo"><lb:get key="label_cm.label_core_community_management_29"/></p>-->
						  <div class="cornerTL">&nbsp;</div>
						  <div class="cornerTR">&nbsp;</div>
						  <div class="cornerBL">&nbsp;</div>
						  <div class="cornerBR">&nbsp;</div>
					 </div>
			   </dd>
			  
			   <dt>
					<a class="wzb-link04" href="{{>a}}" title="">{{>usr_display_bil}}</a>
					<p>{{>usg_display_bil}}</p>
					<p>{{if ugr_display_bil != 'Unspecified'}}{{>ugr_display_bil}}{{/if}}</p>
			   </dt>
		  </dl>
		  
		  {{if sns_enabled && my_ent_id != usr_ent_id}}
			<!--	<em class="wzb-user-plus" id="attention_{{>usr_ent_id}}"><span class="wzb-link04"><i class="fa {{>attention_class}}"></i>{{>attention_status}}</span> | <a title='<lb:get key="global.button_cancel"/>' href="javascript:;" class="color-gray666" onclick="cancelAttention({{>usr_ent_id}}, {{>attention_id}})"><lb:get key="global.button_cancel"/></a></em> -->
			{{/if}}		  
		</div>
	</script>
	<script id="unattention-template" type="text/x-jsrender">
		<div class="wzb-percent-3 wzb-display-01"> 
		   <dl class="wzb-list-7 clearfix">
			   <dd>
					 <div class="wzb-user wzb-user68">
                            <img class="wzb-pic" src="{{>image}}"/>
						   <!--<a href="{{>a}}"><img class="wzb-pic" src="{{>image}}"/></a>
						  <p class="companyInfo"><lb:get key="label_cm.label_core_community_management_29"/></p>-->
						  <div class="cornerTL">&nbsp;</div>
						  <div class="cornerTR">&nbsp;</div>
						  <div class="cornerBL">&nbsp;</div>
						  <div class="cornerBR">&nbsp;</div>
					 </div>
			   </dd>
			  
			   <dt>
					<a class="wzb-link04" href="{{>a}}" title="">{{>usr_display_bil}}</a>
					<p>{{>usg_display_bil}}</p>
					<p>{{if ugr_display_bil != 'Unspecified'}}{{>ugr_display_bil}}{{/if}}</p>
			   </dt>
		  </dl>
		  
		  {{if sns_enabled && my_ent_id != usr_ent_id}}
			<!--   <em class="wzb-user-plus" id="attention_{{>usr_ent_id}}"><a href="javascript:;" class="wzb-link04" title='<lb:get key="label_cm.label_core_community_management_175"/>' onclick="addAttention({{>usr_ent_id}}, {{>attention_id}})"><i class="fa fa-plus"></i><lb:get key="label_cm.label_core_community_management_175"/></a></em>   -->
		 {{/if}}		  
		</div>
	</script>
	<script id="collect-template" type="text/x-jsrender">
		<dl class="wzb-list-20">
		      {{if imageInd == true}}
              <dd><a title="" href="{{>href}}"><img src="{{>image}}"></a></dd>
			  {{/if}}
			  
              <dt>
                  <div class="wzb-title-9">{{>title_desc}} : <a class="wzb-link04" href="{{>href}}" title="">{{>title}}</a> {{if isMeInd == true}}<i class="fa wzb-close-icon fa-times" onclick="cancelCollect({{>id}})"></i>{{/if}}</div>
                  <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999">{{>title_2_desc}} : </span>{{>title_2}}</div> <div class="offwidth"><span class="color-gray999">{{>time}}</span></div></div>
              </dt>
          </dl>
	</script>
	<script id="group-template" type="text/x-jsrender">	    
		<div class="wzb-percent-3 wzb-display-02"> 
			   <dl class="wzb-list-7 clearfix">
				   <dd>
						 <div class="wzb-user wzb-user68">
							  <a href="{{>href}}"><img class="wzb-pic" src="{{>image}}" /></a>
							  <p class="companyInfo"></p>
							  <div class="cornerTL">&nbsp;</div>
							  <div class="cornerTR">&nbsp;</div>
							  <div class="cornerBL">&nbsp;</div>
							  <div class="cornerBR">&nbsp;</div>
						 </div>
				   </dd>
				  
				   <dt>
						<a class="wzb-link04" href="{{>href}}" title="">{{>s_grp_title}}</a>
						<p><lb:get key="label_cm.label_core_community_management_177"/> {{>member_total}}</p>
						<p><lb:get key="label_cm.label_core_community_management_178"/> {{>message_total}}</p>
				   </dt>
			  </dl>
		</div>		
	</script>
 
    
	<script id="member-template" type="text/x-jsrender">		


		<div class="wzb-percent-3 wzb-transfer-sub wzb-display-01"> 
			   <dl class="wzb-list-7 clearfix">
				   <dd {{if add == false && isManager == true || isNormal != true}} {{/if}}>
						 <div class="wzb-user wzb-user68">
							  <a {{if isNormal == true}}href="{{>href}}"{{/if}}><img class="wzb-pic" src="{{>image}}"/></a>
							  <p class="companyInfo"><lb:get key="label_cm.label_core_community_management_29"/></p>
							  <div class="cornerTL">&nbsp;</div>
							  <div class="cornerTR">&nbsp;</div>
							  <div class="cornerBL">&nbsp;</div>
							  <div class="cornerBR">&nbsp;</div>
						 </div>
				   </dd>
				  
				   <dt>
				        {{if isNormal == true}}<a class="wzb-link04" href="{{>href}}">{{/if}}{{>usr_display_bil}}{{if isNormal == true}}</a>{{/if}}
						<p>{{>usg_display_bil}}</p>
						<p>{{if ugr_display_bil != 'Unspecified'}}{{>ugr_display_bil}}{{/if}}</p>
				   </dt>
			  </dl>
			  
			  {{if isManager == true && add == false && group_manager_id != usr_ent_id  && meId != usr_ent_id isNormal}}
				   <input type="button" id="group_assignment_{{>usr_ent_id}}" onclick="changeManager({{>usr_ent_id}})" class="btn wzb-btn-orange wzb-transfer" value='<lb:get key="label_cm.label_core_community_management_110"/>' name="frmSubmitBtn"> <a name="clearGroup" id="delete_member_{{>usr_ent_id}}" class="wzb-delete-01 wzb-transfer" onclick="deleteGroupMember({{>usr_ent_id}})" href="javascript:;"><i class="fa color-gray999 fa-times"></i></a>
			  {{/if}}
			  
			  {{if add == true}}
				<em class="wzb-user-plus" id="member_{{>usr_ent_id}}">
					<a name="addGroup" class="wzb-link04" href="javascript:;" title='<lb:get key="global.button_add"/>' onclick="addGroupMember({{>usr_ent_id}})">
						<i class="fa fa-plus"></i>
						<lb:get key="global.button_add"/>
					</a>
				</em>
			{{/if}}
		</div>
	</script>
	
	<script id="detele-user-button" type="text/x-jsrender">

		<div id="send_user_{{>usr_ent_id}}" class="wzb-choose-info" value="{{>usr_ent_id}}">
			<span class="wzb-choose-detail">{{>usr_display_bil}}</span>
			<a class="wzb-choose-area" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')">
				<i class="fa fa-remove"></i>
			</a>
		</div>
	
	</script>
	
</head>