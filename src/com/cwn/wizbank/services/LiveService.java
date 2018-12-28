package com.cwn.wizbank.services;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.LiveItem;
import com.cwn.wizbank.exception.ErrorException;

public interface LiveService {
	
	public LiveItem createLVBChannel(LiveItem liveItem) throws ErrorException;

	public LiveItem updateLive(LiveItem liveItem) throws ErrorException;

	public boolean deleteLive(LiveItem liveItem) throws ErrorException;

	public LiveItem stopLive(LiveItem liveItem) throws ErrorException;

	public int getOnlineUsers(LiveItem liveItem);

	public int getLiveState(LiveItem liveItem);

	public LiveItem startLive(LiveItem liveItem, loginProfile prof) throws ErrorException;

	public void getInitLang(loginProfile prof);


}
