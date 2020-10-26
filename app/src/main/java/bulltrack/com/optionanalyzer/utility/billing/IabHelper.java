package bulltrack.com.optionanalyzer.utility.billing;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.android.vending.billing.IInAppBillingService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;

public class IabHelper {
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE = 2;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    public static final String GET_SKU_DETAILS_ITEM_TYPE_LIST = "ITEM_TYPE_LIST";
    public static final int IABHELPER_BAD_RESPONSE = -1002;
    public static final int IABHELPER_ERROR_BASE = -1000;
    public static final int IABHELPER_INVALID_CONSUMPTION = -1010;
    public static final int IABHELPER_MISSING_TOKEN = -1007;
    public static final int IABHELPER_REMOTE_EXCEPTION = -1001;
    public static final int IABHELPER_SEND_INTENT_FAILED = -1004;
    public static final int IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE = -1009;
    public static final int IABHELPER_SUBSCRIPTION_UPDATE_NOT_AVAILABLE = -1011;
    public static final int IABHELPER_UNKNOWN_ERROR = -1008;
    public static final int IABHELPER_UNKNOWN_PURCHASE_RESPONSE = -1006;
    public static final int IABHELPER_USER_CANCELLED = -1005;
    public static final int IABHELPER_VERIFICATION_FAILED = -1003;
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
    public static final String ITEM_TYPE_INAPP = "inapp";
    public static final String ITEM_TYPE_SUBS = "subs";
    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    boolean mAsyncInProgress = false;
    private final Object mAsyncInProgressLock = new Object();
    String mAsyncOperation = "";
    Context mContext;
    boolean mDebugLog = false;
    String mDebugTag = "IabHelper";
    boolean mDisposeAfterAsync = false;
    boolean mDisposed = false;
    OnIabPurchaseFinishedListener mPurchaseListener;
    String mPurchasingItemType;
    int mRequestCode;
    IInAppBillingService mService;
    ServiceConnection mServiceConn;
    boolean mSetupDone = false;
    String mSignatureBase64 = null;
    boolean mSubscriptionUpdateSupported = false;
    boolean mSubscriptionsSupported = false;

    public interface OnConsumeFinishedListener {
        void onConsumeFinished(Purchase purchase, IabResult iabResult);
    }

    public interface OnConsumeMultiFinishedListener {
        void onConsumeMultiFinished(List<Purchase> list, List<IabResult> list2);
    }

    public interface OnIabPurchaseFinishedListener {
        void onIabPurchaseFinished(IabResult iabResult, Purchase purchase);
    }

    public interface OnIabSetupFinishedListener {
        void onIabSetupFinished(IabResult iabResult);
    }

    public interface QueryInventoryFinishedListener {
        void onQueryInventoryFinished(IabResult iabResult, Inventory inventory);
    }

    public IabHelper(Context context, String str) {
        this.mContext = context.getApplicationContext();
        this.mSignatureBase64 = str;
        logDebug("IAB helper created.");
    }

    public void enableDebugLogging(boolean z, String str) {
        checkNotDisposed();
        this.mDebugLog = z;
        this.mDebugTag = str;
    }

    public void enableDebugLogging(boolean z) {
        checkNotDisposed();
        this.mDebugLog = z;
    }

    public void startSetup(final OnIabSetupFinishedListener onIabSetupFinishedListener) {
        checkNotDisposed();
        if (!this.mSetupDone) {
            logDebug("Starting in-app billing setup.");
            this.mServiceConn = new ServiceConnection() {
                public void onServiceDisconnected(ComponentName componentName) {
                    IabHelper.this.logDebug("Billing service disconnected.");
                    IabHelper.this.mService = null;
                }

                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    if (!IabHelper.this.mDisposed) {
                        IabHelper.this.logDebug("Billing service connected.");
                        IabHelper.this.mService = IInAppBillingService.Stub.asInterface(iBinder);
                        String packageName = IabHelper.this.mContext.getPackageName();
                        try {
                            IabHelper.this.logDebug("Checking for in-app billing 3 support.");
                            int isBillingSupported = IabHelper.this.mService.isBillingSupported(3, packageName, IabHelper.ITEM_TYPE_INAPP);
                            if (isBillingSupported != 0) {
                                if (onIabSetupFinishedListener != null) {
                                    onIabSetupFinishedListener.onIabSetupFinished(new IabResult(isBillingSupported, "Error checking for billing v3 support."));
                                }
                                IabHelper.this.mSubscriptionsSupported = false;
                                IabHelper.this.mSubscriptionUpdateSupported = false;
                                return;
                            }
                            IabHelper iabHelper = IabHelper.this;
                            iabHelper.logDebug("In-app billing version 3 supported for " + packageName);
                            if (IabHelper.this.mService.isBillingSupported(5, packageName, IabHelper.ITEM_TYPE_SUBS) == 0) {
                                IabHelper.this.logDebug("Subscription re-signup AVAILABLE.");
                                IabHelper.this.mSubscriptionUpdateSupported = true;
                            } else {
                                IabHelper.this.logDebug("Subscription re-signup not available.");
                                IabHelper.this.mSubscriptionUpdateSupported = false;
                            }
                            if (IabHelper.this.mSubscriptionUpdateSupported) {
                                IabHelper.this.mSubscriptionsSupported = true;
                            } else {
                                int isBillingSupported2 = IabHelper.this.mService.isBillingSupported(3, packageName, IabHelper.ITEM_TYPE_SUBS);
                                if (isBillingSupported2 == 0) {
                                    IabHelper.this.logDebug("Subscriptions AVAILABLE.");
                                    IabHelper.this.mSubscriptionsSupported = true;
                                } else {
                                    IabHelper iabHelper2 = IabHelper.this;
                                    iabHelper2.logDebug("Subscriptions NOT AVAILABLE. Response: " + isBillingSupported2);
                                    IabHelper.this.mSubscriptionsSupported = false;
                                    IabHelper.this.mSubscriptionUpdateSupported = false;
                                }
                            }
                            IabHelper.this.mSetupDone = true;
                            OnIabSetupFinishedListener onIabSetupFinishedListener = onIabSetupFinishedListener;
                            if (onIabSetupFinishedListener != null) {
                                onIabSetupFinishedListener.onIabSetupFinished(new IabResult(0, "Setup successful."));
                            }
                        } catch (RemoteException e) {
                            OnIabSetupFinishedListener onIabSetupFinishedListener2 = onIabSetupFinishedListener;
                            if (onIabSetupFinishedListener2 != null) {
                                onIabSetupFinishedListener2.onIabSetupFinished(new IabResult(IabHelper.IABHELPER_REMOTE_EXCEPTION, "RemoteException while setting up in-app billing."));
                            }
                            e.printStackTrace();
                        }
                    }
                }
            };
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage("com.android.vending");
            List<ResolveInfo> queryIntentServices = this.mContext.getPackageManager().queryIntentServices(intent, 0);
            if (queryIntentServices != null && !queryIntentServices.isEmpty()) {
                this.mContext.bindService(intent, this.mServiceConn, 1);
            } else if (onIabSetupFinishedListener != null) {
                onIabSetupFinishedListener.onIabSetupFinished(new IabResult(3, "Billing service unavailable on device."));
            }
        } else {
            throw new IllegalStateException("IAB helper is already set up.");
        }
    }

    public void dispose() throws IabAsyncInProgressException {
        synchronized (this.mAsyncInProgressLock) {
            if (this.mAsyncInProgress) {
                throw new IabAsyncInProgressException("Can't dispose because an async operation (" + this.mAsyncOperation + ") is in progress.");
            }
        }
        logDebug("Disposing.");
        this.mSetupDone = false;
        if (this.mServiceConn != null) {
            logDebug("Unbinding from service.");
            Context context = this.mContext;
            if (context != null) {
                context.unbindService(this.mServiceConn);
            }
        }
        this.mDisposed = true;
        this.mContext = null;
        this.mServiceConn = null;
        this.mService = null;
        this.mPurchaseListener = null;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|(1:5)(2:6|7)|8|9) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0013 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void disposeWhenFinished() {
        /*
            r2 = this;
            java.lang.Object r0 = r2.mAsyncInProgressLock
            monitor-enter(r0)
            boolean r1 = r2.mAsyncInProgress     // Catch:{ all -> 0x0015 }
            if (r1 == 0) goto L_0x0010
            java.lang.String r1 = "Will dispose after async operation finishes."
            r2.logDebug(r1)     // Catch:{ all -> 0x0015 }
            r1 = 1
            r2.mDisposeAfterAsync = r1     // Catch:{ all -> 0x0015 }
            goto L_0x0013
        L_0x0010:
            r2.dispose()     // Catch:{ IabAsyncInProgressException -> 0x0013 }
        L_0x0013:
            monitor-exit(r0)     // Catch:{ all -> 0x0015 }
            return
        L_0x0015:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0015 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.utility.billing.IabHelper.disposeWhenFinished():void");
    }

    private void checkNotDisposed() {
        if (this.mDisposed) {
            throw new IllegalStateException("IabHelper was disposed of, so it cannot be used.");
        }
    }

    public boolean subscriptionsSupported() {
        checkNotDisposed();
        return this.mSubscriptionsSupported;
    }

    public void launchPurchaseFlow(Activity activity, String str, int i, OnIabPurchaseFinishedListener onIabPurchaseFinishedListener) throws IabAsyncInProgressException {
        launchPurchaseFlow(activity, str, i, onIabPurchaseFinishedListener, "");
    }

    public void launchPurchaseFlow(Activity activity, String str, int i, OnIabPurchaseFinishedListener onIabPurchaseFinishedListener, String str2) throws IabAsyncInProgressException {
        launchPurchaseFlow(activity, str, ITEM_TYPE_INAPP, (List<String>) null, i, onIabPurchaseFinishedListener, str2);
    }

    public void launchSubscriptionPurchaseFlow(Activity activity, String str, int i, OnIabPurchaseFinishedListener onIabPurchaseFinishedListener) throws IabAsyncInProgressException {
        launchSubscriptionPurchaseFlow(activity, str, i, onIabPurchaseFinishedListener, "");
    }

    public void launchSubscriptionPurchaseFlow(Activity activity, String str, int i, OnIabPurchaseFinishedListener onIabPurchaseFinishedListener, String str2) throws IabAsyncInProgressException {
        launchPurchaseFlow(activity, str, ITEM_TYPE_SUBS, (List<String>) null, i, onIabPurchaseFinishedListener, str2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x009a A[Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00c2 A[Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void launchPurchaseFlow(Activity r14, String r15, String r16, List<String> r17, int r18, OnIabPurchaseFinishedListener r19, String r20) throws IabAsyncInProgressException {
        /*
            r13 = this;
            r1 = r13
            r9 = r15
            r0 = r16
            r10 = r18
            r11 = r19
            r13.checkNotDisposed()
            java.lang.String r2 = "launchPurchaseFlow"
            r13.checkSetupDone(r2)
            r13.flagStartAsync(r2)
            java.lang.String r2 = "subs"
            boolean r2 = r0.equals(r2)
            r12 = 0
            if (r2 == 0) goto L_0x0032
            boolean r2 = r1.mSubscriptionsSupported
            if (r2 != 0) goto L_0x0032
            bulltrack.com.optionanalyzer.utility.billing.IabResult r0 = new bulltrack.com.optionanalyzer.utility.billing.IabResult
            r2 = -1009(0xfffffffffffffc0f, float:NaN)
            java.lang.String r3 = "Subscriptions are not available."
            r0.<init>(r2, r3)
            r13.flagEndAsync()
            if (r11 == 0) goto L_0x0031
            r11.onIabPurchaseFinished(r0, r12)
        L_0x0031:
            return
        L_0x0032:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r2.<init>()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r3 = "Constructing buy intent for "
            r2.append(r3)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r2.append(r15)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r3 = ", item type: "
            r2.append(r3)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r2.append(r0)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r2 = r2.toString()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r13.logDebug(r2)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            if (r17 == 0) goto L_0x0082
            boolean r2 = r17.isEmpty()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            if (r2 == 0) goto L_0x0057
            goto L_0x0082
        L_0x0057:
            boolean r2 = r1.mSubscriptionUpdateSupported     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            if (r2 != 0) goto L_0x006d
            bulltrack.com.optionanalyzer.utility.billing.IabResult r0 = new bulltrack.com.optionanalyzer.utility.billing.IabResult     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r2 = -1011(0xfffffffffffffc0d, float:NaN)
            java.lang.String r3 = "Subscription updates are not available."
            r0.<init>(r2, r3)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r13.flagEndAsync()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            if (r11 == 0) goto L_0x006c
            r11.onIabPurchaseFinished(r0, r12)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
        L_0x006c:
            return
        L_0x006d:
            com.android.vending.billing.IInAppBillingService r2 = r1.mService     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r3 = 5
            android.content.Context r4 = r1.mContext     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r4 = r4.getPackageName()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r5 = r17
            r6 = r15
            r7 = r16
            r8 = r20
            android.os.Bundle r2 = r2.getBuyIntentToReplaceSkus(r3, r4, r5, r6, r7, r8)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            goto L_0x0094
        L_0x0082:
            com.android.vending.billing.IInAppBillingService r2 = r1.mService     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r3 = 3
            android.content.Context r4 = r1.mContext     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r4 = r4.getPackageName()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r5 = r15
            r6 = r16
            r7 = r20
            android.os.Bundle r2 = r2.getBuyIntent(r3, r4, r5, r6, r7)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
        L_0x0094:
            int r3 = r13.getResponseCodeFromBundle(r2)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            if (r3 == 0) goto L_0x00c2
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r0.<init>()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r2 = "Unable to buy item, Error response: "
            r0.append(r2)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r2 = getResponseDesc(r3)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r0.append(r2)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r0 = r0.toString()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r13.logError(r0)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r13.flagEndAsync()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            bulltrack.com.optionanalyzer.utility.billing.IabResult r0 = new bulltrack.com.optionanalyzer.utility.billing.IabResult     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r2 = "Unable to buy item"
            r0.<init>(r3, r2)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            if (r11 == 0) goto L_0x00c1
            r11.onIabPurchaseFinished(r0, r12)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
        L_0x00c1:
            return
        L_0x00c2:
            java.lang.String r3 = "BUY_INTENT"
            android.os.Parcelable r2 = r2.getParcelable(r3)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            android.app.PendingIntent r2 = (android.app.PendingIntent) r2     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r3.<init>()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r4 = "Launching buy intent for "
            r3.append(r4)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r3.append(r15)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r4 = ". Request code: "
            r3.append(r4)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r3.append(r10)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.String r3 = r3.toString()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r13.logDebug(r3)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r1.mRequestCode = r10     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r1.mPurchaseListener = r11     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r1.mPurchasingItemType = r0     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            android.content.IntentSender r3 = r2.getIntentSender()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            android.content.Intent r5 = new android.content.Intent     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r5.<init>()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r0 = 0
            java.lang.Integer r2 = java.lang.Integer.valueOf(r0)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            int r6 = r2.intValue()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r0)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            int r7 = r2.intValue()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            int r8 = r0.intValue()     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            r2 = r14
            r4 = r18
            r2.startIntentSenderForResult(r3, r4, r5, r6, r7, r8)     // Catch:{ SendIntentException -> 0x013f, RemoteException -> 0x0115 }
            goto L_0x0168
        L_0x0115:
            r0 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "RemoteException while launching purchase flow for sku "
            r2.append(r3)
            r2.append(r15)
            java.lang.String r2 = r2.toString()
            r13.logError(r2)
            r0.printStackTrace()
            r13.flagEndAsync()
            bulltrack.com.optionanalyzer.utility.billing.IabResult r0 = new bulltrack.com.optionanalyzer.utility.billing.IabResult
            r2 = -1001(0xfffffffffffffc17, float:NaN)
            java.lang.String r3 = "Remote exception while starting purchase flow"
            r0.<init>(r2, r3)
            if (r11 == 0) goto L_0x0168
            r11.onIabPurchaseFinished(r0, r12)
            goto L_0x0168
        L_0x013f:
            r0 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "SendIntentException while launching purchase flow for sku "
            r2.append(r3)
            r2.append(r15)
            java.lang.String r2 = r2.toString()
            r13.logError(r2)
            r0.printStackTrace()
            r13.flagEndAsync()
            bulltrack.com.optionanalyzer.utility.billing.IabResult r0 = new bulltrack.com.optionanalyzer.utility.billing.IabResult
            r2 = -1004(0xfffffffffffffc14, float:NaN)
            java.lang.String r3 = "Failed to send intent."
            r0.<init>(r2, r3)
            if (r11 == 0) goto L_0x0168
            r11.onIabPurchaseFinished(r0, r12)
        L_0x0168:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.utility.billing.IabHelper.launchPurchaseFlow(android.app.Activity, java.lang.String, java.lang.String, java.util.List, int, bulltrack.com.optionanalyzer.utility.billing.IabHelper$OnIabPurchaseFinishedListener, java.lang.String):void");
    }

    public boolean handleActivityResult(int i, int i2, Intent intent) {
        if (i != this.mRequestCode) {
            return false;
        }
        checkNotDisposed();
        checkSetupDone("handleActivityResult");
        flagEndAsync();
        if (intent == null) {
            logError("Null data in IAB activity result.");
            IabResult iabResult = new IabResult(IABHELPER_BAD_RESPONSE, "Null data in IAB result");
            OnIabPurchaseFinishedListener onIabPurchaseFinishedListener = this.mPurchaseListener;
            if (onIabPurchaseFinishedListener != null) {
                onIabPurchaseFinishedListener.onIabPurchaseFinished(iabResult, (Purchase) null);
            }
            return true;
        }
        int responseCodeFromIntent = getResponseCodeFromIntent(intent);
        String stringExtra = intent.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
        String stringExtra2 = intent.getStringExtra(RESPONSE_INAPP_SIGNATURE);
        if (i2 == -1 && responseCodeFromIntent == 0) {
            logDebug("Successful resultcode from purchase activity.");
            logDebug("Purchase data: " + stringExtra);
            logDebug("Data signature: " + stringExtra2);
            logDebug("Extras: " + intent.getExtras());
            logDebug("Expected item type: " + this.mPurchasingItemType);
            if (stringExtra == null || stringExtra2 == null) {
                logError("BUG: either purchaseData or dataSignature is null.");
                logDebug("Extras: " + intent.getExtras().toString());
                IabResult iabResult2 = new IabResult(IABHELPER_UNKNOWN_ERROR, "IAB returned null purchaseData or dataSignature");
                OnIabPurchaseFinishedListener onIabPurchaseFinishedListener2 = this.mPurchaseListener;
                if (onIabPurchaseFinishedListener2 != null) {
                    onIabPurchaseFinishedListener2.onIabPurchaseFinished(iabResult2, (Purchase) null);
                }
                return true;
            }
            try {
                Purchase purchase = new Purchase(this.mPurchasingItemType, stringExtra, stringExtra2);
                String sku = purchase.getSku();
                if (!Security.verifyPurchase(this.mSignatureBase64, stringExtra, stringExtra2)) {
                    logError("Purchase signature verification FAILED for sku " + sku);
                    IabResult iabResult3 = new IabResult(IABHELPER_VERIFICATION_FAILED, "Signature verification failed for sku " + sku);
                    if (this.mPurchaseListener != null) {
                        this.mPurchaseListener.onIabPurchaseFinished(iabResult3, purchase);
                    }
                    return true;
                }
                logDebug("Purchase signature successfully verified.");
                OnIabPurchaseFinishedListener onIabPurchaseFinishedListener3 = this.mPurchaseListener;
                if (onIabPurchaseFinishedListener3 != null) {
                    onIabPurchaseFinishedListener3.onIabPurchaseFinished(new IabResult(0, "Success"), purchase);
                }
            } catch (JSONException e) {
                logError("Failed to parse purchase data.");
                e.printStackTrace();
                IabResult iabResult4 = new IabResult(IABHELPER_BAD_RESPONSE, "Failed to parse purchase data.");
                OnIabPurchaseFinishedListener onIabPurchaseFinishedListener4 = this.mPurchaseListener;
                if (onIabPurchaseFinishedListener4 != null) {
                    onIabPurchaseFinishedListener4.onIabPurchaseFinished(iabResult4, (Purchase) null);
                }
                return true;
            }
        } else if (i2 == -1) {
            logDebug("Result code was OK but in-app billing response was not OK: " + getResponseDesc(responseCodeFromIntent));
            if (this.mPurchaseListener != null) {
                this.mPurchaseListener.onIabPurchaseFinished(new IabResult(responseCodeFromIntent, "Problem purchashing item."), (Purchase) null);
            }
        } else if (i2 == 0) {
            logDebug("Purchase canceled - Response: " + getResponseDesc(responseCodeFromIntent));
            IabResult iabResult5 = new IabResult(IABHELPER_USER_CANCELLED, "User canceled.");
            OnIabPurchaseFinishedListener onIabPurchaseFinishedListener5 = this.mPurchaseListener;
            if (onIabPurchaseFinishedListener5 != null) {
                onIabPurchaseFinishedListener5.onIabPurchaseFinished(iabResult5, (Purchase) null);
            }
        } else {
            logError("Purchase failed. Result code: " + Integer.toString(i2) + ". Response: " + getResponseDesc(responseCodeFromIntent));
            IabResult iabResult6 = new IabResult(IABHELPER_UNKNOWN_PURCHASE_RESPONSE, "Unknown purchase response.");
            OnIabPurchaseFinishedListener onIabPurchaseFinishedListener6 = this.mPurchaseListener;
            if (onIabPurchaseFinishedListener6 != null) {
                onIabPurchaseFinishedListener6.onIabPurchaseFinished(iabResult6, (Purchase) null);
            }
        }
        return true;
    }

    public Inventory queryInventory() throws IabException {
        return queryInventory(false, (List<String>) null, (List<String>) null);
    }

    public Inventory queryInventory(boolean z, List<String> list, List<String> list2) throws IabException {
        checkNotDisposed();
        checkSetupDone("queryInventory");
        try {
            Inventory inventory = new Inventory();
            int queryPurchases = queryPurchases(inventory, ITEM_TYPE_INAPP);
            if (queryPurchases == 0) {
                if (z) {
                    int querySkuDetails = querySkuDetails(ITEM_TYPE_INAPP, inventory, list);
                    if (querySkuDetails != 0) {
                        throw new IabException(querySkuDetails, "Error refreshing inventory (querying prices of items).");
                    }
                }
                if (this.mSubscriptionsSupported) {
                    int queryPurchases2 = queryPurchases(inventory, ITEM_TYPE_SUBS);
                    if (queryPurchases2 != 0) {
                        throw new IabException(queryPurchases2, "Error refreshing inventory (querying owned subscriptions).");
                    } else if (z) {
                        int querySkuDetails2 = querySkuDetails(ITEM_TYPE_SUBS, inventory, list2);
                        if (querySkuDetails2 != 0) {
                            throw new IabException(querySkuDetails2, "Error refreshing inventory (querying prices of subscriptions).");
                        }
                    }
                }
                return inventory;
            }
            throw new IabException(queryPurchases, "Error refreshing inventory (querying owned items).");
        } catch (RemoteException e) {
            throw new IabException(IABHELPER_REMOTE_EXCEPTION, "Remote exception while refreshing inventory.", e);
        } catch (JSONException e2) {
            throw new IabException(IABHELPER_BAD_RESPONSE, "Error parsing JSON response while refreshing inventory.", e2);
        }
    }

    public void queryInventoryAsync(boolean z, List<String> list, List<String> list2, QueryInventoryFinishedListener queryInventoryFinishedListener) throws IabAsyncInProgressException {
        final Handler handler = new Handler();
        checkNotDisposed();
        checkSetupDone("queryInventory");
        flagStartAsync("refresh inventory");
        final boolean z2 = z;
        final List<String> list3 = list;
        final List<String> list4 = list2;
        final QueryInventoryFinishedListener queryInventoryFinishedListener2 = queryInventoryFinishedListener;
        new Thread(new Runnable() {
            public void run() {
                final Inventory inventory;
                final IabResult iabResult = new IabResult(0, "Inventory refresh successful.");
                try {
                    inventory = IabHelper.this.queryInventory(z2, list3, list4);
                } catch (IabException e) {
                    iabResult = e.getResult();
                    inventory = null;
                }
                IabHelper.this.flagEndAsync();
                if (!IabHelper.this.mDisposed && queryInventoryFinishedListener2 != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            queryInventoryFinishedListener2.onQueryInventoryFinished(iabResult, inventory);
                        }
                    });
                }
            }
        }).start();
    }

    public void queryInventoryAsync(QueryInventoryFinishedListener queryInventoryFinishedListener) throws IabAsyncInProgressException {
        queryInventoryAsync(false, (List<String>) null, (List<String>) null, queryInventoryFinishedListener);
    }

    /* access modifiers changed from: package-private */
    public void consume(Purchase purchase) throws IabException {
        checkNotDisposed();
        checkSetupDone("consume");
        if (purchase.mItemType.equals(ITEM_TYPE_INAPP)) {
            try {
                String token = purchase.getToken();
                String sku = purchase.getSku();
                if (token == null || token.equals("")) {
                    logError("Can't consume " + sku + ". No token.");
                    throw new IabException((int) IABHELPER_MISSING_TOKEN, "PurchaseInfo is missing token for sku: " + sku + " " + purchase);
                }
                logDebug("Consuming sku: " + sku + ", token: " + token);
                int consumePurchase = this.mService.consumePurchase(3, this.mContext.getPackageName(), token);
                if (consumePurchase == 0) {
                    logDebug("Successfully consumed sku: " + sku);
                    return;
                }
                logDebug("Error consuming consuming sku " + sku + ". " + getResponseDesc(consumePurchase));
                throw new IabException(consumePurchase, "Error consuming sku " + sku);
            } catch (RemoteException e) {
                throw new IabException(IABHELPER_REMOTE_EXCEPTION, "Remote exception while consuming. PurchaseInfo: " + purchase, e);
            }
        } else {
            throw new IabException((int) IABHELPER_INVALID_CONSUMPTION, "Items of type '" + purchase.mItemType + "' can't be consumed.");
        }
    }

    public void consumeAsync(Purchase purchase, OnConsumeFinishedListener onConsumeFinishedListener) throws IabAsyncInProgressException {
        checkNotDisposed();
        checkSetupDone("consume");
        ArrayList arrayList = new ArrayList();
        arrayList.add(purchase);
        consumeAsyncInternal(arrayList, onConsumeFinishedListener, (OnConsumeMultiFinishedListener) null);
    }

    public void consumeAsync(List<Purchase> list, OnConsumeMultiFinishedListener onConsumeMultiFinishedListener) throws IabAsyncInProgressException {
        checkNotDisposed();
        checkSetupDone("consume");
        consumeAsyncInternal(list, (OnConsumeFinishedListener) null, onConsumeMultiFinishedListener);
    }

    public static String getResponseDesc(int i) {
        String[] split = "0:OK/1:User Canceled/2:Unknown/3:Billing Unavailable/4:Item unavailable/5:Developer Error/6:Error/7:Item Already Owned/8:Item not owned".split("/");
        String[] split2 = "0:OK/-1001:Remote exception during initialization/-1002:Bad response received/-1003:Purchase signature verification failed/-1004:Send intent failed/-1005:User cancelled/-1006:Unknown purchase response/-1007:Missing token/-1008:Unknown error/-1009:Subscriptions not available/-1010:Invalid consumption attempt".split("/");
        if (i <= -1000) {
            int i2 = -1000 - i;
            if (i2 >= 0 && i2 < split2.length) {
                return split2[i2];
            }
            return String.valueOf(i) + ":Unknown IAB Helper Error";
        } else if (i >= 0 && i < split.length) {
            return split[i];
        } else {
            return String.valueOf(i) + ":Unknown";
        }
    }

    /* access modifiers changed from: package-private */
    public void checkSetupDone(String str) {
        if (!this.mSetupDone) {
            logError("Illegal state for operation (" + str + "): IAB helper is not set up.");
            throw new IllegalStateException("IAB helper is not set up. Can't perform operation: " + str);
        }
    }

    /* access modifiers changed from: package-private */
    public int getResponseCodeFromBundle(Bundle bundle) {
        Object obj = bundle.get(RESPONSE_CODE);
        if (obj == null) {
            logDebug("Bundle with null response code, assuming OK (known issue)");
            return 0;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        } else {
            if (obj instanceof Long) {
                return (int) ((Long) obj).longValue();
            }
            logError("Unexpected type for bundle response code.");
            logError(obj.getClass().getName());
            throw new RuntimeException("Unexpected type for bundle response code: " + obj.getClass().getName());
        }
    }

    /* access modifiers changed from: package-private */
    public int getResponseCodeFromIntent(Intent intent) {
        Object obj = intent.getExtras().get(RESPONSE_CODE);
        if (obj == null) {
            logError("Intent with no response code, assuming OK (known issue)");
            return 0;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        } else {
            if (obj instanceof Long) {
                return (int) ((Long) obj).longValue();
            }
            logError("Unexpected type for intent response code.");
            logError(obj.getClass().getName());
            throw new RuntimeException("Unexpected type for intent response code: " + obj.getClass().getName());
        }
    }

    /* access modifiers changed from: package-private */
    public void flagStartAsync(String str) throws IabAsyncInProgressException {
        synchronized (this.mAsyncInProgressLock) {
            if (!this.mAsyncInProgress) {
                this.mAsyncOperation = str;
                this.mAsyncInProgress = true;
                logDebug("Starting async operation: " + str);
            } else {
                throw new IabAsyncInProgressException("Can't start async operation (" + str + ") because another async operation (" + this.mAsyncOperation + ") is in progress.");
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|(2:5|6)|7|8) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0027 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void flagEndAsync() {
        /*
            r3 = this;
            java.lang.Object r0 = r3.mAsyncInProgressLock
            monitor-enter(r0)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0029 }
            r1.<init>()     // Catch:{ all -> 0x0029 }
            java.lang.String r2 = "Ending async operation: "
            r1.append(r2)     // Catch:{ all -> 0x0029 }
            java.lang.String r2 = r3.mAsyncOperation     // Catch:{ all -> 0x0029 }
            r1.append(r2)     // Catch:{ all -> 0x0029 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0029 }
            r3.logDebug(r1)     // Catch:{ all -> 0x0029 }
            java.lang.String r1 = ""
            r3.mAsyncOperation = r1     // Catch:{ all -> 0x0029 }
            r1 = 0
            r3.mAsyncInProgress = r1     // Catch:{ all -> 0x0029 }
            boolean r1 = r3.mDisposeAfterAsync     // Catch:{ all -> 0x0029 }
            if (r1 == 0) goto L_0x0027
            r3.dispose()     // Catch:{ IabAsyncInProgressException -> 0x0027 }
        L_0x0027:
            monitor-exit(r0)     // Catch:{ all -> 0x0029 }
            return
        L_0x0029:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0029 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.utility.billing.IabHelper.flagEndAsync():void");
    }

    public static class IabAsyncInProgressException extends Exception {
        public IabAsyncInProgressException(String str) {
            super(str);
        }
    }

    /* access modifiers changed from: package-private */
    public int queryPurchases(Inventory inventory, String str) throws JSONException, RemoteException {
        logDebug("Querying owned items, item type: " + str);
        logDebug("Package name: " + this.mContext.getPackageName());
        String str2 = null;
        boolean z = false;
        do {
            logDebug("Calling getPurchases with continuation token: " + str2);
            Bundle purchases = this.mService.getPurchases(3, this.mContext.getPackageName(), str, str2);
            int responseCodeFromBundle = getResponseCodeFromBundle(purchases);
            logDebug("Owned items response: " + String.valueOf(responseCodeFromBundle));
            if (responseCodeFromBundle != 0) {
                logDebug("getPurchases() failed: " + getResponseDesc(responseCodeFromBundle));
                return responseCodeFromBundle;
            } else if (!purchases.containsKey(RESPONSE_INAPP_ITEM_LIST) || !purchases.containsKey(RESPONSE_INAPP_PURCHASE_DATA_LIST) || !purchases.containsKey(RESPONSE_INAPP_SIGNATURE_LIST)) {
                logError("Bundle returned from getPurchases() doesn't contain required fields.");
                return IABHELPER_BAD_RESPONSE;
            } else {
                ArrayList<String> stringArrayList = purchases.getStringArrayList(RESPONSE_INAPP_ITEM_LIST);
                ArrayList<String> stringArrayList2 = purchases.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST);
                ArrayList<String> stringArrayList3 = purchases.getStringArrayList(RESPONSE_INAPP_SIGNATURE_LIST);
                for (int i = 0; i < stringArrayList2.size(); i++) {
                    String str3 = stringArrayList2.get(i);
                    String str4 = stringArrayList3.get(i);
                    String str5 = stringArrayList.get(i);
                    if (Security.verifyPurchase(this.mSignatureBase64, str3, str4)) {
                        logDebug("Sku is owned: " + str5);
                        Purchase purchase = new Purchase(str, str3, str4);
                        if (TextUtils.isEmpty(purchase.getToken())) {
                            logWarn("BUG: empty/null token!");
                            logDebug("Purchase data: " + str3);
                        }
                        inventory.addPurchase(purchase);
                    } else {
                        logWarn("Purchase signature verification **FAILED**. Not adding item.");
                        logDebug("   Purchase data: " + str3);
                        logDebug("   Signature: " + str4);
                        z = true;
                    }
                }
                str2 = purchases.getString(INAPP_CONTINUATION_TOKEN);
                logDebug("Continuation token: " + str2);
            }
        } while (!TextUtils.isEmpty(str2));
        if (z) {
            return IABHELPER_VERIFICATION_FAILED;
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int querySkuDetails(String str, Inventory inventory, List<String> list) throws RemoteException, JSONException {
        logDebug("Querying SKU details.");
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(inventory.getAllOwnedSkus(str));
        if (list != null) {
            for (String next : list) {
                if (!arrayList.contains(next)) {
                    arrayList.add(next);
                }
            }
        }
        if (arrayList.size() == 0) {
            logDebug("queryPrices: nothing to do because there are no SKUs.");
            return 0;
        }
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size() / 20;
        int size2 = arrayList.size() % 20;
        for (int i = 0; i < size; i++) {
            ArrayList arrayList3 = new ArrayList();
            int i2 = i * 20;
            for (String add : arrayList.subList(i2, i2 + 20)) {
                arrayList3.add(add);
            }
            arrayList2.add(arrayList3);
        }
        if (size2 != 0) {
            ArrayList arrayList4 = new ArrayList();
            int i3 = size * 20;
            for (String add2 : arrayList.subList(i3, size2 + i3)) {
                arrayList4.add(add2);
            }
            arrayList2.add(arrayList4);
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, (ArrayList) it.next());
            Bundle skuDetails = this.mService.getSkuDetails(3, this.mContext.getPackageName(), str, bundle);
            if (!skuDetails.containsKey(RESPONSE_GET_SKU_DETAILS_LIST)) {
                int responseCodeFromBundle = getResponseCodeFromBundle(skuDetails);
                if (responseCodeFromBundle != 0) {
                    logDebug("getSkuDetails() failed: " + getResponseDesc(responseCodeFromBundle));
                    return responseCodeFromBundle;
                }
                logError("getSkuDetails() returned a bundle with neither an error nor a detail list.");
                return IABHELPER_BAD_RESPONSE;
            }
            Iterator<String> it2 = skuDetails.getStringArrayList(RESPONSE_GET_SKU_DETAILS_LIST).iterator();
            while (it2.hasNext()) {
                SkuDetails skuDetails2 = new SkuDetails(str, it2.next());
                logDebug("Got sku details: " + skuDetails2);
                inventory.addSkuDetails(skuDetails2);
            }
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public void consumeAsyncInternal(List<Purchase> list, OnConsumeFinishedListener onConsumeFinishedListener, OnConsumeMultiFinishedListener onConsumeMultiFinishedListener) throws IabAsyncInProgressException {
        final Handler handler = new Handler();
        flagStartAsync("consume");
        final List<Purchase> list2 = list;
        final OnConsumeFinishedListener onConsumeFinishedListener2 = onConsumeFinishedListener;
        final OnConsumeMultiFinishedListener onConsumeMultiFinishedListener2 = onConsumeMultiFinishedListener;
        new Thread(new Runnable() {
            public void run() {
                final ArrayList arrayList = new ArrayList();
                for (Purchase purchase : list2) {
                    try {
                        IabHelper.this.consume(purchase);
                        arrayList.add(new IabResult(0, "Successful consume of sku " + purchase.getSku()));
                    } catch (IabException e) {
                        arrayList.add(e.getResult());
                    }
                }
                IabHelper.this.flagEndAsync();
                if (!IabHelper.this.mDisposed && onConsumeFinishedListener2 != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            onConsumeFinishedListener2.onConsumeFinished((Purchase) list2.get(0), (IabResult) arrayList.get(0));
                        }
                    });
                }
                if (!IabHelper.this.mDisposed && onConsumeMultiFinishedListener2 != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            onConsumeMultiFinishedListener2.onConsumeMultiFinished(list2, arrayList);
                        }
                    });
                }
            }
        }).start();
    }

    /* access modifiers changed from: package-private */
    public void logDebug(String str) {
        if (this.mDebugLog) {
            Log.d(this.mDebugTag, str);
        }
    }

    /* access modifiers changed from: package-private */
    public void logError(String str) {
        String str2 = this.mDebugTag;
        Log.e(str2, "In-app billing error: " + str);
    }

    /* access modifiers changed from: package-private */
    public void logWarn(String str) {
        String str2 = this.mDebugTag;
        Log.w(str2, "In-app billing warning: " + str);
    }
}
