/*******************************************************************************
* KindEditor - WYSIWYG HTML Editor for Internet
* Copyright (C) 2006-2011 kindsoft.net
*
* @author Roddy <luolonghao@gmail.com>
* @site http://www.kindsoft.net/
* @licence http://www.kindsoft.net/license.php
*******************************************************************************/

KindEditor.plugin('fb', function(K) {
	var self = this, name = 'fb';
	self.plugin.fb = {
		edit : function() {
			var lang = self.lang(name + '.'),
				html = '<div style="padding:30px;">' +
					//answer
					'<div class="ke-dialog-row">' +
					'<label for="keAnswer" style="width:60px;text-align:right;"><span class="wzb-form-star">*</span>' + lang.answer + '</label>' +
					'<input class="ke-input-text" type="text" id="keAnswer" name="answer" value="" style="width:260px;" /></div>' +
					//score
					'<div class="ke-dialog-row"">' +
					'<label for="keScore" style="width:60px;text-align:right;"><span class="wzb-form-star">*</span>' + lang.score + '</label>' +
					'<input class="ke-input-text" type="text" id="keScore" name="score" value="" style="width:260px;" maxlength="4"/></div>' +
                    //Explanation 
					'<div class="ke-dialog-row"">' +
					'<label for="keExp" style="width:60px;text-align:right;">' + lang.explanation + '</label>' +
					'<input class="ke-input-text" type="text" id="keExp" name="exp" value="" style="width:260px;"/></div>' +
					//xingxing 
					'<div class="ke-dialog-row"">' +
					'<label style="width:60px;"></label>' +
					'<span class="wzb-form-star">*</span>'+lang.required+'</div>' +
					
					'</div>',
				dialog = self.createDialog({
					name : name,
					width : 450,
					title : self.lang(name),
					body : html,
					yesBtn : {
						name : self.lang('yes'),
						click : function(e) {
							var answer = K.trim(answerBox.val());
							var score = K.trim(scoreBox.val());
							var explanation = K.trim(explanationBox.val());
							var reg = /^\d+$/;
							if(answer == '' || answer.length <= 0) {
								alert(self.lang('invalidAnswer'));
								return false;
							}
							if(getChars(answer) > 2000)
							{
								alert(self.lang('fb.answer')+fetchLabel('label_core_training_management_372'));
								return;
							}
							if(!reg.test(score)) {
								alert(self.lang('invalidDigital'));
								return;
							}
							if(score <= 0) {
								alert(fetchLabel("label_core_training_management_355"));
								return;
							}	
							if(getChars(explanation) > 2000)
							{
								alert(self.lang('fb.explanation')+fetchLabel('label_core_training_management_372'));
								return;
							}
//							if (url == 'http://' || K.invalidUrl(url)) {
//								alert(self.lang('invalidUrl'));
//								answerBox[0].focus();
//								return;
//							}
							self.exec('createfb', answer, score,explanation).hideDialog().focus();
						}
					}
				}),
				div = dialog.div,
				answerBox = K('input[name="answer"]', div),
				scoreBox = K('input[name="score"]', div),
				explanationBox = K('input[name="exp"]', div);
				                                
			//urlBox.val('http://');
			
			//typeBox[0].options[0] = new Option(lang.newWindow, '_blank');
			//typeBox[0].options[1] = new Option(lang.selfWindow, '');
			self.cmd.selection();
			var a = self.plugin.getSelectedImage();
			if (a) {
				self.cmd.range.selectNode(a[0]);
				self.cmd.select();
				answerBox.val(a.attr('title'));
				scoreBox.val(a.attr('id'));
				
				var ans = a.attr('alt').split('[FB_split~]');
					if(ans.length >= 4) {
						explanationBox.val(ans[3]);
					}
						
				
			}
			answerBox[0].focus();
			answerBox[0].select();
		}
	};
	self.clickToolbar(name, self.plugin.fb.edit);
});
