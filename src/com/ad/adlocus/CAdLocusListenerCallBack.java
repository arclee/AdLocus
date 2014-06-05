package com.ad.adlocus;

public interface CAdLocusListenerCallBack {
	void onReceiveAd();
	void onFailedToReceiveAd(int errorcode);
}
