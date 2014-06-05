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
	//unity �� active.
    private Activity mActivity;
    
    //�]�w�O�_�����ռҦ�.
    private boolean isTest = false;
    
    //�b AdLocals ���ε{�������إߪ� APP ID.
    private String appid = ""; 
    
    // AdLocus ���s�i.
    private AdLocusLayout adlocusLayout = null;
    // AdLocus �s�i layout �Ѽ�
    private LinearLayout.LayoutParams adlocusLayoutParams = null;
    // �s�i���]�w.
    private AdLocusTargeting targeting = null;
    // �s�i�ƥ��ť.
    private AdListener listener = null;
    //unity ���ε{���ؤ@�� layout �Ψө�s�i.
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
    
    //�O�_������.
    public void SetTestMode(boolean istest)
    {
    	isTest = istest;
    }

    //�]�w����ID.
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
	    			// �L�s�i�i�� .
	    				if (listenerCB != null)
	    				{
	    					listenerCB.onFailedToReceiveAd(1);
	    				}
	    				Log.v("CAdLocus", "NO_FILL");
	    			break ;
	    			case INVAILD_KEY :
	    			// ���~�� APP KEY .
	    				if (listenerCB != null)
	    				{
	    					listenerCB.onFailedToReceiveAd(2);
	    				}
	    				Log.v("CAdLocus", "INVAILD_KEY");
	    			break ;
	    			case NETWORK_ERROR :
	    			// �����s�u���D
	    				if (listenerCB != null)
	    				{
	    					listenerCB.onFailedToReceiveAd(3);
	    				}
	    				Log.v("CAdLocus", "NETWORK_ERROR");
	    			break ;
	    			case SERVICE_ERROR :
	    			// ���A�����L�κ��@��
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
    	
    	// �]�w�~�֬�23���C
    	//targeting.setAge(23);
    	// �]�w�ͤ鬰 1990/12/29.
    	//targeting.setBirthDate(new GregorianCalendar(1990, 12, 29));
    	// �]�w�ʧO���k�ʡC
    	//targeting.setGender(AdLocusTargeting$Gender.MALE);
    	// �]���D���դ��C
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
					AdLocusLayout.AD_SIZE_BANNER, //�]�w�s�i�j�p�A�q�`�� Banner 
					appid, //��J app key
					15
					,targeting); //�]�w�����ɶ��A�̧C 15 ��A-1 ���������u��ܤ@�h
			adlocusLayout.setListener(listener);
    	}
    	
		
		// �]�w�����ʵe�A�w�]���H�J�A�d�ҳ]�w���H���ʵe
		adlocusLayout.setTransitionAnimation(AdLocusLayout.ANIMATION_RANDOM);
    }
    
    private void CreateadlocusLayoutParams()
    {
    	if (adlocusLayoutParams == null)
    	{
        	adlocusLayoutParams = new LinearLayout.LayoutParams(
        			ViewGroup.LayoutParams.WRAP_CONTENT,
        			ViewGroup.LayoutParams.WRAP_CONTENT);
        	// �]�w�m��
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
    
    //��ܼs�i.
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

    //����ܼs�i.
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
    
    // ��l��.
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
