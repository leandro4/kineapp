package com.gon.kineapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SharedPreferencesEditor {

  private final SharedPreferences mSharedPreferences;

  public SharedPreferencesEditor(Context context, String prefix) {
    mSharedPreferences = context.getSharedPreferences(prefix + "_prefs", Context.MODE_PRIVATE);
  }

  public void clear() {
    SharedPreferences.Editor e = mSharedPreferences.edit();
    e.clear();
    e.apply();
  }

  /**
   * @param key a textViewProperty key
   * @return true if the key exists in this plugin'circle_blue_border properties
   */
  public boolean hasValueForKey(String key) {
    return mSharedPreferences.contains(key);
  }

  /**
   * removes a textViewProperty and its value from the plugin'circle_blue_border properties
   *
   * @param key a textViewProperty key
   */
  public void removeValueForKey(String key) {
    SharedPreferences.Editor e = mSharedPreferences.edit();
    e.remove(key);
    e.apply();
  }


  /**
   * sets a string textViewProperty with key
   *
   * @param key   a textViewProperty key
   * @param value the value to save. If value is null the textViewProperty is removed.
   */
  public void setValueForKey(String key, String value) {
    if (value == null) {
      removeValueForKey(key);
      return;
    }

    SharedPreferences.Editor e = mSharedPreferences.edit();
    e.putString(key, value);
    e.apply();
  }

  public void setValueForKey(String key, int value) {
    SharedPreferences.Editor e = mSharedPreferences.edit();
    e.putInt(key, value);
    e.apply();
  }

  public String valueForKey(String key) {
    return mSharedPreferences.getString(key, null);
  }

  public String valueForKey(String key, String defValue) {
    return mSharedPreferences.getString(key, defValue);
  }

  public int valueForKey(String key, int defValue) {
    return mSharedPreferences.getInt(key, defValue);
  }

  public void setValueForKey(String key, long value) {
    SharedPreferences.Editor e = mSharedPreferences.edit();
    e.putLong(key, value);
    e.apply();
  }

  public long valueForKey(String key, long defValue) {
    return mSharedPreferences.getLong(key, defValue);
  }

  public void setValueForKey(String key, boolean value) {
    SharedPreferences.Editor e = mSharedPreferences.edit();
    e.putBoolean(key, value);
    e.apply();
  }

  public boolean valueForKey(String key, boolean defValue) {
    return mSharedPreferences.getBoolean(key, defValue);
  }

  public void setValueForKey(String key, float value) {
    SharedPreferences.Editor e = mSharedPreferences.edit();
    e.putFloat(key, value);
    e.apply();
  }

  public float valueForKey(String key, float defValue) {
    return mSharedPreferences.getFloat(key, defValue);
  }

  /**
   * @param key   a property key
   * @param value an object that can be converted to JSON
   * @throws JSONException throws exception in case value cannot be converted to json
   */
  public void setJSONObjectForKey(String key, JSONObject value) throws JSONException {
    if (value == null) {
      removeValueForKey(key);
      return;
    }

    SharedPreferences.Editor e = mSharedPreferences.edit();

    e.putString(key, value.toString());

    e.apply();
  }

  /**
   * @param key key a propery key
   * @return an object
   * @throws JSONException throws exception in case value cannot be converted to json
   */
  public JSONObject jsonObjectForKey(String key) throws JSONException {

    if (!hasValueForKey(key))
      return null;

    String str = mSharedPreferences.getString(key, "{}");
    return new JSONObject(str);
  }

  /**
   * @param key   a propery key
   * @param value an array
   * @throws JSONException throws exception in case value cannot be converted to json
   */
  public void setJSONArrayForKey(String key, JSONArray value) throws JSONException {
    if (value == null) {
      removeValueForKey(key);
      return;
    }
    SharedPreferences.Editor e = mSharedPreferences.edit();
    e.putString(key, value.toString());
    e.apply();
  }

  /**
   * @param key key a propery key
   * @return an array
   * @throws JSONException throws exception in case value cannot be converted to json
   */
  public JSONArray jsonArrayForKey(String key) throws JSONException {

    if (!hasValueForKey(key)) {
      return null;
    }

    String str = mSharedPreferences.getString(key, "{}");
    return new JSONArray(str);
  }
}
