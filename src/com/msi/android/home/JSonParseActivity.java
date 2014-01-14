package com.msi.android.home;

import java.io.File;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.mobilimsel.util.JSonParser;

public class JSonParseActivity extends Activity {

	//url de�iti
	public String urlip = "http://192.168.1.100:8080/";
	//public String urlip = "http://192.168.1.67/";
	
	//public String urlip = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	//public String urlip = "http://192.168.1.107/";
	//public String urlip = "http://192.168.178.82:8080/";
	public String configDir = "astek/configfiles/json/";

	public List<JSONObject> items = null;

	public JSONArray jsonArray = null;

	public String url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void parseJSon(String url, String enc) {
		try {

			jsonArray = new JSONArray(JSonParser.getJSonArray(url, enc)
					.optJSONObject("responseData").optJSONArray("results")
					.toString());

			if (jsonArray == null)
				return;
		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

}
