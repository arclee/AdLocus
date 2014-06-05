package com.ad.adlocus;


//import java.util.GregorianCalendar;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.FrameLayout;



import com.adlocus.*;

public class CAdLocus
{
	//unity 的 active.
    private Activity mActivity;
    
    //設定是否為測試模式.
    private boolean isTest = false;
    
    //在 AdLocals 應用程式網站建立的 APP ID.
    private String appid = ""; 
    
    // AdLocus 的廣告.
    private AdLocusLayout adlocusLayout = null;
    // AdLocus 廣告 layout 參數
    private LinearLayout.LayoutParams adlocusLayoutParams = null;
    // 廣告的設定.
    private AdLocusTargeting targeting = null;
    // 廣告事件監聽.
    private AdListener listener = null;
    //unity 應用程式建一個 layout 用來放廣告.
    private FrameLayout framelayout = null;
    
    //callback.
    CAdLocusListenerCallBack listenerCB = null;
    
    public CAdLocus(Activity currentActivity)
    {
    	mActivity = currentActivity;
    }
    
    public void SetListenerCB(CAdLocusListenerCallBack cb)
    {
    	listenerCB = cb;
    }
    
    //是否為測試.
    public void SetTestMode(boolean istest)
    {
    	isTest = istest;
    }

    //設定應用ID.
    public void SetAppID(String id)
    {
    	appid = id;
    }

    private void CreateAdListener()
    {
    	if (listener == null)
    	{
    		listener =  new AdListener()
			{
    			@Override
    			public void onReceiveAd(Ad adView )
    			{
    				//AdLocusLayout llayout = (AdLocusLayout)adView;
    				if (listenerCB != null)
    				{
    					listenerCB.onReceiveAd();
    				}
    			}
    			@Override
    			public void onFailedToReceiveAd(Ad adView , com.adlocus.AdLocusLayout$ErrorCode errorCode)
    			{
    				if (listenerCB == null)
    				{
	    				Log.v("CAdLocus", "No CB");
    				}
    				
	    			//AdLocusLayout llayout = (AdLocusLayout) adView;
	    			switch (errorCode)
	    			{
	    			case NO_FILL :
	    			// 無廣告展示 .
	    				if (listenerCB != null)
	    				{
	    					listenerCB.onFailedToReceiveAd(1);
	    				}
	    				Log.v("CAdLocus", "NO_FILL");
	    			break ;
	    			case INVAILD_KEY :
	    			// 錯誤的 APP KEY .
	    				if (listenerCB != null)
	    				{
	    					listenerCB.onFailedToReceiveAd(2);
	    				}
	    				Log.v("CAdLocus", "INVAILD_KEY");
	    			break ;
	    			case NETWORK_ERROR :
	    			// 網路連線問題
	    				if (listenerCB != null)
	    				{
	    					listenerCB.onFailedToReceiveAd(3);
	    				}
	    				Log.v("CAdLocus", "NETWORK_ERROR");
	    			break ;
	    			case SERVICE_ERROR :
	    			// 伺服器忙碌或維護中
	    				if (listenerCB != null)
	    				{
	    					listenerCB.onFailedToReceiveAd(4);
	    				}
	    				Log.v("CAdLocus", "SERVICE_ERROR");
	    			break ;
	    			default :
	    			break ;
	    			}
    			}
			}
    		;
    	}
    	
    }
    
    private void CreateAdLocusTargeting()
    {
    	if (targeting == null)
    	{
        	targeting = new AdLocusTargeting();
    	}
    	
    	// 設定年齡為23歲。
    	//targeting.setAge(23);
    	// 設定生日為 1990/12/29.
    	//targeting.setBirthDate(new GregorianCalendar(1990, 12, 29));
    	// 設定性別為男性。
    	//targeting.setGender(AdLocusTargeting$Gender.MALE);
    	// 設為非測試中。
    	targeting.setTestMode(isTest);    		
    }
    
    private void CreateAdLocusLayout()
    {
    	if (adlocusLayout == null)
    	{
    		Log.v("CAdLocus", appid);
			adlocusLayout =
					new AdLocusLayout(
					mActivity,
					AdLocusLayout.AD_SIZE_BANNER, //設定廣告大小，通常為 Banner 
					appid, //填入 app key
					15
					,targeting); //設定輪播時間，最低 15 秒，-1 為不輪播只顯示一則
			adlocusLayout.setListener(listener);
    	}
    	
		
		// 設定輪播動畫，預設為淡入，範例設定為隨機動畫
		adlocusLayout.setTransitionAnimation(AdLocusLayout.ANIMATION_RANDOM);
    }
    
    private void CreateadlocusLayoutParams()
    {
    	if (adlocusLayoutParams == null)
    	{
        	adlocusLayoutParams = new LinearLayout.LayoutParams(
        			ViewGroup.LayoutParams.WRAP_CONTENT,
        			ViewGroup.LayoutParams.WRAP_CONTENT);
        	// 設定置中
        	adlocusLayoutParams.gravity = Gravity.CENTER;    		
    	}
    	
    }
    
    private void CreateFrameLayout()
    {
    	if (framelayout == null)
    	{
    		framelayout = new FrameLayout(mActivity);
    		mActivity.addContentView(framelayout,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
    	}
				
    }
    
    //顯示廣告.
    public void HideAD()
    {

        mActivity.runOnUiThread(new Runnable()
        {
			public void run()
            {
		    	framelayout.removeView(adlocusLayout);
				framelayout.invalidate();
            }
        });
        
    }

    //不顯示廣告.
    public void ShowAD()
    {
        mActivity.runOnUiThread(new Runnable()
        {
			public void run()
            {
		    	framelayout.removeView(adlocusLayout);
				//framelayout.setGravity(Gravity.CENTER_HORIZONTAL);
		    	framelayout.addView(adlocusLayout, adlocusLayoutParams);
				framelayout.invalidate();
            }
        });
    	
    }
    
    
    private void CreateAll()
    {
    	CreateFrameLayout();
    	CreateadlocusLayoutParams();
		CreateAdLocusTargeting();
		CreateAdListener();
		CreateAdLocusLayout();    	
    }
    
    // 初始化.
	public void Inital()
	{
		
        mActivity.runOnUiThread(new Runnable()
        {
            //@SuppressWarnings("deprecation")
			public void run()
            {
				CreateAll();
            }
        });
        

	}
}
