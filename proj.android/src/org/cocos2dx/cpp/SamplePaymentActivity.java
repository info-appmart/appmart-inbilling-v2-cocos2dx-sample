package org.cocos2dx.cpp;

import java.util.ArrayList;
import java.util.List;

import jp.app_mart.billing.v2.AppmartIabHelper;
import jp.app_mart.billing.v2.IabException;
import jp.app_mart.billing.v2.IabResult;
import jp.app_mart.billing.v2.Inventory;
import jp.app_mart.billing.v2.Purchase;
import jp.app_mart.billing.v2.SkuDetails;
import jp.app_mart.billing.v2.AppmartIabHelper.OnIabPurchaseFinishedListener;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SamplePaymentActivity extends Cocos2dxActivity {
	private static final String MARKET = "market";
	private static final int MARKET_TYPE_GOOGLE_PLAY = 1;
	private static final int MARKET_TYPE_OTHER = 2;
	
	private static final int AUTO_CONSUME = 1;
	private static final int MANUAL_CONSUME = 2;

	// アプリ情報
	private static final String APPMART_APP_ID = "your appId";
	private static final String APPMART_LICENSE_KEY = "your licenseKey";
	private static final String APPMART_SERVICE = "your servuce";
	
	private static final boolean isDebug = true;

	public static AppmartIabHelper mHelper;

	static SamplePaymentActivity me = null;
	static String lTAG = "appmart";

	/**
	 * /////////////////////////////////////////////////////////////////////////
	 * 
	 * Overrides
	 * 
	 * /////////////////////////////////////////////////////////////////////////
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		me = this;
		mHelper = new AppmartIabHelper(me, APPMART_APP_ID, APPMART_LICENSE_KEY);
		mHelper.startSetup(new AppmartIabHelper.OnIabSetupFinishedListener() {
			// 設定完了後呼ばれるcallback
			@Override
			public void onIabSetupFinished(IabResult result) {
				if(!result.isSuccess()){
					if(result.getResponse() == AppmartIabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE)
						me.debugMess("appmartを更新してください。");
					Log.d("appmart", "appmartアプリ内課金：問題が発生しました。"+result);
					return;
				}else{
					Log.d("appmart", "appmartアプリ内課金：設定完了");
				}
				requestSetInventory();
			}
		});

		// マーケット情報
		getMarketType();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(lTAG, "onActivityResult handled by AppmartIabHelper.");
		}
	}

	/**
	 * /////////////////////////////////////////////////////////////////////////
	 * 
	 * CallStaticMethods
	 * 
	 * /////////////////////////////////////////////////////////////////////////
	 */

	// //////////////////////////////////////
	// helper設定
	// //////////////////////////////////////
	public static void setHelper(){
		try{
			mHelper = new AppmartIabHelper(me, APPMART_APP_ID, APPMART_LICENSE_KEY);
			mHelper.startSetup(new AppmartIabHelper.OnIabSetupFinishedListener() {
				// 設定完了後呼ばれるcallback
				@Override
				public void onIabSetupFinished(IabResult result) {
					if(!result.isSuccess()){
						if(result.getResponse() == AppmartIabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE)
							me.debugMess("appmartを更新してください。");
						Log.d("appmart", "appmartアプリ内課金：問題が発生しました。"+result);
					}else{
						Log.d("appmart", "appmartアプリ内課金：設定完了");
					}
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////
	// Inventory取得
	// //////////////////////////////////////
	public static void requestSetInventory() {
		List<String> list = new ArrayList<String>();
		list.add(APPMART_SERVICE);
		mHelper.queryInventoryAsync(list, new AppmartIabHelper.QueryInventoryFinishedListener() {
			@Override
			public void onQueryInventoryFinished(IabResult result,
					Inventory inv) {
				if (result.isFailure()) {
					if (result.getResponse() == AppmartIabHelper.BILLING_RESPONSE_USER_NOT_LOG) {
						me.debugMess("appmartでログインしてください。");
					} else {
						me.debugMess(result.getMessage());
					}
				}
			}
		});
	}

	// //////////////////////////////////////
	// サービス購入
	// //////////////////////////////////////
	public static void requestPayment(int callService) {
		try{
			if (callService == AUTO_CONSUME) {
				me.buyService(APPMART_SERVICE, true);
			} else if (callService == MANUAL_CONSUME) {
				me.buyService(APPMART_SERVICE, false);
			} else {
				me.debugMess("サービスの購入に失敗しました。");
			}
		}catch(Exception e){
			me.debugMess("エラーが発生しました。");
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////
	// サービス消費
	// //////////////////////////////////////
	// 手動消費
	public static void startManualConsumeService() {
		try {
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(APPMART_SERVICE));

			me.manualConsumeService(list);
		} catch (Exception e) {
			e.printStackTrace();
			me.debugMess("サービス消費処理が失敗しました。");
		}
	}

	// //////////////////////////////////////
	// マーケット情報
	// //////////////////////////////////////
	public static int getMarketType() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(SamplePaymentActivity.getContext());
		int _market;
		if ((!pref.contains(MARKET)) || pref.getInt(MARKET, 0) == 0) {
			// マーケット情報の保存
			PackageManager pm = SamplePaymentActivity.getContext()
					.getPackageManager();
			SharedPreferences.Editor edit = pref.edit();
			String installationSource = pm
					.getInstallerPackageName(Cocos2dxActivity.getContext()
							.getPackageName());

			if (installationSource != null
					&& installationSource.equals("com.android.vending")) {
				Log.d("DEBUG", "google Play(c)からインストールされました。");
				edit.putInt(MARKET, MARKET_TYPE_GOOGLE_PLAY);
			} else {
				Log.d("DEBUG", "google Play(c)以外のマーケットからインストールされました。");
				edit.putInt(MARKET, MARKET_TYPE_OTHER);
			}
			edit.commit();
		}
		_market = pref.getInt(MARKET, 0);

		return _market;
	}

	/**
	 * /////////////////////////////////////////////////////////////////////////
	 * 
	 * OtherMethods
	 * 
	 * /////////////////////////////////////////////////////////////////////////
	 */
	// //////////////////////////////////////
	// サービス購入
	// //////////////////////////////////////
	protected void buyService(String serviceId, boolean autoFlg) {
		mHelper.launchPurchaseFlow(this, serviceId, 10001,
				getIPFListener(autoFlg), "test string");
	}

	private OnIabPurchaseFinishedListener getIPFListener(final boolean autoFlg) {
		// 決済完了後callback
		AppmartIabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new AppmartIabHelper.OnIabPurchaseFinishedListener() {
			@Override
			public void onIabPurchaseFinished(IabResult result, Purchase info) {
				if (result.isFailure()) {
					Log.d(lTAG, "購入エラー：" + result);
					return;
				}
				debugMess("サービスが購入されました（" + info.getSku() + "）");

				if (autoFlg)
					autoConsumeService(info);
			}
		};

		return mPurchaseFinishedListener;
	}

	// //////////////////////////////////////
	// サービス消費
	// //////////////////////////////////////
	public void consumeServiceItem(Inventory mInventory, SkuDetails entory) {
		me.consumeService(mInventory, entory);
	}

	private void consumeService(Inventory mInventory, SkuDetails entory) {
		// Inventoryで購入履歴チェック
		if (mInventory.hasPurchase(entory.getSku())) {
			Purchase p = mInventory.getPurchase(entory.getSku());
			mHelper.consumeAsync(p, getCFListener());
		} else {
			Log.d(this.getClass().getSimpleName(), "未消費の情報がございません。");
		}
	}

	private AppmartIabHelper.OnConsumeFinishedListener getCFListener() {
		// サービス消費後callback
		AppmartIabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new AppmartIabHelper.OnConsumeFinishedListener() {
			@Override
			public void onConsumeFinished(Purchase purchase, IabResult result) {
				if (result.isFailure()) {
					debugMess("サービスが消費されませんでした。");
				} else {
					debugMess("サービスが消費されました");
				}
			}
		};

		return mConsumeFinishedListener;
	}

	// 自動呼び出し
	private void autoConsumeService(Purchase p) {
		// 購入履歴をチェック
		mHelper.consumeAsync(p, getCFListener());
	}

	// 手動時呼び出し
	private void manualConsumeService(List<String> list) throws IabException {
		if (!mHelper.isAsyncInProgress()) {
			Inventory mInventory = mHelper.queryInventory(list);
			if (mInventory.getAllOwnedSkus().size() > 0) {
				String[] sArray = (String[]) mInventory
						.getAllOwnedSkus()
						.toArray(
								new String[mInventory.getAllOwnedSkus().size()]);

				for (int i = 0; i < sArray.length; i++) {
					if (mInventory.hasPurchase(sArray[i])) {
						final Purchase p = mInventory.getPurchase(sArray[i]);

						Cocos2dxHelper.getActivity().runOnUiThread(
								new Runnable() {
									@Override
									public void run() {
										mHelper.consumeAsync(p, getCFListener());
									}
								});
					}
				}
			}
		}
	}

	// //////////////////////////////////////
	// Other
	// //////////////////////////////////////

	// デバッグ用メッセージ表示
	private void debugMess(String mess) {
		if (isDebug) {
			Log.d("DEBUG", mess);
			Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT)
					.show();
		}
		return;
	}

}