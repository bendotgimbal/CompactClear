package compact.mobile.config;

public class Koneksi {

	String baseurl_online = "http://api-mobile.atex.co.id/clear_mobile";
	String baseurl_offline = "http://43.252.144.14:81/clear_mobile";
	String baseurl_offline_2 = "http://43.252.144.14:81/compact_mobile";
	String baseurl_localhost = "http://192.168.50.2/compact_mobile2";
	
	public String koneksi_login()
	{
		String login_user = baseurl_offline_2 + "/login";
		return login_user;
	}
	
	public String ConnLogin()
	{
		String STR_PathLoginUser = "/compact_mobile/login";
		return STR_PathLoginUser;
	}
	
	public String ConnCekUpdate()
	{
		String STR_PathCekUpdate = "/compact_mobile/cekversi";
		return STR_PathCekUpdate;
	}
	
	public String ConnAssigment()
	{
		String STR_PathAssigment = "/compact_mobile/assigment/";
		return STR_PathAssigment;
	}
	
	public String ConnPOOutstanding()
	{
		String STR_PathPOOutstanding = "/compact_mobile/po_outstanding/";
		return STR_PathPOOutstanding;
	}
	
	public String ConnPOOutstandingScan()
	{
		String STR_PathPOOutstandingScan = "/compact_mobile/scan_pup_all";
		return STR_PathPOOutstandingScan;
	}
	
	public String ConnPOOutstandingClose()
	{
		String STR_PathPOOutstandingClose = "/compact_mobile/closing_po";
		return STR_PathPOOutstandingClose;
	}
	
	public String ConnADS()
	{
		String STR_PathADS = "/compact_mobile/tokoads/";
		return STR_PathADS;
	}
	
	public String ConnADSDetail()
	{
		String STR_PathADSDetail = "/compact_mobile/ads/";
		return STR_PathADSDetail;
	}
	
	public String ConnADSScan()
	{
		String STR_PathADSScan = "/compact_mobile/scan_awb_ads";
		return STR_PathADSScan;
	}
	
	public String ConnADSFinish()
	{
		String STR_PathADSFinish = "/compact_mobile/closing_po";
		return STR_PathADSFinish;
	}
	
	public String ConnADSFinishPincode()
	{
		String STR_PathADSFinishPincode = "/compact_mobile/closing_ads_all";
		return STR_PathADSFinishPincode;
	}
	
	public String ConnAWBOthers()
	{
		String STR_PathAWBOthers = "/compact_mobile/pickup_other/";
		return STR_PathAWBOthers;
	}
	
	public String ConnAWBOthersDetail()
	{
		String STR_PathAWBOthersDetail = "/compact_mobile/details_pickup_other/";
		return STR_PathAWBOthersDetail;
	}
	
	public String ConnAWBOthersScan()
	{
//		String STR_PathAWBOthersScan = "/compact_mobile/scan_awb_ads";
		String STR_PathAWBOthersScan = "/compact_mobile/scan_pup_all";
		return STR_PathAWBOthersScan;
	}
	
	public String ConnAWBOthersClose()
	{
		String STR_PathAWBOthersClose = "/compact_mobile/closing_other";
		return STR_PathAWBOthersClose;
	}
	
	public String ConnPODDEX()
	{
		String STR_PathPODDEX = "/compact_mobile/poddex/";
		return STR_PathPODDEX;
	}
	
	public String ConnPODScan()
	{
		String STR_PathPODScan = "/compact_mobile/scan_pod";
		return STR_PathPODScan;
	}
	
	public String ConnDEXScan()
	{
		String STR_PathDEXScan = "/compact_mobile/scan_dex";
		return STR_PathDEXScan;
	}
	
	public String ConnSetorCOD()
	{
//		String STR_PathSetorCOD = "/android/";
		String STR_PathSetorCOD = "/compact_mobile/getlistsetorancod";
		return STR_PathSetorCOD;
	}
	
	public String ConnAwbCOD()
	{
		String STR_PathAwbCOD = "/compact_mobile/getlistawbcod";
		return STR_PathAwbCOD;
	}
	
	public String ConnInsertCOD()
	{
		String STR_PathInsertCOD = "/compact_mobile/insert_scod";
		return STR_PathInsertCOD;
	}

	public String ConnAddIDFirebase()
	{
		String STR_PathAddIDFirebase = "/compact_mobile/add_devicefirebase";
		return STR_PathAddIDFirebase;
	}

	public String ConnRemoveIDFirebase()
	{
		String STR_PathRemoveIDFirebase = "/compact_mobile/remove_devicefirebase";
		return STR_PathRemoveIDFirebase;
	}

	public String ConnDEXList()
	{
//		String STR_PathDEXList = baseurl_localhost + "/remarkdex";
		String STR_PathDEXList = "/compact_mobile/remarkdex";
		return STR_PathDEXList;
	}

	public String ConnPODList()
	{
//		String STR_PathDEXList = baseurl_localhost + "/remarkdex";
		String STR_PathPODList = "/compact_mobile/remarkpod";
		return STR_PathPODList;
	}

	public String ConnCekConnection()
	{
		String STR_CekConnection = "/compact_mobile/cekconnection";
		return STR_CekConnection;
	}

	public String ConnAccessKeyAWS()
	{
		String STR_CekAccessKeyAWS = "/compact_mobile/access_key_aws";
		return STR_CekAccessKeyAWS;
	}

	public String ConnScanSignature()
	{
		String STR_CekScanSignature = "/compact_mobile/aws_filename";
		return STR_CekScanSignature;
	}

	public String ConnBadgeCount()
	{
		String STR_CekBadgeCount = "/compact_mobile/badge_count/";
		return STR_CekBadgeCount;
	}
	
}
