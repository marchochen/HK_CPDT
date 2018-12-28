<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />



</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_CPT_D_MGT"/>

	 <title:get function="global.FTN_AMD_CPT_D_MGT"/>
    <ol class="breadcrumb wzb-breadcrumb">
        <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
        <li class="active"><lb:get key="global.FTN_AMD_CPT_D_HISTORY" /></li>
    </ol>
    <!-- wzb-breadcrumb End-->

    <div class="panel wzb-panel">
        <div class="panel-heading"><lb:get key="label_cpd.label_core_cpt_d_management_2" /></div>

        <div class="panel-body" style="overflow:auto;">
        <form class="form-search" onsubmit="return false;">
        <table>
        <tr>
        <td width="30%" >
            <!-- 评估年度 -->
            <div style="float:left;"><lb:get key="label_cpd.label_core_cpt_d_management_182" />：
                <select name="period_select"  id = "period_select"  onchange="reloadTable()">
                    <option value="0">--</option>
                          <c:forEach items="${periods}"  var="periodLists">
                          <option value="${periodLists.cghi_period }">${periodLists.cghi_period }</option>
                          </c:forEach>
                </select>
                
            </div>
         </td>
        <td width="30%">
            <!-- 牌照类别 -->
            <div style="float:left;"><lb:get key="label_cpd.label_core_cpt_d_management_3" />：
                <select name="ct_id_select"  id = "ct_id_select"  onchange="reloadTable()">
                    <option value="0">--</option>
                    <c:forEach items="${cpdTypeList}"  var="cpdType">
                      <option value="${cpdType.ct_id }">${cpdType.ct_license_type }</option>
                    </c:forEach>
                </select>
                
            </div>
        </td>
        <td width="30%">
            <lb:get key="label_cpd.label_core_cpt_d_management_190" />：
            <input name="searchText" id = "searchText"    type="text" class="form-control" >
        </td>
        <td width="10%">
        
            <button type="button" onclick="reloadTable()" class="btn wzb-btn-blue margin-right10 wzb-btn-big">
                 <lb:get key="global.button_search" />
            </button>
            
        </td>
        </tr>
        </table>
            
        
            </form>

            <div id="cpdRegistrationMgt_list"></div>

        </div>
    </div>
    <!-- wzb-panel End-->
    <script type="text/javascript">
    var searchTb;
    var encrytor = wbEncrytor();
    $(function() {
         searchTb = $("#cpdRegistrationMgt_list").table({
            url : '${ctx}/app/admin/cpdHistoryRecord/listJson',
            dataType : 'json',
            colModel : colModel,
            rp : 10,
            showpager : 3,
            usepager : true
        }); 
        
        $(document).keydown(function(event){
      	  if(event.keyCode ==13){
      		  reloadTable();
      	  }
     	});
    })

    var colModel = [
            {
            	//用户名
                name : 'usr_ste_usr_id',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_58" />',
                align : 'left',
                format : function(data) {
                    p = {
                        text : data.usr_ste_usr_id
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
            	//全名
                name : 'usr_display_bil',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_59" />',
                align : 'center',
                format : function(data) {
                    p = {
                        text : data.usr_display_bil
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
            	//牌照类别
                name : 'ct_license_type',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_3" />',
                align : 'center',
                format : function(data) {
                    p = {
                        text : data.cghi_license_type
                    }
                    return $('#text-center-template').render(p);
                }
            },
            {
                //牌照别名
                name : 'ct_license_type',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_67" />',
                align : 'center',
                format : function(data) {
                    p = {
                        text : data.cghi_license_alias
                    }
                    return $('#text-center-template').render(p);
                }
            },
            {
                //组别编号
                name : 'ct_license_type',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_83" />',
                align : 'center',
                format : function(data) {
                    p = {
                        text : data.cghi_code
                    }
                    return $('#text-center-template').render(p);
                }
            },
            {
                //分组别名
                name : 'ct_license_type',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_183" />',
                align : 'center',
                format : function(data) {
                    p = {
                        text :data.cghi_alias
                    }
                    return $('#text-center-template').render(p);
                }
            },
            {
            	//注册时间
                name : 'cr_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_61" />',
                align : "center",
                format : function(data) {
                    p = {
                        text :data.cghi_cr_reg_date==null?"--":data.cghi_cr_reg_date.substring(0, 10)
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
            	//除牌时间
                name : 'cr_de_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_62" />',
                align : "center",
                format : function(data) {
                    p = {
                        text : data.cghi_cr_de_reg_date==null? "--":data.cghi_cr_de_reg_date.substring(0, 10)
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                //注册号码
                name : 'cr_reg_number',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_60" />',
                align : "center",
                format : function(data) {
                    p = {
                        text :data.cghi_cr_reg_number==null? "--":data.cghi_cr_reg_number
                    };
                    return $('#text-center-template').render(p);
                }
            }, 
            {
                //开始时间
                name : 'cr_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_184" />',
                align : "center",
                format : function(data) {
                    p = {
                        text :data.cghi_initial_date==null?"--":data.cghi_initial_date.substring(0, 10)
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                //过期时间
                name : 'cr_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_185" />',
                align : "center",
                format : function(data) {
                    p = {
                        text :data.cghi_expiry_date==null?"--":data.cghi_expiry_date.substring(0, 10)
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                //评估年度
                name : 'cr_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_182" />',
                align : "center",
                format : function(data) {
                    p = {
                        text : data.cghi_period
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                //要求核心时数
                name : 'cr_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_186" />',
                align : "center",
                format : function(data) {
                    p = {
                        text : data.cghi_execute_core_hours==null?"0.0":data.cghi_execute_core_hours
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                //要求非核心时数
                name : 'cr_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_187" />',
                align : "center",
                format : function(data) {
                    p = {
                        text : data.cghi_execute_non_core_hours==null?"0.0":data.cghi_execute_non_core_hours
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                //获得核心时数
                name : 'cr_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_188" />',
                align : "center",
                format : function(data) {
                    p = {
                        text : data.cghi_award_core_hours==null?"0.0":data.cghi_award_core_hours
                    };
                    return $('#text-center-template').render(p);
                }
            },
            {
                //获得非核心时数
                name : 'cr_reg_datetime',
                display : '<lb:get key="label_cpd.label_core_cpt_d_management_189" />',
                align : "center",
                format : function(data) {
                    p = {
                            text : data.cghi_award_non_core_hours==null?"0.0":data.cghi_award_non_core_hours
                    };
                    return $('#text-center-template').render(p);
                }
            } ];

    function reloadTable() {
        var searchText = $('#searchText').attr("value");
        var ct_id = $('#ct_id_select option:selected') .val();
        var period = $('#period_select option:selected') .val();
        $(searchTb).reloadTable({
            url : '${ctx}/app/admin/cpdHistoryRecord/listJson',
            params : {
            	searchText : searchText,
                ct_id:ct_id,
                period : period,
            },
            dataType : 'json'
        });
    };
    

</script>

<script id="text-center-template" type="text/x-jsrender">
        <div>{{>text}}</div>
</script>

<script id="cpdInfo-template" type="text/x-jsrender">
 <span class="datatable-table-column text-center"> <a href="javascript:go('{{>href}}');">{{>text}}</a>   </span>
</script>
</body>
</html>