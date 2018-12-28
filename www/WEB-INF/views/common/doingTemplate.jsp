<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>

<script id="doingTemplate" type="text/x-jsrender">
		<div class="wzb-trend clearfix" did="{{>s_doi_id}}">
			<div class="wzb-user wzb-user68">
		      	{{if s_doi_target_type == '1' && operator}}
				<a class="trendpic" href="${ctx}/app/personal/{{>operator.usr_ent_id}}"> <img class="wzb-pic" src="${ctx}{{>operator.usr_photo}}"/></a>
				{{else}}
				<a class="trendpic" href="${ctx}/app/personal/{{>user.usr_ent_id}}"> <img class="wzb-pic" src="${ctx}{{>user.usr_photo}}"/></a>
				{{/if}}
				<p class="companyInfo"><lb:get key="label_cm.label_core_community_management_29"/></p>
				<div class="cornerTL" style="width: 60px; height: 60px;">&nbsp;</div>
          		<div class="cornerTR" style="width: 60px; height: 60px;">&nbsp;</div>
          		<div class="cornerBL" style="width: 60px; height: 60px;">&nbsp;</div>
          		<div class="cornerBR" style="width: 60px; height: 60px;">&nbsp;</div>
			</div>
		 
			<div class="wzb-trend-content">
		      	{{if s_doi_target_type == '1'}}
					<div class="color-gray999">
						<a href="${ctx }/app/personal/{{if operator}}{{>operator.usr_ent_id}}{{else}}{{>user.usr_ent_id}}{{/if}}" class="wzb-link04" title="">
							{{if operator}}{{>operator.usr_display_bil}}{{else}}{{>user.usr_display_bil}}{{/if}}
						</a> 
						{{>s_doi_create_datetime}} {{>s_doi_act_str}}：
					</div>
				{{else}}
					<div class="color-gray999">
						<a href="${ctx }/app/personal/{{>user.usr_ent_id}}" class="wzb-link04" title="">{{>user.usr_display_bil}}</a> 
						{{>s_doi_create_datetime}} {{>s_doi_act_str}}：
					</div>
				{{/if}}
		      
				<p class="mt10 f14 title wbdu">{{>s_doi_title}}</p>
				{{if snsShare}}<p>{{>snsShare.s_sha_title}}</p>{{/if}}
				<p>
		      		{{if s_doi_target_type != '1'}}
						{{if s_doi_act != 'like' }}
							{{if s_doi_act != 'group_open' }}
							{{if s_doi_act != 'group_create' }}
							{{if s_doi_act != 'group_dissmiss' }}
							{{if s_doi_act != 'group_app' }}
							{{if s_doi_act != 'question_add' }}
							{{if s_doi_act != 'answer_add' }}
							{{if s_doi_act != 'completed_cos' }}
							{{if s_doi_act != 'enroll_cos' }}
								<a class="wzb-link03 margin-right15" id="doi_{{>s_doi_id}}" href="javascript:;" title="">
									<i class="fa skin-color mr5 fa-thumbs-o-up"></i>
									<lb:get key="sns_like"/>(<span>0</span>)
								</a>
					
								<a class="wzb-link03 margin-right15 review" href="javascript:void(0);">
									<i class="fa skin-color mr5 fa-comment"></i>
									<lb:get key="sns_comment"/>(<span>0</span>)
								</a>
							{{/if}}
							{{/if}}
							{{/if}}
							{{/if}}
							{{/if}}
							{{/if}}
							{{/if}}
							{{/if}}
						{{/if}}
					{{/if}}

					{{if s_doi_uid == '${prof.usr_ent_id}'}}
						<a class="wzb-link03 deleteDoing" data="{{>s_doi_id}}" href="javascript:;">
							<i class="fa skin-color fa-times"></i>
							<lb:get key="button_del"/>
						</a>
					{{/if}}
				</p>

		        <div class="wzb-trend-parcel">                   

				</div>

			</div>
		</div>
</script>

<script id="commentDescTemplate" type="text/x-jsrender">
<div class="wzb-reply clearfix">
	{{if isNormal}}
		<a href="${ctx }/app/personal/{{>user.usr_ent_id}}" class="wzb-reply-pic" title="" target="">
			<img width="34" height="34" src="${ctx}{{>user.usr_photo}}"/>
		</a>
	{{else}}
		<div class="wzb-reply-content">
			<img width="34" height="34" src="${ctx}{{>user.usr_photo}}"/>
		</div>
	{{/if}}

	<div class="wzb-reply-content">
		<p class="color-gray999">
			{{if user}}
				{{if isNormal}}
					<a href="${ctx }/app/personal/{{>user.usr_ent_id}}" class="wzb-link04" title="" target="">{{>user.usr_display_bil}}</a>
				{{else}}
					<span class="wzb-link04">{{>user.usr_display_bil}}</span>
				{{/if}}
			{{/if}}
			{{if toUser}}
				<lb:get key="detail_comment_to"/>
				{{if isNormal}}
					<a href="${ctx }/app/personal/{{>toUser.usr_ent_id}}" class="wzb-link04" title="" target="_blank">{{>toUser.usr_display_bil}}</a>
				{{else}}
					<span class="wzb-link04">{{>toUser.usr_display_bil}}</span>
				{{/if}}
			{{/if}}：
			{{>s_cmt_content}}
		  </p>
          <p>
               <span class="margin-right15 color-gray999">{{>s_cmt_create_datetime}}</span>
			   <a class="wzb-link03 margin-right15" href="javascript:;" title="" id="art_like_count_{{>s_cmt_id}}">
					<i class="fa mr5 f14 skin-color fa-thumbs-o-up"></i>(<span>0</span>)
				</a>
			   <a class="wzb-link03 margin-right15 review" uname="{{>user.usr_display_bil}}" uid="{{>s_cmt_uid}}" did="{{>s_cmt_id}}" href="javascript:;">
					<i class="fa skin-color fa-comment"></i>
			   </a>
			   {{if s_cmt_uid == '${prof.usr_ent_id}' || !isNormal}}
 					<a class="wzb-link03 delete desc" data="{{>s_cmt_id}}" href="javascript:;">
						<i class="fa skin-color fa-times"></i>
					</a>
			   {{/if}}
		</p>
	</div>
</div>
</script>

<script id="commentFormTemplate" type="text/x-jsrender">
<form class="wbedit" method="post" action="#">
    <textarea class="replypl wzb-textarea-01 align-bottom margin-right10"></textarea>
    <input type="button" class="btn wzb-btn-yellow align-bottom replybtn" value="<lb:get key='btn_submit'/>"/>
</form>

</script>

<script id="likeTemplate" type="text/x-jsrender">
<div class="wzb-trend clearfix" did="{{>id}}">
     <div class="wzb-user wzb-user68">
	      {{if operator}} 
          <a href="${ctx}/app/personal/{{>operator.usr_ent_id}}"><img class="wzb-pic" src="{{>operator.usr_photo}}"/></a>
		  {{/if}}
          <p class="companyInfo"><lb:get key="label_cm.label_core_community_management_29"/></p>
          <div class="cornerTL">&nbsp;</div>
          <div class="cornerTR">&nbsp;</div>
          <div class="cornerBL">&nbsp;</div>
          <div class="cornerBR">&nbsp;</div>
     </div>
        
     <div class="wzb-trend-content">
          <div class="color-gray999"><a href="${ctx }/app/personal/{{if operator}}{{>operator.usr_ent_id}}{{else}}{{>user.usr_ent_id}}{{/if}}" class="wzb-link04" title="">{{if operator}}{{>operator.usr_display_bil}}{{else}}{{>user.usr_display_bil}}{{/if}}</a> {{>crtTime}} {{>titleTcr}} :</div>
       
          <p>{{>title}}</p>
     </div>
</div>		
</script>
</html>