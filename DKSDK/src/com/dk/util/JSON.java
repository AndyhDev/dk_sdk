package com.dk.util;

import org.json.JSONArray;

public class JSON {
	public static JSONArray RemoveJSONArray(JSONArray array, int index) {
		JSONArray temp =new JSONArray();
		try{
			for(int i = 0; i < array.length(); i++){     
			    if(i != index)
			        temp.put(array.get(i));     
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return temp;
	}
}
