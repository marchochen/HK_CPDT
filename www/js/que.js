function getQuePanel(pageStore, no_toolbar) {
	var noToolbar;
	if(no_toolbar === undefined || no_toolbar === '') {
		noToolbar = false;
	} else {
		noToolbar = no_toolbar;
	}
	var queData = pageStore.reader.jsonData['que_detail'];
	var queType;
	if(queData !== undefined) {
		queType = queData['que_type'];
	}
	var isAnswered = getIsAnwsered(pageStore);
	var panelHtml = '';
	if(queData !== undefined) {
		panelHtml = WzbHtm.que(
			{
				1 : Wzb.getRelativeImagePath(queData['usr_photo']),
				2 : Wzb.l('227'),
				4 : queData['usr_nickname'],
				3 : 'javascript:showUserInfoWin(' + queData['usr_ent_id'] + ')',
				5 : Wzb.l('219'),
				6 : Wzb.displayTime(queData['que_create_timestamp']),
				7 : function(){
					var str = '';
					if(isAnswered) {
						str = '<br/>';
					}
					return str;
				}(),
				8 : function() {
					var str = '';
					if(isAnswered) {
						str = Wzb.l('237') + ': ';
					}
					return str;
				}(),
				9 :  function(){
					var str = '';
					if(isAnswered) {
						str = Wzb.displayTime(queData['que_answered_timestamp']);
					}
					return str;
				}(),
				10 : Wzb.l('218'),
				11 : queData['ans_count'],
				12 : queData['que_title'],
				13 : Wzb.unescapeHtmlLineFeed(queData['que_content']) 
			}
		);
	}
	var title = function() {
		var str = '';
		if(queType === 'SOLVED') {
			str = Wzb.l('546');
		} else if(queType === 'UNSOLVED') {
			str = Wzb.l('547');
		} else if(queType === 'FAQ'){
			str = Wzb.l('684');
		}
		return str;
	}();
	var que = new Wzb.Panel({
		cls: 'bottom',
		id : 'que_panel',
		title : title
	});
	if(!noToolbar && !isAnswered) {
		var queToolBar = new Ext.Toolbar({
			id : 'unsolved_toolbar',
			items : ['->']
		});
		que.add(queToolBar)
	}
	que.add({
		border : false,
		bodyStyle : Wzb.style_inner_space,
		html: panelHtml
	});
	return que;
}

function getAnswerPanel(pageStore, que_id) {
	var mainData = pageStore.reader.jsonData;
	var isMineQue = getIsMineQue(pageStore);
	var isAnswered = getIsAnwsered(pageStore);
	var ansData = mainData['que_other_ans'];
	var answer;
	if(ansData !== undefined) {
		var ans_store = new Ext.data.Store({
			proxy: new Ext.data.MemoryProxy(ansData),
			autoLoad: true,
			reader: new Ext.data.JsonReader({
				root : 'ans_lst'			
			},
			[
				{name: 'usr_photo'},
				{name: 'usr_nickname'},
				{name: 'usr_ent_id'},
				{name: 'ans_create_timestamp'},
				{name: 'ans_create_ent_id'},
				{name: 'ans_id'},
				{name: 'ans_content'},
				{name: 'ans_refer_content'}
			])
		});
		var renderAns = function(value, p, record) {
			var disCls = 'displayNone';
			if(record.data.ans_refer_content !== undefined && record.data.ans_refer_content !== '') {
				disCls = '';
			}
			var s = WzbHtm.answer(
				{
					1 : Wzb.getRelativeImagePath(record.data.usr_photo),
					2 : Wzb.l('233'),
					4 : record.data.usr_nickname,
					3 : 'javascript:showUserInfoWin(' + record.data.usr_ent_id + ')',
					5 : Wzb.l('234'),
					6 : Wzb.displayTime(record.data.ans_create_timestamp),
					8 : Wzb.unescapeHtmlLineFeed(record.data.ans_content),
					7 : function() {
						var str = '';
						if(isAnswered === false && isMineQue === true) {
							var rightAnsHandler = 'rightAnsAction(' + que_id + ','  + record.data.ans_id + ')';
							str = Wzb.getButton(rightAnsHandler, Wzb.l('258'));
						}
						return str;
					}(),
					9 : disCls,
					10 : Wzb.l('236'),
					11 : record.data.ans_refer_content
				}
			);
			return s;
		};
		answer = new Wzb.GridPanel({
			title :Wzb.l('228')+ '(' + ansData['ans_count'] + ')',
			border : true,
			cls: 'bottom',
			hideHeaders : true,
			store : ans_store,
			columns : [{
				width : '100%',
				dataIndex: 'usr_photo',
				renderer : renderAns
			}],
			type : 5,
			viewConfig : {
				emptyText : Wzb.getEmptyText()
			}
		});
	} else {
		answer = {border : false};
	}
	return answer;
}

function getBestAnsPanel(pageStore, dis_vote) {
	var disableVote;
	if(dis_vote === undefined || dis_vote === '') {
		disableVote = false;
	} else {
		disableVote = dis_vote;
	}
	var mainData = pageStore.reader.jsonData;
	var queOutOfDate = false;
	var usrCanVote = true;
	if(mainData['que_vote'] !== undefined) {
		queOutOfDate = !mainData['que_vote']['allow_vote'];
		usrCanVote = !mainData['que_vote']['has_vote'];
	}
	var ansData = mainData['que_right_ans'];
	var bestAns;
	if(ansData !== undefined) {
		var disCls = 'displayNone';
		if(ansData['ans_refer_content'] !== undefined && ansData['ans_refer_content'] !== '') {
			disCls = '';
		}

		var que = new Wzb.Panel({
			html: WzbHtm.bestanswer(
				{
					1 : Wzb.getRelativeImagePath(ansData['usr_photo']),
					2 : Wzb.l('233'),
					4 : ansData['usr_nickname'],
					3 : 'javascript:showUserInfoWin(' + ansData['usr_ent_id'] + ')',
					5 : Wzb.l('234'),
					6 : Wzb.displayTime(ansData['ans_create_timestamp']),
					7 : Wzb.unescapeHtmlLineFeed(ansData['ans_content']),
					8 : disCls,
					9 : Wzb.l('236'),
					10 : ansData['ans_refer_content']
				}
			)
		});
		var msg = Wzb.l('239');
		if(queOutOfDate) {
			msg = '<font color="#FF0080">'+Wzb.l('553')+'<font>';
		}
		var pingFenPanel = new Wzb.Panel({
			hideBorders: true,
			items: [
				{
					html: WzbHtm.desc1({
						1 : msg, 
						2 : Wzb.l('554'), 
						3 : ansData['ans_vote_total'], 
						4 : Wzb.l('556')
					})
				},
				{
					hideBorders: true,
					layout: 'column',
					items: [
						{
							width: 80,
							hideBorders: true,
							items:[
								{
									xtype: 'button',
									text: Wzb.l('362'),
									id : 'vote_for',
									disabled : chkDisableBtn(),
									handler : voteQue
								},
								{
									html: WzbHtm.votefor(
										{
											1 : ansData['ans_vote_for_rate'],
											2 : ansData['ans_vote_for']
										}
									)
								}
							]
						},
						{
							width: 80,
							hideBorders: true,
							items:[
								{
									xtype: 'button',
									text: Wzb.l('241'),
									id : 'vote_down',
									disabled : chkDisableBtn(),
									handler : voteQue
								},
								{
									html: WzbHtm.votedown(
										{
											1 : ansData['ans_vote_down_rate'],
											2 : ansData['ans_vote_down']
										}
									)
								}
							]
						}
					]
				}
			]
		});
		bestAns = new Wzb.Panel({
			cls: 'bottom',
			title : Wzb.l('238'),
			hideBorders: true,
			bodyStyle: Wzb.style_inner_space,
			items: [
				que,
				pingFenPanel
			],
			type : 4
		})
	} else {
		bestAns = {border : false};
	}
	return bestAns;
	
	function voteQue(btnObj) {
		if(!usrCanVote) {
			alert(Wzb.l('557'));
			return;
		}
		var vote_for_ind = false;
		if(btnObj.id === 'vote_for') {
			vote_for_ind = true;
		}
		var url_success = '../htm/que_detail.htm?que_id=' + que_id;
		var url_failure = url_success;
		window.location.href = Wzb.getDisUrl('module', 'JsonMod.know.KnowModule', 'cmd', 'vote_ans', 'ans_id', mainData['que_right_ans']['ans_id'], 'vote_for_ind', vote_for_ind, 'url_success', url_success, 'url_failure', url_failure);
	}
	
	function chkDisableBtn() {
		var dis = false;
		if(queOutOfDate || disableVote) {
			dis = true;
		}
		return dis;
	}

}

function getFaqAnsPanel(pageStore) {
	var ansData = pageStore.reader.jsonData['que_right_ans'];
	var faqAns;
	if(ansData !== undefined) {
		var que = new Wzb.Panel({
			html: WzbHtm.faqanswer(
				{
					1 : Wzb.unescapeHtmlLineFeed(ansData['ans_content'])
				}
			)
		});
		faqAns = new Wzb.Panel({
			cls: 'bottom',
			title : Wzb.l('683'),
			hideBorders: true,
			bodyStyle: Wzb.style_inner_space,
			items: [
				que
			],
			type : 4
		})
	} else {
		faqAns = {border : false};
	}
	return faqAns;
	
	function voteQue(btnObj) {
		if(!usrCanVote) {
			alert(Wzb.l('557'));
			return;
		}
		var vote_for_ind = false;
		if(btnObj.id === 'vote_for') {
			vote_for_ind = true;
		}
		var url_success = '../htm/que_detail.htm?que_id=' + que_id;
		var url_failure = url_success;
		window.location.href = Wzb.getDisUrl('module', 'JsonMod.know.KnowModule', 'cmd', 'vote_ans', 'ans_id', mainData['que_right_ans']['ans_id'], 'vote_for_ind', vote_for_ind, 'url_success', url_success, 'url_failure', url_failure);
	}
	
	function chkDisableBtn() {
		var dis = false;
		if(queOutOfDate || disableVote) {
			dis = true;
		}
		return dis;
	}

}

function getIsMineQue(pageStore) {
	var mainData = pageStore.reader.jsonData;
	var isMineQue = false;
	if(mainData['que_detail'] !== undefined && mainData['meta']['ent_id'] === mainData['que_detail']['que_create_ent_id']) {
		isMineQue = true;
	}
	return isMineQue;
}

function getIsAnwsered(pageStore) {
	var queData = pageStore.reader.jsonData['que_detail'];
	var isAnswered = false;
	if(queData !== undefined && (queData['que_type'] === 'SOLVED' || queData['que_type'] === 'FAQ')) {
		isAnswered = true;
	}
	return isAnswered;
}