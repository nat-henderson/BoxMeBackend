package hackathon.boxme.register;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import java.io.File;

import java.io.PrintWriter;

import java.util.Scanner;

import com.dropbox.client2.DropboxAPI;

import com.dropbox.client2.exception.DropboxException;

import com.dropbox.client2.session.AccessTokenPair;

import com.dropbox.client2.session.AppKeyPair;

import com.dropbox.client2.session.RequestTokenPair;

import com.dropbox.client2.session.Session.AccessType;

import com.dropbox.client2.session.WebAuthSession;

public class DropBoxAuth {
	
	final static private String APP_KEY = "u4d52lnoqpoqztk";
	final static private String APP_SECRET = "58xjxsb08ybg584";
	final static private AccessType ACCESS_TYPE = AccessType.DROPBOX;
	private static AppKeyPair appKeys; 
	private static WebAuthSession session; 
	private static DropboxAPI<WebAuthSession> mDBApi;
	private static AccessTokenPair tokenPair;
	
	
	public static void getTokens(String facebookid){

		RequestTokenPair tokens = new RequestTokenPair(tokenPair.key, tokenPair.secret);

		try {
			mDBApi.getSession().retrieveWebAccessToken(tokens);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Credentials creds = new DropBoxCredentials("dropbox", facebookid, session.getAccessTokenPair().key, session.getAccessTokenPair().secret);
		try {
			// How to get the UID here ??
			String uId = "dummy";
			boolean result = RegisterAccount.main(uId, creds);
		} catch (AccountNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getUrl() {
		appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
		session = new WebAuthSession(appKeys,ACCESS_TYPE);
		mDBApi = new DropboxAPI<WebAuthSession>(session);
		String url = "";
		try {
			url = mDBApi.getSession().getAuthInfo().url;
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tokenPair = mDBApi.getSession().getAccessTokenPair();
		return url;
	}
	
	

}
