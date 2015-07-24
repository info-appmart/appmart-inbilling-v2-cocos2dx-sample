#include "SampleJni.h"
#include "platform/android/jni/JniHelper.h"

#define ACCESS_CLASS "org.cocos2dx.cpp.SamplePaymentActivity"

extern "C" {

void setInventory() {
	cocos2d::JniMethodInfo jMInfo;
	
	if (cocos2d::JniHelper::getStaticMethodInfo(jMInfo, ACCESS_CLASS,
			"requestSetInventory", "()V")) {
		jMInfo.env->CallStaticVoidMethod(jMInfo.classID, jMInfo.methodID);
		jMInfo.env->DeleteLocalRef(jMInfo.classID);
	}
}
void requestPayment(int callService){
	cocos2d::JniMethodInfo jMInfo;
	if(cocos2d::JniHelper::getStaticMethodInfo(jMInfo, ACCESS_CLASS, "requestPayment", "(I)V")){
		jMInfo.env->CallStaticVoidMethod(jMInfo.classID, jMInfo.methodID, callService);
		jMInfo.env->DeleteLocalRef(jMInfo.classID);
	}
}
void requestConsume(){
	cocos2d::JniMethodInfo jMInfo;
	if(cocos2d::JniHelper::getStaticMethodInfo(jMInfo, ACCESS_CLASS, "startManualConsumeService", "()V")){
		jMInfo.env->CallStaticVoidMethod(jMInfo.classID, jMInfo.methodID);
		jMInfo.env->DeleteLocalRef(jMInfo.classID);
	}
}

}
